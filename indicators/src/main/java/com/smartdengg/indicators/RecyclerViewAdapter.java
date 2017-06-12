package com.smartdengg.indicators;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 创建时间:  2017/02/28 11:17 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private Context context;

  public RecyclerViewAdapter(Context context) {
    this.context = context;
  }

  @Override
  public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    return new ViewHolder(rootView);
  }

  @SuppressLint("SetTextI18n") @Override
  public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
    int color = ColorsHelper.getRandomColor();
    TextView textView = (TextView) holder.itemView;
    textView.setBackgroundColor(color);
    textView.setText("#" + Integer.toHexString(color));
  }

  @Override public int getItemCount() {
    return 16;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
