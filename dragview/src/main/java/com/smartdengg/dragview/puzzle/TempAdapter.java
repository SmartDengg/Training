package com.smartdengg.dragview.puzzle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.smartdengg.dragview.R;
import com.smartdengg.dragview.entity.TempEntity;
import java.util.List;

/**
 * 创建时间:  2016/12/28 12:38 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ViewHolder> {

  private Context context;
  private List<TempEntity> items;
  private Callback callback;

  public TempAdapter(Context context, List<TempEntity> items) {
    this.context = context;
    this.items = items;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.puzzle_temp_item, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {

    final int pos = holder.getAdapterPosition();
    final TempEntity tempEntity = items.get(pos);
    ImageView imageView = (ImageView) holder.itemView;

    if (tempEntity.isSelected) {
      imageView.setImageDrawable(context.getDrawable(tempEntity.selectedDrawable));
    } else {
      imageView.setImageDrawable(context.getDrawable(tempEntity.normalDrawable));
    }

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (callback != null) callback.onTemplateClick(pos, tempEntity);
      }
    });
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
      super(itemView);
    }
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public interface Callback {

    void onTemplateClick(int position, TempEntity tempEntity);
  }
}
