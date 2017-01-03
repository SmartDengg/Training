package com.smartdengg.dragview.puzzle;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.ImageEntity;
import com.smartdengg.dragview.entity.TempEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.RIGHT_OF;

/**
 * 创建时间:  2016/12/28 12:31 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class PuzzleActivity extends Activity implements TempAdapter.Callback, DragView.Callback {

  public static final String TAG = PuzzleActivity.class.getSimpleName();
  public static final String ITEMS = "ITEMS";

  private RelativeLayout dragViewContainer;
  private RecyclerView tempRecyclerView;
  private ViewGroup tipContainer;
  private List<ImageEntity> items;

  private TempAdapter tempAdapter;
  private ObjectAnimator tipContainerAnimator;

  private TemplateGroup templateGroup;
  private List<TempEntity> tempEntities;
  private TempEntity currentTempEntity;

  private static SparseIntArray childSize = new SparseIntArray(2);
  private static SparseIntArray parentSize = new SparseIntArray(2);
  private static SparseArray<RelativeLayout.LayoutParams> paramsSparseArray = new SparseArray<>(2);

  public static void start(Context context, List<ImageEntity> items) {
    Intent intent = new Intent(context, PuzzleActivity.class);
    intent.putExtra(ITEMS, new ArrayList<>(items));
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.puzzle_layout);

    if (savedInstanceState == null) {  //当ACT被系统销毁时，并重建后，必须保持之前的选中顺序不变
      Intent intent = getIntent();
      //noinspection unchecked
      items = (List<ImageEntity>) intent.getSerializableExtra(ITEMS);
    } else {
      //noinspection unchecked
      this.items = (List<ImageEntity>) savedInstanceState.getSerializable(ITEMS);
    }

    this.initView();
    this.initTemplate();
  }

  private void initView() {

    ViewGroup rootView = (ViewGroup) findViewById(R.id.puzzle_root);

    this.dragViewContainer = (RelativeLayout) findViewById(R.id.puzzle_drag_container);
    this.tipContainer = (ViewGroup) findViewById(R.id.tip_container);
    this.tempRecyclerView = (RecyclerView) findViewById(R.id.puzzle_template_recyclerview);
    ViewGroup viewGroup = (ViewGroup) tempRecyclerView.getParent();
    viewGroup.bringChildToFront(tempRecyclerView);

    rootView.post(new Runnable() {
      @Override public void run() {
        // The bounds for the delegate view (an ImageButton
        // in this example)
        Rect delegateArea = new Rect();
        ImageButton dismissButton = (ImageButton) findViewById(R.id.tip_cancel_btn);
        dismissButton.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            tipContainerAnimator.start();
          }
        });

        // The hit rectangle for the ImageButton
        dismissButton.getHitRect(delegateArea);

        // Extend the touch area of the ImageButton beyond its bounds
        // on the right and bottom.
        delegateArea.left += 200;
        delegateArea.right += 200;
        delegateArea.top += 200;
        delegateArea.bottom += 200;

        // Instantiate a TouchDelegate.
        // "delegateArea" is the bounds in local coordinates of
        // the containing view to be mapped to the delegate view.
        // "myButton" is the child view that should receive motion
        // events.
        TouchDelegate touchDelegate = new TouchDelegate(delegateArea, dismissButton);

        // Sets the TouchDelegate on the parent view, such that touches
        // within the touch delegate bounds are routed to the child.
        if (ViewGroup.class.isInstance(dismissButton.getParent())) {
          ((ViewGroup) dismissButton.getParent()).setTouchDelegate(touchDelegate);
        }
      }
    });

    ViewTreeObserver tipContainerViewTreeObserver = this.tipContainer.getViewTreeObserver();
    if (tipContainerViewTreeObserver.isAlive()) {
      tipContainerViewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override public boolean onPreDraw() {
          tipContainer.getViewTreeObserver().removeOnPreDrawListener(this);
          tipContainerAnimator =
              ObjectAnimator.ofFloat(tipContainer, View.TRANSLATION_Y, tipContainer.getHeight());
          tipContainerAnimator.setInterpolator(new LinearInterpolator());
          return true;
        }
      });
    }

    ViewTreeObserver dragViewContainerViewTreeObserver =
        this.dragViewContainer.getViewTreeObserver();
    if (dragViewContainerViewTreeObserver.isAlive()) {
      dragViewContainerViewTreeObserver.addOnGlobalLayoutListener(
          new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {

              int height = dragViewContainer.getHeight();
              int width = dragViewContainer.getWidth();

              Log.d(TAG, "height = " + height);
              Log.d(TAG, "width = " + width);

              if (height == 0 || width == 0) return;

              dragViewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

              if (parentSize.size() != 2) {
                parentSize.append(0, width);
                parentSize.append(0, height);
              }

              if (paramsSparseArray.size() != 2 || childSize.size() != 2) {

                int paddingTop = dragViewContainer.getPaddingTop();
                int paddingBottom = dragViewContainer.getPaddingBottom();
                int paddingLeft = dragViewContainer.getPaddingLeft();
                int paddingRight = dragViewContainer.getPaddingRight();
                int margin = getResources().getDimensionPixelSize(R.dimen.material_4dp);

                int horizontalGap = paddingLeft + paddingRight;
                int verticalGap = paddingTop + paddingBottom;

                int childWidth = (width - horizontalGap - margin * 2) / 2;
                int childHeight = (height - verticalGap - margin * 3) / 4;

                childSize.append(0, childWidth);
                childSize.append(0, childHeight);

                Log.d(TAG, "paddingTop = " + paddingTop);
                Log.d(TAG, "paddingBottom = " + paddingBottom);
                Log.d(TAG, "paddingLeft = " + paddingBottom);
                Log.d(TAG, "paddingRight = " + paddingBottom);
                Log.d(TAG, "horizontalGap = " + horizontalGap);
                Log.d(TAG, "verticalGap = " + verticalGap);
                Log.d(TAG, "childHeight = " + childHeight);
                Log.d(TAG, "childWidth = " + childWidth);
                Log.d(TAG, "margin = " + margin);

                RelativeLayout.LayoutParams headerParams =
                    new RelativeLayout.LayoutParams((childWidth + margin) * 2,
                        (childHeight + margin) * 2);
                headerParams.setMargins(margin, margin, margin, margin);

                RelativeLayout.LayoutParams normalParams =
                    new RelativeLayout.LayoutParams(childWidth, childHeight);
                normalParams.setMargins(margin, margin, margin, margin);

                paramsSparseArray.append(0, headerParams);
                paramsSparseArray.append(1, normalParams);
              }

              /*排列图片*/
              PuzzleActivity.this.initDragView();
            }
          });
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PuzzleActivity.this);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    this.tempRecyclerView.setLayoutManager(linearLayoutManager);
  }

  private void initTemplate() {

    tempEntities = switchTempGroupMode();

    /*只有一张图片时，隐藏提示条*/
    if (tempEntities.size() == 1 && tempEntities.get(0).ID == TemplateGroup.Subset.ONE_MODE_11) {
      this.tipContainer.setVisibility(View.GONE);
    }

    this.currentTempEntity = tempEntities.get(0);
    this.tempAdapter = new TempAdapter(PuzzleActivity.this, tempEntities);
    this.tempAdapter.setCallback(this);
    this.tempRecyclerView.setAdapter(tempAdapter);
  }

  @NonNull private List<TempEntity> switchTempGroupMode() {

    templateGroup = TemplateGroup.correspondTemplate(items);
    Log.d(TAG, "switchTempGroupMode symbol = " + templateGroup.symbol);
    Log.d(TAG, "switchTempGroupMode ids = " + Arrays.toString(templateGroup.ids));

    return Collections.unmodifiableList(templateGroup.apply());
  }

  private void initDragView() {

    this.dragViewContainer.removeAllViews();
    int id = currentTempEntity.ID;

    switch (id) {
      case TemplateGroup.Subset.ONE_MODE_11: {

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView.setId(R.id.id_0);
        centerDragView.setCallback(this);

        RelativeLayout.LayoutParams source = paramsSparseArray.get(0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(source);
        params.addRule(CENTER_IN_PARENT);
        this.dragViewContainer.addView(centerDragView, params);

        break;
      }

      case TemplateGroup.Subset.TWO_MODE_21: {

        RelativeLayout.LayoutParams source = paramsSparseArray.get(0);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(source);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(source);
        params1.addRule(BELOW, R.id.id_0);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);

        break;
      }

      case TemplateGroup.Subset.THREE_MODE_31: {

        RelativeLayout.LayoutParams headerParams = paramsSparseArray.get(0);
        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(headerParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(BELOW, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);
        params2.addRule(RIGHT_OF, R.id.id_1);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);

        break;
      }

      case TemplateGroup.Subset.THREE_MODE_32: {

        RelativeLayout.LayoutParams headerParams = paramsSparseArray.get(0);
        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(headerParams);
        params2.addRule(BELOW, R.id.id_0);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);

        break;
      }

      case TemplateGroup.Subset.FOUR_MODE_41: {

        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageEntity imageEntity3 = items.get(3);
        DragView centerDragView3 = new DragView(PuzzleActivity.this, imageEntity3);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);
        params3.addRule(RIGHT_OF, R.id.id_2);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);
        this.dragViewContainer.addView(centerDragView3, params3);

        break;
      }

      case TemplateGroup.Subset.FIVE_MODE_51: {

        RelativeLayout.LayoutParams headerParams = paramsSparseArray.get(0);
        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(headerParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(BELOW, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);
        params2.addRule(RIGHT_OF, R.id.id_1);

        ImageEntity imageEntity3 = items.get(3);
        DragView centerDragView3 = new DragView(PuzzleActivity.this, imageEntity3);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);

        ImageEntity imageEntity4 = items.get(4);
        DragView centerDragView4 = new DragView(PuzzleActivity.this, imageEntity4);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
        params4.addRule(BELOW, R.id.id_2);
        params4.addRule(RIGHT_OF, R.id.id_3);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);
        this.dragViewContainer.addView(centerDragView3, params3);
        this.dragViewContainer.addView(centerDragView4, params4);

        break;
      }

      case TemplateGroup.Subset.FIVE_MODE_52: {

        RelativeLayout.LayoutParams headerParams = paramsSparseArray.get(0);
        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageEntity imageEntity3 = items.get(3);
        DragView centerDragView3 = new DragView(PuzzleActivity.this, imageEntity3);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);
        params3.addRule(RIGHT_OF, R.id.id_2);

        ImageEntity imageEntity4 = items.get(4);
        DragView centerDragView4 = new DragView(PuzzleActivity.this, imageEntity4);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(headerParams);
        params4.addRule(BELOW, R.id.id_2);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);
        this.dragViewContainer.addView(centerDragView3, params3);
        this.dragViewContainer.addView(centerDragView4, params4);

        break;
      }

      case TemplateGroup.Subset.FIVE_MODE_53: {

        RelativeLayout.LayoutParams headerParams = paramsSparseArray.get(0);
        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(headerParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageEntity imageEntity3 = items.get(3);
        DragView centerDragView3 = new DragView(PuzzleActivity.this, imageEntity3);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_2);

        ImageEntity imageEntity4 = items.get(4);
        DragView centerDragView4 = new DragView(PuzzleActivity.this, imageEntity4);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
        params4.addRule(BELOW, R.id.id_2);
        params4.addRule(RIGHT_OF, R.id.id_3);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);
        this.dragViewContainer.addView(centerDragView3, params3);
        this.dragViewContainer.addView(centerDragView4, params4);

        break;
      }

      case TemplateGroup.Subset.SIX_MODE_61: {

        RelativeLayout.LayoutParams normalParams = paramsSparseArray.get(1);

        ImageEntity imageEntity0 = items.get(0);
        DragView centerDragView0 = new DragView(PuzzleActivity.this, imageEntity0);
        centerDragView0.setId(R.id.id_0);
        centerDragView0.setCallback(this);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(normalParams);

        ImageEntity imageEntity1 = items.get(1);
        DragView centerDragView1 = new DragView(PuzzleActivity.this, imageEntity1);
        centerDragView1.setId(R.id.id_1);
        centerDragView1.setCallback(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(normalParams);
        params1.addRule(RIGHT_OF, R.id.id_0);

        ImageEntity imageEntity2 = items.get(2);
        DragView centerDragView2 = new DragView(PuzzleActivity.this, imageEntity2);
        centerDragView2.setId(R.id.id_2);
        centerDragView2.setCallback(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(normalParams);
        params2.addRule(BELOW, R.id.id_0);

        ImageEntity imageEntity3 = items.get(3);
        DragView centerDragView3 = new DragView(PuzzleActivity.this, imageEntity3);
        centerDragView3.setId(R.id.id_3);
        centerDragView3.setCallback(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(normalParams);
        params3.addRule(BELOW, R.id.id_1);
        params3.addRule(RIGHT_OF, R.id.id_2);

        ImageEntity imageEntity4 = items.get(4);
        DragView centerDragView4 = new DragView(PuzzleActivity.this, imageEntity4);
        centerDragView4.setId(R.id.id_4);
        centerDragView4.setCallback(this);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(normalParams);
        params4.addRule(BELOW, R.id.id_2);

        ImageEntity imageEntity5 = items.get(5);
        DragView centerDragView5 = new DragView(PuzzleActivity.this, imageEntity5);
        centerDragView5.setId(R.id.id_5);
        centerDragView5.setCallback(this);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(normalParams);
        params5.addRule(BELOW, R.id.id_3);
        params5.addRule(RIGHT_OF, R.id.id_4);

        this.dragViewContainer.addView(centerDragView0, params0);
        this.dragViewContainer.addView(centerDragView1, params1);
        this.dragViewContainer.addView(centerDragView2, params2);
        this.dragViewContainer.addView(centerDragView3, params3);
        this.dragViewContainer.addView(centerDragView4, params4);
        this.dragViewContainer.addView(centerDragView5, params5);

        break;
      }
    }
  }

  @Override public void onTemplateClick(int position, TempEntity tempEntity) {

    if (currentTempEntity == tempEntity) return;

    currentTempEntity.isSelected = false;
    tempEntity.isSelected = true;
    this.currentTempEntity = tempEntity;

    tempAdapter.notifyDataSetChanged();
    this.initDragView();
  }

  @Override public void onCancelClick(DragView dragView, ImageEntity imageEntity) {
    Log.d(TAG, "onCancelClick = " + imageEntity.toString());
  }

  private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

  public static int generateViewId() {

    if (Build.VERSION.SDK_INT < 17) {
      for (; ; ) {
        final int result = sNextGeneratedId.get();
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
        if (sNextGeneratedId.compareAndSet(result, newValue)) {
          return result;
        }
      }
    } else {
      return View.generateViewId();
    }
  }
}
