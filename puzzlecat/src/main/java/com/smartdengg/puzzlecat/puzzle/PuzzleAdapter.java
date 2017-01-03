package com.smartdengg.puzzlecat.puzzle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.smartdengg.puzzlecat.CatEntity;
import com.smartdengg.puzzlecat.R;
import java.util.Collections;
import java.util.List;

/**
 * 创建时间:  2016/12/19 16:34 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.ViewHolder>
    implements ItemDragHelperCallback.AdapterCallback {

  private static final String TAG = PuzzleAdapter.class.getSimpleName();

  private Context context;
  private List<CatEntity> items;
  private Callback callback;
  private Drawable mBackgroundDrawable;

  private int startPosition = -1;
  private int endPosition = -1;

  public PuzzleAdapter(Context context, List<CatEntity> items) {
    this.context = context;
    this.items = items;
    this.mBackgroundDrawable =
        context.getResources().getDrawable(R.drawable.item_seleced_background);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(this,
        LayoutInflater.from(context).inflate(R.layout.puzzle_image_item, parent, false));
  }

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {

    final int adapterPosition = holder.getAdapterPosition();
    final ImageView imageView = (ImageView) holder.itemView;
    CatEntity catEntity = items.get(adapterPosition);
    imageView.setImageResource(catEntity.drawableID);
    imageView.setBackgroundColor(catEntity.isResume ? Color.RED : Color.WHITE);

    if (catEntity.isResume) imageView.setBackground(mBackgroundDrawable);

    final GestureDetector gestureDetector =
        new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

          @Override public void onLongPress(MotionEvent e) {
            if (callback != null) callback.onItemLongPressed();
          }

          @Override public void onShowPress(MotionEvent e) {
          }

          @Override public boolean onDown(MotionEvent e) {
            if (callback != null) callback.startDrag(holder);
            return false;
          }
        });

    imageView.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        //Log.d(TAG, "event = " + event);

        return true;
      }
    });
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  public boolean isHeader(int position) {
    return position == 0;
  }

  public boolean isSingular() {
    return items.size() % 2 == 1;
  }

  @Override public boolean onItemMove(final int fromPosition, final int toPosition) {

    /*CatEntity remove = items.remove(toPosition);
    items.add(fromPosition, remove);
    notifyItemMoved(toPosition, fromPosition);
    notifyItemMoved(fromPosition, toPosition);

    notifyItemChanged(fromPosition);
    notifyItemChanged(toPosition);*/

   /* if (toPosition > fromPosition) {
      for (int i = toPosition; i > fromPosition; i--) {
        Collections.swap(items, i, i - 1);
        notifyItemMoved(i, i - 1);
      }
    } else {
      for (int i = toPosition; i < fromPosition; i++) {
        Collections.swap(items, i, i + 1);
        notifyItemMoved(i, i + 1);
      }
    }*/

    Log.d(TAG, "startPosition = " + fromPosition);
    Log.d(TAG, "endPosition = " + toPosition);

    /*初次拖拽的item*/
    if (this.startPosition == -1 && this.startPosition != fromPosition) {
      this.startPosition = fromPosition;
    }

    if (this.endPosition != toPosition) {

      int prevEndPosition = endPosition;
      if (prevEndPosition != -1) {
        items.get(prevEndPosition).isResume = false;
        notifyItemChanged(prevEndPosition);
      }

      items.get(toPosition).isResume = true;
      notifyItemChanged(toPosition);

      this.endPosition = toPosition;
    }

    return true;
  }

  public void swapItem() {
    if (startPosition == -1 || endPosition == -1) return;

    Collections.swap(items, startPosition, endPosition);
    //notifyItemMoved(startPosition, endPosition);
    items.get(startPosition).isResume = false;
    //items.get(endPosition).isResume = false;
    notifyItemChanged(startPosition);
    notifyItemChanged(endPosition);

    /*reset the position*/
    this.startPosition = this.endPosition = -1;
  }

  public void setCallback(final Callback callback) {
    this.callback = callback;
  }

  static class ViewHolder extends RecyclerView.ViewHolder
      implements ItemDragHelperCallback.ViewHolderCallback {

    private RecyclerView.Adapter mAdapter;

    ViewHolder(RecyclerView.Adapter adapter, View itemView) {
      super(itemView);
      this.mAdapter = adapter;
    }

    @Override public void onItemDragged() {
      //applyAnimation(itemView);
    }

    @Override public void onItemIdle() {
      //resetAnimation(itemView);
      if (mAdapter instanceof PuzzleAdapter) {
        //itemView.setVisibility(View.GONE);
        ((PuzzleAdapter) mAdapter).swapItem();
      }
    }

    private static void applyAnimation(View view) {
      ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.1f);
      ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.1f);
      ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.9f);
      AnimatorSet animSet = new AnimatorSet();
      animSet.setDuration(300);
      animSet.setInterpolator(new LinearInterpolator());
      animSet.playTogether(scaleX, scaleY, alpha);
      animSet.start();
    }

    private static void resetAnimation(View view) {
      ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f);
      ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f);
      ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f);
      AnimatorSet animSet = new AnimatorSet();
      animSet.setDuration(300);
      animSet.setInterpolator(new LinearInterpolator());
      animSet.playTogether(scaleX, scaleY, alpha);
      animSet.start();
    }
  }

  public interface Callback {

    void onItemLongPressed();

    void startDrag(RecyclerView.ViewHolder viewHolder);
  }
}
