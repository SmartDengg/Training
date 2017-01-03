package com.smartdengg.puzzlecat.puzzle;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import java.util.List;

/**
 * 创建时间:  2016/12/19 18:07 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

  private static final String TAG = ItemDragHelperCallback.class.getSimpleName();
  public boolean isLongPressDragEnabled = false;

  private final AdapterCallback mAdapterCallback;

  public ItemDragHelperCallback(AdapterCallback adapterCallback) {
    this.mAdapterCallback = adapterCallback;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

    Log.d(TAG, "getMovementFlags=============================================================");
    Log.d(TAG, "getMovementFlags");
    Log.d(TAG, "=============================================================getMovementFlags");

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

    if (mAdapterCallback != null) {
      int sourcePosition = source.getAdapterPosition();
      int targetPosition = target.getAdapterPosition();

      Log.d(TAG, "onMove=============================================================");
      Log.d(TAG, "onMove sourcePosition = " + sourcePosition);
      Log.d(TAG, "onMove targetPosition = " + targetPosition);
      Log.d(TAG, "=============================================================onMove");

      mAdapterCallback.onItemMove(sourcePosition, targetPosition);
    }

    return true;
  }

  @Override
  public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos,
      RecyclerView.ViewHolder target, int toPos, int x, int y) {

    Log.d(TAG, "onMoved=============================================================");
    Log.d(TAG, "onMoved fromPos = " + fromPos);
    Log.d(TAG, "onMoved toPos = " + toPos);
    Log.d(TAG, "onMoved x = " + x);
    Log.d(TAG, "onMoved y = " + y);
    Log.d(TAG, "=============================================================onMoved");
    super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
  }

  @Override public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected,
      List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {

    //Log.d(TAG, "chooseDropTarget=============================================================");
    //Log.d(TAG, "curX = " + curX);
    //Log.d(TAG, "curY = " + curY);
    //Log.d(TAG, "=============================================================chooseDropTarget");

    return super.chooseDropTarget(selected, dropTargets, curX, curY);
  }

  @Override
  public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
      float dX, float dY, int actionState, boolean isCurrentlyActive) {

    //Log.d(TAG, "onChildDraw=============================================================");
    //Log.d(TAG, "onChildDraw actionState = " + actionState);
    //Log.d(TAG, "onChildDraw dX = " + dX);
    //Log.d(TAG, "onChildDraw dY = " + dY);
    //Log.d(TAG, "=============================================================onChildDraw");

    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      viewHolder.itemView.setTranslationX(dX);
    } else {
      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
  }

  @Override
  public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx,
      float animateDy) {

    //Log.d(TAG, "getAnimationDuration=============================================================");
    //Log.d(TAG, "getAnimationDuration animationType = " + animationType);
    //Log.d(TAG, "getAnimationDuration animateDx = " + animateDx);
    //Log.d(TAG, "getAnimationDuration animateDy = " + animateDy);
    //Log.d(TAG, "=============================================================getAnimationDuration");

    return 0;
  }

  @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

    Log.d(TAG, "onSelectedChanged=============================================================");
    Log.d(TAG, "onSelectedChanged actionState = " + actionState);
    Log.d(TAG, "=============================================================onSelectedChanged");

    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {/*拖动状态*/
      if (viewHolder instanceof ViewHolderCallback) {
        ((ViewHolderCallback) viewHolder).onItemDragged();
      }
    }
    super.onSelectedChanged(viewHolder, actionState);
  }


  @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);

    Log.d(TAG, "clearView=============================================================");
    Log.d(TAG, "             clearView           ");
    Log.d(TAG, "=============================================================clearView");

    if (viewHolder instanceof ViewHolderCallback) {
      ((ViewHolderCallback) viewHolder).onItemIdle();
    }
  }

  @Override public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current,
      RecyclerView.ViewHolder target) {
    return super.canDropOver(recyclerView, current, target);
  }

  @Override public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize,
      int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
    return super.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds,
        totalSize, msSinceStartScroll);
  }

  @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    /*no-op*/
  }

  @Override public boolean isLongPressDragEnabled() {
    return isLongPressDragEnabled;
  }

  @Override public boolean isItemViewSwipeEnabled() {
    return false;
  }

  public boolean isDragEnabled() {
    return isLongPressDragEnabled;
  }

  public interface AdapterCallback {

    boolean onItemMove(int fromPosition, int toPosition);
  }

  public interface ViewHolderCallback {

    void onItemDragged();

    void onItemIdle();
  }
}
