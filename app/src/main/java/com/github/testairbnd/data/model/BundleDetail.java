package com.github.testairbnd.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roge on 11/12/2016.
 */
public class BundleDetail implements Parcelable {
  private int id;
  private String name;
  private String url;

  public BundleDetail(int id, String name, String url) {
    this.id = id;
    this.name = name;
    this.url = url;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeString(this.url);
  }

  public BundleDetail() {
  }


  protected BundleDetail(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.url = in.readString();
  }

  public static final Parcelable.Creator<BundleDetail> CREATOR = new Parcelable.Creator<BundleDetail>() {
    @Override
    public BundleDetail createFromParcel(Parcel source) {
      return new BundleDetail(source);
    }

    @Override
    public BundleDetail[] newArray(int size) {
      return new BundleDetail[size];
    }
  };
}
