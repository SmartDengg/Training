package com.smartdengg.parabola;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * 创建时间:  2017/07/06 11:14 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

  private Context context;
  private List<Integer> items;
  Callback callback;

  public Adapter(Context context, List<Integer> items) {
    this.context = context;
    this.items = items;
  }

  @Override public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
  }

  @Override public void onBindViewHolder(final Adapter.ViewHolder holder, int position) {

    final Integer colorValue = items.get(holder.getAdapterPosition());
    holder.imageView.setBackgroundColor(colorValue);
    holder.textView.setText(String.valueOf(colorValue));

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (callback != null) {
          callback.onItemClick(colorValue, ParabolaUtils.getGlobalVisibleBound(holder.imageView));
        }
      }
    });
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  void setCallback(Callback callback) {
    this.callback = callback;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;

    ViewHolder(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.imageview);
      textView = (TextView) itemView.findViewById(R.id.textview);
    }
  }

  interface Callback {
    void onItemClick(Integer color, Rect startBound);
  }
}
