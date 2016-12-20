package com.smartdengg.puzzlecat.puzzle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 创建时间:  2016/12/19 18:07 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

  private static final String TAG = ItemDragHelperCallback.class.getSimpleName();

  private final AdapterCallback mDragHelperAdapter;

  public ItemDragHelperCallback(AdapterCallback dragHelperAdapter) {
    this.mDragHelperAdapter = dragHelperAdapter;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

    Log.d(TAG, "getMovementFlags");

    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    int dragFlags = 0;
    int swipeFlags = 0;

    if (layoutManager instanceof GridLayoutManager) {//GridLayout
      dragFlags =
          ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    } else if (layoutManager instanceof LinearLayoutManager) {
      int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
      if (orientation == LinearLayoutManager.VERTICAL) {
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
      } else if (orientation == LinearLayoutManager.HORIZONTAL) {
        swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
      }
    }

    return makeMovementFlags(dragFlags, swipeFlags);
  }

  @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
      RecyclerView.ViewHolder target) {

    if (source.getItemViewType() != target.getItemViewType()) return false;

    if (mDragHelperAdapter != null) {
      int sourcePosition = source.getAdapterPosition();
      int targetPosition = target.getAdapterPosition();
      mDragHelperAdapter.onItemMove(sourcePosition, targetPosition);
    }

    return true;
  }

  @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

    Log.d(TAG, "onSelectedChanged: actionState = " + actionState);

    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
      if (viewHolder instanceof ViewHolderCallback) {
        ViewHolderCallback holder = (ViewHolderCallback) viewHolder;
        holder.onItemSelected();
      }
    }

    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) applyAnimation(viewHolder.itemView);

    super.onSelectedChanged(viewHolder, actionState);
  }

  @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);

    resetAnimation(viewHolder.itemView);

    if (viewHolder instanceof ViewHolderCallback) {
      // Tell the view holder it's time to restore the idle state
      ViewHolderCallback holder = (ViewHolderCallback) viewHolder;
      holder.onItemClear();
    }
  }

  @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    /*no-op*/
  }

  @Override public boolean isLongPressDragEnabled() {
    return true;
  }

  @Override public boolean isItemViewSwipeEnabled() {
    return false;
  }

  public void applyAnimation(View view) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.1f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.1f);
    ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.9f);
    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(300);
    animSet.setInterpolator(new LinearInterpolator());
    animSet.playTogether(scaleX, scaleY, alpha);
    animSet.start();
  }

  public void resetAnimation(View view) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f);
    ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f);
    AnimatorSet animSet = new AnimatorSet();
    animSet.setDuration(300);
    animSet.setInterpolator(new LinearInterpolator());
    animSet.playTogether(scaleX, scaleY, alpha);
    animSet.start();
  }

  public interface AdapterCallback {
    boolean onItemMove(int fromPosition, int toPosition);
  }

  public interface ViewHolderCallback {
    void onItemSelected();

    void onItemClear();
  }
}
