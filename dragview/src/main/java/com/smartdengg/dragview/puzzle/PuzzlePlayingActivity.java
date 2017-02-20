package com.smartdengg.dragview.puzzle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.smartdengg.dragview.MarginDecoration;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.v4.widget.ViewDragHelper.STATE_IDLE;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.RIGHT_OF;
import static com.smartdengg.dragview.puzzle.ImageSize.LARGE;
import static com.smartdengg.dragview.puzzle.ImageSize.LAYOUT_PARAMS;
import static com.smartdengg.dragview.puzzle.ImageSize.NORMAL;

/**
 * 创建时间:  2016/12/28 12:31 <br>
 * 作者:  dengwei <br>
 * 描述:  "使用拼了"营销房源 - 玩转拼图界面
 */
public class PuzzlePlayingActivity extends Activity
    implements PuzzlePlayingTempAdapter.Callback, DragView.Callback {

  public static final String TAG = PuzzlePlayingActivity.class.getSimpleName();
  public static final String ITEMS = "ITEMS";
  public static final String TEMPLATES = "TEMPLATES";

  private DragControllerLayout dragControllerLayout;
  private RecyclerView mTemplateRecyclerView;
  private ViewGroup mTipContainer;
  private ArrayList<ImageItem> mItems;

  private PuzzlePlayingTempAdapter mPuzzlePlayingTempAdapter;
  private ObjectAnimator mTipContainerAnimator;

  private ArrayList<TemplateGroup.Temp> mTempEntities;
  private TemplateGroup.Temp mCurrentTemp;
  private DragControllerLayout.Behavior currentBehavior = DragControllerLayout.Behavior.NORMAL;

  private final List<DragView> dragViews = new ArrayList<>(6);
  private int dragViewsContainerHeight;
  private int dragViewsContainerWidth;
  private int puzzleMargin;

  public static void start(Context context, List<ImageItem> items) {

    Intent intent = new Intent(context, PuzzlePlayingActivity.class);
    if (items instanceof ArrayList) {
      intent.putExtra(ITEMS, (Serializable) items);
    } else {
      intent.putExtra(ITEMS, new ArrayList<>(items));
    }
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activty_puzzle_playing_layout);

    if (savedInstanceState == null) {
      //noinspection unchecked
      this.mItems = (ArrayList<ImageItem>) getIntent().getSerializableExtra(ITEMS);
    } else {
      //当ACT被系统销毁时，并重建后，我们应该保持之前的选中顺序不变
      //noinspection unchecked
      this.mItems = (ArrayList<ImageItem>) savedInstanceState.getSerializable(ITEMS);
      //noinspection unchecked
      this.mTempEntities =
          (ArrayList<TemplateGroup.Temp>) savedInstanceState.getSerializable(TEMPLATES);
    }

    this.initView();
    this.initTemplate();
  }

  private void initView() {

    this.puzzleMargin = getResources().getDimensionPixelSize(R.dimen.material_4dp);

    Button completedBtn = (Button) findViewById(R.id.puzzle_completed);
    completedBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dragControllerLayout.setBehavior(currentBehavior = DragControllerLayout.Behavior.NORMAL);
      }
    });

    this.dragControllerLayout = (DragControllerLayout) findViewById(R.id.puzzle_dragframe_layout);
    this.mTipContainer = (ViewGroup) findViewById(R.id.puzzle_tip_container);
    this.mTemplateRecyclerView = (RecyclerView) findViewById(R.id.puzzle_template_recyclerview);
    ViewGroup viewGroup = (ViewGroup) mTemplateRecyclerView.getParent();
    viewGroup.bringChildToFront(mTemplateRecyclerView);

    /*处理一些计算尺寸的运算逻辑*/
    dragControllerLayout.post(new CalculateRunnable());

    /*设置拖拽事件监听*/
    dragControllerLayout.setDragFrameController(new DragController());

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PuzzlePlayingActivity.this);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    this.mTemplateRecyclerView.setLayoutManager(linearLayoutManager);
    this.mTemplateRecyclerView.addItemDecoration(new MarginDecoration(this, R.dimen.material_8dp));
  }

  private void swapViewWithAnimation(final DragView capturedView, final DragView targetView) {

    final Rect capturedFinalBound = targetView.getStartBound();
    final Rect targetFinalBound = capturedView.getStartBound();
    float capturedScale = calculateScale(new Rect(capturedFinalBound), new Rect(targetFinalBound));
    float targetScale = calculateScale(new Rect(targetFinalBound), new Rect(capturedFinalBound));

    capturedView.setPivotX(0.0f);
    capturedView.setPivotY(0.0f);
    targetView.setPivotX(0.0f);
    targetView.setPivotY(0.0f);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(ObjectAnimator.ofFloat(capturedView, View.X, capturedFinalBound.left))
        .with(ObjectAnimator.ofFloat(capturedView, View.Y, capturedFinalBound.top))
        .with(ObjectAnimator.ofFloat(capturedView, View.SCALE_X, capturedScale))
        .with(ObjectAnimator.ofFloat(capturedView, View.SCALE_Y, capturedScale))

        .with(ObjectAnimator.ofFloat(targetView, View.X, targetFinalBound.left))
        .with(ObjectAnimator.ofFloat(targetView, View.Y, targetFinalBound.top))
        .with(ObjectAnimator.ofFloat(targetView, View.SCALE_X, targetScale))
        .with(ObjectAnimator.ofFloat(targetView, View.SCALE_Y, targetScale));

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        targetView.setZ(10.0f);
        targetView.getImageButton().setVisibility(View.INVISIBLE);
        targetView.setBackgroundColor(0);
      }

      @Override public void onAnimationEnd(Animator animation) {

        int sourceIndex = dragViews.indexOf(capturedView);
        int targetIndex = dragViews.indexOf(targetView);

        if (sourceIndex != -1 && targetIndex != -1) {
          targetView.setZ(0.0f);
          targetView.getImageButton().setVisibility(View.VISIBLE);
          Collections.swap(dragViews, sourceIndex, targetIndex);
          Collections.swap(mItems, sourceIndex, targetIndex);
        }

        PuzzlePlayingActivity.this.initDragViews(false);

        /*fuck!!!!*/
        //dragRelativeLayout.requestLayout();
        //dragRelativeLayout.invalidate();
        //dragRelativeLayout.setDragViews(dragViews);
      }
    });
    animatorSet.setDuration(3000);
    animatorSet.setInterpolator(new FastOutLinearInInterpolator());
    animatorSet.start();
  }

  private float calculateScale(Rect sourceBounds, Rect targetBounds) {

    float startScale;
    if ((float) targetBounds.width() / targetBounds.height()
        > (float) sourceBounds.width() / sourceBounds.height()) {
      // Extend start bounds horizontally
      startScale = (float) sourceBounds.height() / targetBounds.height();
      float startWidth = startScale * targetBounds.width();
      float deltaWidth = (startWidth - sourceBounds.width()) / 2;
      sourceBounds.left -= deltaWidth;
      sourceBounds.right += deltaWidth;
    } else {
      // Extend start bounds vertically
      startScale = (float) sourceBounds.width() / targetBounds.width();
      float startHeight = startScale * targetBounds.height();
      float deltaHeight = (startHeight - sourceBounds.height()) / 2;
      sourceBounds.top -= deltaHeight;
      sourceBounds.bottom += deltaHeight;
    }
    return startScale;
  }

  private void initTemplate() {

    if (mTempEntities == null || mTempEntities.size() == 0) {
      this.mTempEntities = switchTempGroupMode();
    }

    /*只有一张图片时，隐藏提示条*/
    if (mTempEntities.size() == 1 && mTempEntities.get(0).ID == TemplateGroup.ID.ONE_MODE_11) {
      this.mTipContainer.setVisibility(View.GONE);
    }

    for (TemplateGroup.Temp temp : mTempEntities) {
      if (temp.isSelected) {
        mCurrentTemp = temp;
        break;
      }
    }

    this.mPuzzlePlayingTempAdapter =
        new PuzzlePlayingTempAdapter(PuzzlePlayingActivity.this, mTempEntities);
    this.mPuzzlePlayingTempAdapter.setCallback(this);
    this.mTemplateRecyclerView.setAdapter(mPuzzlePlayingTempAdapter);
  }

  @NonNull private ArrayList<TemplateGroup.Temp> switchTempGroupMode() {
    return new ArrayList<>(TemplateGroup.correspondTemplate(mItems).apply());
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    /*当该界面被系统销毁后，我们必须恢复之前的状态 我们只恢复图片列表以及模板选择，不恢复提示条状态和编辑状态*/
    outState.putSerializable(ITEMS, mItems);
    outState.putSerializable(TEMPLATES, mTempEntities);
    super.onSaveInstanceState(outState);
  }

  //private void initDragViews(boolean hasRemoved) {
  //  int id = mCurrentTemp.ID;
  //  this.dragControllerLayout.clear();
  //
  //  if (hasRemoved) this.dragViews.clear();
  //
  //  switch (id) {
  //    case TemplateGroup.ID.ONE_MODE_11: {
  //
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //      DragView centerDragView;
  //
  //      if (!hasRemoved) {
  //        centerDragView = dragViews.get(0);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        centerDragView.setId(R.id.id_0);
  //        centerDragView.setCallback(this);
  //        /*只有一张拼图是，不可编辑，其他模式默认可编辑*/
  //        centerDragView.setEditable(false);
  //      }
  //
  //      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(largeParams);
  //      params.addRule(CENTER_IN_PARENT);
  //      this.dragControllerLayout.addView(centerDragView, params);
  //      this.dragViews.add(centerDragView);
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.TWO_MODE_21: {
  //
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //      }
  //      centerDragView0.setCallback(this);
  //      centerDragView0.setId(R.id.id_0);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(largeParams);
  //
  //      centerDragView1.setCallback(this);
  //      centerDragView1.setId(R.id.id_1);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(largeParams);
  //      params1.addRule(BELOW, R.id.id_0);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.THREE_MODE_31: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(largeParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(BELOW, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //      params2.addRule(RIGHT_OF, R.id.id_1);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.THREE_MODE_32: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(RIGHT_OF, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(largeParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.FOUR_MODE_41: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //      DragView centerDragView3;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //        centerDragView3 = dragViews.get(3);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //        ImageItem imageEntity3 = mItems.get(3);
  //        centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(RIGHT_OF, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //
  //      centerDragView3.setId(R.id.id_3);
  //      centerDragView3.setCallback(this);
  //      RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
  //      params3.addRule(BELOW, R.id.id_1);
  //      params3.addRule(RIGHT_OF, R.id.id_2);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //      this.dragControllerLayout.addView(centerDragView3, params3);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //        this.dragViews.add(centerDragView3);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.FIVE_MODE_51: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //      DragView centerDragView3;
  //      DragView centerDragView4;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //        centerDragView3 = dragViews.get(3);
  //        centerDragView4 = dragViews.get(4);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //        ImageItem imageEntity3 = mItems.get(3);
  //        centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3);
  //        ImageItem imageEntity4 = mItems.get(4);
  //        centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(largeParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(BELOW, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //      params2.addRule(RIGHT_OF, R.id.id_1);
  //
  //      centerDragView3.setId(R.id.id_3);
  //      centerDragView3.setCallback(this);
  //      RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
  //      params3.addRule(BELOW, R.id.id_1);
  //
  //      centerDragView4.setId(R.id.id_4);
  //      centerDragView4.setCallback(this);
  //      RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
  //      params4.addRule(BELOW, R.id.id_2);
  //      params4.addRule(RIGHT_OF, R.id.id_3);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //      this.dragControllerLayout.addView(centerDragView3, params3);
  //      this.dragControllerLayout.addView(centerDragView4, params4);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //        this.dragViews.add(centerDragView3);
  //        this.dragViews.add(centerDragView4);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.FIVE_MODE_52: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //      DragView centerDragView3;
  //      DragView centerDragView4;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //        centerDragView3 = dragViews.get(3);
  //        centerDragView4 = dragViews.get(4);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //        ImageItem imageEntity3 = mItems.get(3);
  //        centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3);
  //        ImageItem imageEntity4 = mItems.get(4);
  //        centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(RIGHT_OF, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //
  //      centerDragView3.setId(R.id.id_3);
  //      centerDragView3.setCallback(this);
  //      RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
  //      params3.addRule(BELOW, R.id.id_1);
  //      params3.addRule(RIGHT_OF, R.id.id_2);
  //
  //      centerDragView4.setId(R.id.id_4);
  //      centerDragView4.setCallback(this);
  //      RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(largeParams);
  //      params4.addRule(BELOW, R.id.id_2);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //      this.dragControllerLayout.addView(centerDragView3, params3);
  //      this.dragControllerLayout.addView(centerDragView4, params4);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //        this.dragViews.add(centerDragView3);
  //        this.dragViews.add(centerDragView4);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.FIVE_MODE_53: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //      ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];
  //
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //      DragView centerDragView3;
  //      DragView centerDragView4;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //        centerDragView3 = dragViews.get(3);
  //        centerDragView4 = dragViews.get(4);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //        ImageItem imageEntity3 = mItems.get(3);
  //        centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3);
  //        ImageItem imageEntity4 = mItems.get(4);
  //        centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(RIGHT_OF, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(largeParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //
  //      centerDragView3.setId(R.id.id_3);
  //      centerDragView3.setCallback(this);
  //      RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
  //      params3.addRule(BELOW, R.id.id_2);
  //
  //      centerDragView4.setId(R.id.id_4);
  //      centerDragView4.setCallback(this);
  //      RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
  //      params4.addRule(BELOW, R.id.id_2);
  //      params4.addRule(RIGHT_OF, R.id.id_3);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //      this.dragControllerLayout.addView(centerDragView3, params3);
  //      this.dragControllerLayout.addView(centerDragView4, params4);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //        this.dragViews.add(centerDragView3);
  //        this.dragViews.add(centerDragView4);
  //      }
  //
  //      break;
  //    }
  //
  //    case TemplateGroup.ID.SIX_MODE_61: {
  //
  //      ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
  //      DragView centerDragView0;
  //      DragView centerDragView1;
  //      DragView centerDragView2;
  //      DragView centerDragView3;
  //      DragView centerDragView4;
  //      DragView centerDragView5;
  //
  //      if (!hasRemoved) {
  //        centerDragView0 = dragViews.get(0);
  //        centerDragView1 = dragViews.get(1);
  //        centerDragView2 = dragViews.get(2);
  //        centerDragView3 = dragViews.get(3);
  //        centerDragView4 = dragViews.get(4);
  //        centerDragView5 = dragViews.get(5);
  //      } else {
  //        ImageItem imageEntity0 = mItems.get(0);
  //        centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0);
  //        ImageItem imageEntity1 = mItems.get(1);
  //        centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1);
  //        ImageItem imageEntity2 = mItems.get(2);
  //        centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2);
  //        ImageItem imageEntity3 = mItems.get(3);
  //        centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3);
  //        ImageItem imageEntity4 = mItems.get(4);
  //        centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4);
  //        ImageItem imageEntity5 = mItems.get(5);
  //        centerDragView5 = new DragView(PuzzlePlayingActivity.this, imageEntity5);
  //      }
  //
  //      centerDragView0.setId(R.id.id_0);
  //      centerDragView0.setCallback(this);
  //      RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);
  //
  //      centerDragView1.setId(R.id.id_1);
  //      centerDragView1.setCallback(this);
  //      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
  //      params1.addRule(RIGHT_OF, R.id.id_0);
  //
  //      centerDragView2.setId(R.id.id_2);
  //      centerDragView2.setCallback(this);
  //      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
  //      params2.addRule(BELOW, R.id.id_0);
  //
  //      centerDragView3.setId(R.id.id_3);
  //      centerDragView3.setCallback(this);
  //      RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
  //      params3.addRule(BELOW, R.id.id_1);
  //      params3.addRule(RIGHT_OF, R.id.id_2);
  //
  //      centerDragView4.setId(R.id.id_4);
  //      centerDragView4.setCallback(this);
  //      RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
  //      params4.addRule(BELOW, R.id.id_2);
  //
  //      centerDragView5.setId(R.id.id_5);
  //      centerDragView5.setCallback(this);
  //      RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(normalParams);
  //      params5.addRule(BELOW, R.id.id_3);
  //      params5.addRule(RIGHT_OF, R.id.id_4);
  //
  //      this.dragControllerLayout.addView(centerDragView0, params0);
  //      this.dragControllerLayout.addView(centerDragView1, params1);
  //      this.dragControllerLayout.addView(centerDragView2, params2);
  //      this.dragControllerLayout.addView(centerDragView3, params3);
  //      this.dragControllerLayout.addView(centerDragView4, params4);
  //      this.dragControllerLayout.addView(centerDragView5, params5);
  //
  //      if (hasRemoved) {
  //        this.dragViews.add(centerDragView0);
  //        this.dragViews.add(centerDragView1);
  //        this.dragViews.add(centerDragView2);
  //        this.dragViews.add(centerDragView3);
  //        this.dragViews.add(centerDragView4);
  //        this.dragViews.add(centerDragView5);
  //      }
  //
  //      break;
  //    }
  //  }
  //
  //  /*add to dragLayout*/
  //  this.dragControllerLayout.setDragViews(dragViews);
  //  this.dragControllerLayout.setBehavior(currentBehavior);
  //}

  private void initDragViews(boolean hasRemoved) {

    this.dragViews.clear();
    this.dragControllerLayout.clear();
    int id = mCurrentTemp.ID;

    switch (id) {
      case TemplateGroup.ID.ONE_MODE_11: {

        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView = new DragView(PuzzlePlayingActivity.this, imageEntity0, NORMAL);
        centerDragView.setId(R.id.id_0);
        centerDragView.setCallback(this);
        /*只有一张拼图是，不可编辑，其他模式默认可编辑*/
        centerDragView.setEditable(false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(largeParams);
        params.addRule(CENTER_IN_PARENT);

        this.dragControllerLayout.addView(centerDragView, params);

        this.dragViews.add(centerDragView);

        break;
      }

      case TemplateGroup.ID.TWO_MODE_21: {

        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, LARGE);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(largeParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, LARGE);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(largeParams);
        params1.addRule(BELOW, R.id.id_0);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);

        break;
      }

      case TemplateGroup.ID.THREE_MODE_31: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, LARGE);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(largeParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(BELOW, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, NORMAL);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);
        params2.addRule(RIGHT_OF, R.id.id_1);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);

        break;
      }

      case TemplateGroup.ID.THREE_MODE_32: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, NORMAL);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, LARGE);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(largeParams);
        params2.addRule(BELOW, R.id.id_0);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);

        break;
      }

      case TemplateGroup.ID.FOUR_MODE_41: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, NORMAL);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, NORMAL);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageItem imageEntity3 = mItems.get(3);
        DragView centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3, NORMAL);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);
        params3.addRule(RIGHT_OF, R.id.id_2);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);
        this.dragControllerLayout.addView(centerDragView3, params3);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);
        this.dragViews.add(centerDragView3);

        break;
      }

      case TemplateGroup.ID.FIVE_MODE_51: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, LARGE);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(largeParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(BELOW, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, NORMAL);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);
        params2.addRule(RIGHT_OF, R.id.id_1);

        ImageItem imageEntity3 = mItems.get(3);
        DragView centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3, NORMAL);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);

        ImageItem imageEntity4 = mItems.get(4);
        DragView centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4, NORMAL);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
        params4.addRule(BELOW, R.id.id_2);
        params4.addRule(RIGHT_OF, R.id.id_3);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);
        this.dragControllerLayout.addView(centerDragView3, params3);
        this.dragControllerLayout.addView(centerDragView4, params4);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);
        this.dragViews.add(centerDragView3);
        this.dragViews.add(centerDragView4);

        break;
      }

      case TemplateGroup.ID.FIVE_MODE_52: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, NORMAL);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, NORMAL);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageItem imageEntity3 = mItems.get(3);
        DragView centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3, NORMAL);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);
        params3.addRule(RIGHT_OF, R.id.id_2);

        ImageItem imageEntity4 = mItems.get(4);
        DragView centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4, LARGE);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(largeParams);
        params4.addRule(BELOW, R.id.id_2);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);
        this.dragControllerLayout.addView(centerDragView3, params3);
        this.dragControllerLayout.addView(centerDragView4, params4);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);
        this.dragViews.add(centerDragView3);
        this.dragViews.add(centerDragView4);

        break;
      }

      case TemplateGroup.ID.FIVE_MODE_53: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];
        ViewGroup.MarginLayoutParams largeParams = LAYOUT_PARAMS[1];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, NORMAL);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, LARGE);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(largeParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageItem imageEntity3 = mItems.get(3);
        DragView centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3, NORMAL);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_2);

        ImageItem imageEntity4 = mItems.get(4);
        DragView centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4, NORMAL);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
        params4.addRule(BELOW, R.id.id_2);
        params4.addRule(RIGHT_OF, R.id.id_3);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);
        this.dragControllerLayout.addView(centerDragView3, params3);
        this.dragControllerLayout.addView(centerDragView4, params4);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);
        this.dragViews.add(centerDragView3);
        this.dragViews.add(centerDragView4);

        break;
      }

      case TemplateGroup.ID.SIX_MODE_61: {

        ViewGroup.MarginLayoutParams normalParams = LAYOUT_PARAMS[0];

        ImageItem imageEntity0 = mItems.get(0);
        DragView centerDragView0 = new DragView(PuzzlePlayingActivity.this, imageEntity0, NORMAL);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageItem imageEntity1 = mItems.get(1);
        DragView centerDragView1 = new DragView(PuzzlePlayingActivity.this, imageEntity1, NORMAL);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageItem imageEntity2 = mItems.get(2);
        DragView centerDragView2 = new DragView(PuzzlePlayingActivity.this, imageEntity2, NORMAL);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageItem imageEntity3 = mItems.get(3);
        DragView centerDragView3 = new DragView(PuzzlePlayingActivity.this, imageEntity3, NORMAL);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);
        params3.addRule(RIGHT_OF, R.id.id_2);

        ImageItem imageEntity4 = mItems.get(4);
        DragView centerDragView4 = new DragView(PuzzlePlayingActivity.this, imageEntity4, NORMAL);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
        params4.addRule(BELOW, R.id.id_2);

        ImageItem imageEntity5 = mItems.get(5);
        DragView centerDragView5 = new DragView(PuzzlePlayingActivity.this, imageEntity5, NORMAL);
        centerDragView5.setId(R.id.id_5);
        centerDragView5.setCallback(this);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(normalParams);
        params5.addRule(BELOW, R.id.id_3);
        params5.addRule(RIGHT_OF, R.id.id_4);

        this.dragControllerLayout.addView(centerDragView0, params0);
        this.dragControllerLayout.addView(centerDragView1, params1);
        this.dragControllerLayout.addView(centerDragView2, params2);
        this.dragControllerLayout.addView(centerDragView3, params3);
        this.dragControllerLayout.addView(centerDragView4, params4);
        this.dragControllerLayout.addView(centerDragView5, params5);

        this.dragViews.add(centerDragView0);
        this.dragViews.add(centerDragView1);
        this.dragViews.add(centerDragView2);
        this.dragViews.add(centerDragView3);
        this.dragViews.add(centerDragView4);
        this.dragViews.add(centerDragView5);

        break;
      }
    }

    /*add to dragLayout*/
    this.dragControllerLayout.setDragViews(dragViews);
    this.dragControllerLayout.setBehavior(currentBehavior);

    ViewGroup.LayoutParams layoutParams = this.dragControllerLayout.getLayoutParams();
  }

  @Override public void onTemplateClick(int position, TemplateGroup.Temp temp) {

    if (mCurrentTemp == temp) return;

    mCurrentTemp.isSelected = false;
    temp.isSelected = true;
    this.mCurrentTemp = temp;

    mPuzzlePlayingTempAdapter.notifyDataSetChanged();
    this.initDragViews(false);
  }

  @Override public void onLongPressed() {
    this.dragControllerLayout.setBehavior(currentBehavior = DragControllerLayout.Behavior.EDITOR);
  }

  @Override public void onCancelClick(DragView dragView, ImageItem imageEntity) {
    if (mCurrentTemp.ID == TemplateGroup.ID.ONE_MODE_11) {
      Toast.makeText(this, "拼图功能至少需要1张图片，无法删除！", Toast.LENGTH_LONG).show();
    } else {
      mItems.remove(imageEntity);
      dragViews.remove(dragView);

      this.mTempEntities.clear();
      /*初始化模板*/
      initTemplate();
      //初始化模板对应的拼图
      initDragViews(true);
    }
  }

  private class CalculateRunnable implements Runnable {
    @Override public void run() {
      expandHitArea();

      initTipContainerAnim();

      calculateDragViewSize();
    }
  }

  /**
   * 提示条中的"取消"按钮，可点击区域较小，从自身向左右各扩展200px的区域且属于父View，均代理给该按钮
   */
  private void expandHitArea() {
    Rect delegateArea = new Rect();
    ImageButton dismissButton = (ImageButton) findViewById(R.id.puzzle_tip_cancel_btn);
    dismissButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mTipContainerAnimator.start();
      }
    });

    dismissButton.getHitRect(delegateArea);
    delegateArea.left += 200;
    delegateArea.right += 200;

    TouchDelegate touchDelegate = new TouchDelegate(delegateArea, dismissButton);
    ((ViewGroup) dismissButton.getParent()).setTouchDelegate(touchDelegate);
  }

  private void initTipContainerAnim() {
    mTipContainerAnimator =
        ObjectAnimator.ofFloat(mTipContainer, View.TRANSLATION_Y, mTipContainer.getHeight());
    mTipContainerAnimator.setInterpolator(new LinearInterpolator());
    mTipContainerAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        mTipContainer.setVisibility(View.GONE);
        mTipContainer.setWillNotDraw(true);
      }
    });
  }

  private void calculateDragViewSize() {
    ViewGroup parent = (ViewGroup) dragControllerLayout.getParent();
    dragViewsContainerHeight =
        parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
    dragViewsContainerWidth =
        parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();

    //if (!validatedNormalSize() || !validatedLargeSize() || !validatedParams()) {

    int paddingTop = dragControllerLayout.getPaddingTop();
    int paddingBottom = dragControllerLayout.getPaddingBottom();
    int paddingLeft = dragControllerLayout.getPaddingLeft();
    int paddingRight = dragControllerLayout.getPaddingRight();

    int horizontalGap = paddingLeft + paddingRight;
    int verticalGap = paddingTop + paddingBottom;

    //int largeWidth = dragViewsContainerWidth - horizontalGap - puzzleMargin * 2;
    int largeHeight = (dragViewsContainerHeight - verticalGap - puzzleMargin * 4) / 2;
    int largeWidth = largeHeight * 4 / 3;

    //int normalWidth = (largeWidth - puzzleMargin * 2) / 2;
    int normalHeight = (largeHeight - puzzleMargin * 2) / 2;
    int normalWidth = normalHeight * 4 / 3;

    ViewGroup.MarginLayoutParams normalParams =
        new ViewGroup.MarginLayoutParams(normalWidth, normalHeight);
    ViewGroup.MarginLayoutParams largeParams =
        new ViewGroup.MarginLayoutParams(largeWidth, largeHeight);

    LAYOUT_PARAMS[0] = normalParams;
    LAYOUT_PARAMS[1] = largeParams;

    switch (mCurrentTemp.ID) {

      case TemplateGroup.ID.ONE_MODE_11: {

        break;
      }

      case TemplateGroup.ID.TWO_MODE_21: {

        break;
      }

      case TemplateGroup.ID.THREE_MODE_31: {

        break;
      }

      case TemplateGroup.ID.THREE_MODE_32: {

        break;
      }
    }

    NORMAL[0] = normalWidth;
    NORMAL[1] = normalHeight;
    LARGE[0] = largeWidth;
    LARGE[1] = largeHeight;

    normalParams.setMargins(puzzleMargin, puzzleMargin, puzzleMargin, puzzleMargin);
    largeParams.setMargins(puzzleMargin, puzzleMargin, puzzleMargin, puzzleMargin);

    //}

    /*排列图片*/
    PuzzlePlayingActivity.this.initDragViews(true);
  }

  private class DragController implements DragControllerLayout.DragController {

    private DragView capturedView;
    private DragView targetView;

    private int lastX;
    private int lastY;

    private boolean hasReleased;

    @Override public void onViewCaptured(DragView capturedChild) {
      this.hasReleased = false;
      this.capturedView = capturedChild;
    }

    @Override public boolean onViewReleased(DragView releasedChild) {

      this.hasReleased = true;

      /*没有移动到任何一块拼图中*/
      if (targetView == null) return false;

      /*当前拖拽拼图并没有超出自己的范围，则复原其位置，并且check again*/
      if (targetView == releasedChild || !targetView.getStartBound().contains(lastX, lastY)) {
        return false;
      }

        /*当前拖拽拼图与指定位置的拼图进行调换，并且check again*/
      if (targetView != releasedChild && targetView.getStartBound().contains(lastX, lastY)) {
        PuzzlePlayingActivity.this.swapViewWithAnimation(capturedView, targetView);
        return true;
      }

      return false;
    }

    @Override public void onViewPositionChanged(int x, int y) {

      if (hasReleased) return;

      this.lastX = x;
      this.lastY = y;

      /*永远不要为当前拖拽拼图更新背景*/
      for (DragView target : dragViews) {
        if (capturedView == target) continue;
        if (target.getStartBound().contains(x, y)) {
          target.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
          if (targetView != target) this.targetView = target;
        } else {
          target.setBackgroundColor(0);
        }
      }
    }

    @Override public void onViewDragStateChanged(int state) {
      if (state == STATE_IDLE) {
          /*清除状态*/
        capturedView = targetView = null;
        lastX = lastY = 0;
      }
    }
  }
}
