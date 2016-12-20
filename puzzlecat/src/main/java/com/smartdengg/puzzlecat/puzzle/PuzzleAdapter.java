package com.smartdengg.puzzlecat.puzzle;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  private static final int ITEM_VIEW_TYPE_HEADER = 0;
  private static final int ITEM_VIEW_TYPE_ITEM = 1;

  private Context context;
  private List<CatEntity> items;

  public PuzzleAdapter(Context context, List<CatEntity> items) {
    this.context = context;
    this.items = items;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    int layoutID;
    if (viewType == ITEM_VIEW_TYPE_HEADER) {
      layoutID = R.layout.big_image_item;
    } else {
      layoutID = R.layout.small_image_item;
    }

    View rootView = LayoutInflater.from(context).inflate(R.layout.puzzle_image_item, parent, false);
    return new ViewHolder(rootView);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {

    final int adapterPosition = holder.getAdapterPosition();
    ImageView imageView = (ImageView) holder.itemView;
    Integer drawableID = items.get(adapterPosition).drawableID;
    imageView.setImageResource(drawableID);
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  /*@Override public int getItemViewType(int position) {
    return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
  }*/

  public boolean isHeader(int position) {
    return position == 0;
  }

  public boolean isSingular() {
    return items.size() % 2 == 1;
  }

  @Override public boolean onItemMove(final int fromPosition, final int toPosition) {
    Collections.swap(items, fromPosition, toPosition);
    notifyItemMoved(fromPosition, toPosition);

    /*CatEntity targetEntity = items.remove(toPosition);
    items.add(fromPosition, targetEntity);
    notifyItemMoved(toPosition, fromPosition);*/

   /* if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(items, i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(items, i, i - 1);
      }
    }
    notifyItemMoved(fromPosition, toPosition);*/

    /*if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(items, i, i + 1);
        //notifyItemMoved(i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(items, i, i - 1);
        //notifyItemMoved(i, i - 1);
      }
    }*/

    /*if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(items, i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(items, i, i - 1);
      }
    }*/

    //notifyItemMoved(fromPosition, toPosition);

    /*new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        notifyDataSetChanged();
      }
    }, 1000);*/

    //Log.d("ItemDragHelperCallback",
    //    "items = " + Arrays.toString(items.toArray(new CatEntity[items.size()])));

    return true;
  }

  static class ViewHolder extends RecyclerView.ViewHolder
      implements ItemDragHelperCallback.ViewHolderCallback {

    ViewHolder(View itemView) {
      super(itemView);
    }

    @Override public void onItemSelected() {
      itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override public void onItemClear() {
      itemView.setBackgroundColor(0);
    }
  }
}
