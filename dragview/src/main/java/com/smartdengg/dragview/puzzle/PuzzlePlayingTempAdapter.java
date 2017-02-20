package com.smartdengg.dragview.puzzle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.smartdengg.dragview.R;
import java.util.List;

/**
 * 创建时间:  2016/12/28 12:38 <br>
 * 作者:  dengwei <br>
 * 描述:  "使用拼了营销房源" - 玩转拼图 模板选择Adapter
 */
public class PuzzlePlayingTempAdapter
    extends RecyclerView.Adapter<PuzzlePlayingTempAdapter.ViewHolder> {

  private Context context;
  private List<TemplateGroup.Temp> items;
  private Callback callback;

  public PuzzlePlayingTempAdapter(Context context, List<TemplateGroup.Temp> items) {
    this.context = context;
    this.items = items;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.puzzle_template_item, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {

    final int pos = holder.getAdapterPosition();
    final TemplateGroup.Temp tempEntity = items.get(pos);
    ImageView imageView = (ImageView) holder.itemView;

    if (tempEntity.isSelected) {
      imageView.setImageDrawable(context.getResources().getDrawable(tempEntity.selectedDrawable));
    } else {
      imageView.setImageDrawable(context.getResources().getDrawable(tempEntity.normalDrawable));
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

    void onTemplateClick(int position, TemplateGroup.Temp temp);
  }
}
