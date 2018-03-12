package com.financial.gavin.plugshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gavin on 3/11/18.
 */

public class Photo implements Parcelable {
	
	@SerializedName("id")
	@Expose
	private String id;
	
	@SerializedName("url")
	@Expose
	private String url;
	
	@SerializedName("caption")
	@Expose
	private String caption;
	
	public Photo(String id, String url, String caption) {
		this.id = id;
		this.url = url;
		this.caption = caption;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.url);
		dest.writeString(this.caption);
	}
	
	protected Photo(Parcel in) {
		this.id = in.readString();
		this.url = in.readString();
		this.caption = in.readString();
	}
	
	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
		@Override
		public Photo createFromParcel(Parcel source) {
			return new Photo(source);
		}
		
		@Override
		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};
}
