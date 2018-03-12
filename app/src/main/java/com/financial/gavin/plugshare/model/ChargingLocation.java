package com.financial.gavin.plugshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gavin on 3/11/18.
 */

public class ChargingLocation implements Parcelable {

	@SerializedName("id")
	@Expose
	private String id;
	
	@SerializedName("name")
	@Expose
	private String name;
	
	@SerializedName("address")
	@Expose
	private String address;
	
	@SerializedName("description")
	@Expose
	private String description;
	
	@SerializedName("latitude")
	@Expose
	private double latitude;
	
	@SerializedName("longitude")
	@Expose
	private double longitude;
	
	@SerializedName("score")
	@Expose
	private double score;
	
	@SerializedName("photos")
	@Expose
	private Photo[] photos;
	
	public ChargingLocation(String id, String name, String address, String description,
							double latitude, double longitude, double score, Photo[] photos) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.score = score;
		this.photos = photos;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public Photo[] getPhotos() {
		return photos;
	}
	
	public void setPhotos(Photo[] photos) {
		this.photos = photos;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.address);
		dest.writeString(this.description);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeDouble(this.score);
		dest.writeTypedArray(this.photos, flags);
	}
	
	private ChargingLocation(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.address = in.readString();
		this.description = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.score = in.readDouble();
		this.photos = in.createTypedArray(Photo.CREATOR);
	}
	
	public static final Parcelable.Creator<ChargingLocation> CREATOR = new Parcelable.Creator<ChargingLocation>() {
		@Override
		public ChargingLocation createFromParcel(Parcel source) {
			return new ChargingLocation(source);
		}
		
		@Override
		public ChargingLocation[] newArray(int size) {
			return new ChargingLocation[size];
		}
	};
}
