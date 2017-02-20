package com.smartdengg.dragview.entity;

import java.io.Serializable;
import java.util.Objects;

public class ImageItem implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  private String imageId = "";// 原始图片Id
  private String imagePath = "";// 图片路径
  private long date;
  private String imageName = "";
  private boolean isSelected = false;// 是否被选中
  public int drawableID;

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public ImageItem newInstance() {
    try {
      return (ImageItem) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ImageItem imageItem = (ImageItem) o;
    return date == imageItem.date &&
        isSelected == imageItem.isSelected &&
        drawableID == imageItem.drawableID &&
        Objects.equals(imageId, imageItem.imageId) &&
        Objects.equals(imagePath, imageItem.imagePath) &&
        Objects.equals(imageName, imageItem.imageName);
  }

  @Override public int hashCode() {
    int result = imageName != null ? imageName.hashCode() : 0;
    result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
    result = 31 * result + (int) (date ^ (date >>> 32));
    result = 31 * result + (isSelected ? 1 : 0);
    return result;
  }

  @Override public String toString() {
    return "ImageItem{"
        + "imageId='"
        + imageId
        + '\''
        + "imageId='"
        + imageId
        + '\''
        + ", imagePath='"
        + imagePath
        + '\''
        + ", date='"
        + date
        + '\''
        + ", isSelected="
        + isSelected
        + '}';
  }
}
