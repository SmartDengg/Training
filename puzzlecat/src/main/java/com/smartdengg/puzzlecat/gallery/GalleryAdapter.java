package com.smartdengg.puzzlecat.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.smartdengg.puzzlecat.CatEntity;
import com.smartdengg.puzzlecat.R;
import java.util.List;

/**
 * 创建时间:  2016/12/19 16:34 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

  private Context context;
  private List<CatEntity> items;
  private Callback mCallback;

  public GalleryAdapter(Context context, List<CatEntity> items) {
    this.context = context;
    this.items = items;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(context).inflate(R.layout.gallery_image_item, parent, false);
    return new ViewHolder(rootView);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, int position) {

    final int adapterPosition = holder.getAdapterPosition();
    final CatEntity catEntity = items.get(adapterPosition);
    holder.mCheckBox.setChecked(catEntity.isChecked);
    final ImageView imageView = holder.mImageView;
    imageView.setImageResource(catEntity.drawableID);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        catEntity.updateChecked();
        notifyItemChanged(adapterPosition);

        if (mCallback != null) mCallback.onItemClick(catEntity);
      }
    });
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  public void setCallback(@NonNull Callback callback) {
    this.mCallback = callback;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageView;
    private CheckBox mCheckBox;

    ViewHolder(View itemView) {
      super(itemView);
      mImageView = (ImageView) itemView.findViewById(R.id.imageview);
      mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
    }
  }

  public interface Callback {
    void onItemClick(CatEntity catEntity);
  }
}
