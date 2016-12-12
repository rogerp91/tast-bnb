package com.github.testairbnd.data.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListingDetail extends RealmObject{

  @PrimaryKey
  @SerializedName("id")
  private int id;

  @SerializedName("lat")
  private double lat;

  @SerializedName("lng")
  private double lng;

  @SerializedName("address")
  private String address;

  @SerializedName("name")
  private String name;

  @SerializedName("native_currency")
  private String native_currency;

  @SerializedName("picture_url")
  private String picture_url;

  @SerializedName("price")
  private int price;

  @SerializedName("price_formatted")
  private String price_formatted;

  @SerializedName("price_native")
  private int price_native;

  @SerializedName("scrim_color")
  private String scrim_color;

  @SerializedName("smart_location")
  private String smart_location;

  @SerializedName("property_type")
  private String property_type;

  @SerializedName("room_type")
  private String room_type;

  @SerializedName("public_address")
  private String public_address;

  @SerializedName("guests_included")
  private int guests_included;

  @SerializedName("bathrooms")
  private double bathrooms;

  @SerializedName("bedrooms")
  private int bedrooms;

  @SerializedName("beds")
  private int beds;

  @SerializedName("description")
  private String description;

  private boolean favorite;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNative_currency() {
    return native_currency;
  }

  public void setNative_currency(String native_currency) {
    this.native_currency = native_currency;
  }

  public String getPicture_url() {
    return picture_url;
  }

  public void setPicture_url(String picture_url) {
    this.picture_url = picture_url;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getPrice_formatted() {
    return price_formatted;
  }

  public void setPrice_formatted(String price_formatted) {
    this.price_formatted = price_formatted;
  }

  public int getPrice_native() {
    return price_native;
  }

  public void setPrice_native(int price_native) {
    this.price_native = price_native;
  }

  public String getScrim_color() {
    return scrim_color;
  }

  public void setScrim_color(String scrim_color) {
    this.scrim_color = scrim_color;
  }

  public String getSmart_location() {
    return smart_location;
  }

  public void setSmart_location(String smart_location) {
    this.smart_location = smart_location;
  }

  public String getProperty_type() {
    return property_type;
  }

  public void setProperty_type(String property_type) {
    this.property_type = property_type;
  }

  public String getRoom_type() {
    return room_type;
  }

  public void setRoom_type(String room_type) {
    this.room_type = room_type;
  }

  public String getPublic_address() {
    return public_address;
  }

  public void setPublic_address(String public_address) {
    this.public_address = public_address;
  }

  public int getGuests_included() {
    return guests_included;
  }

  public void setGuests_included(int guests_included) {
    this.guests_included = guests_included;
  }

  public double getBathrooms() {
    return bathrooms;
  }

  public void setBathrooms(double bathrooms) {
    this.bathrooms = bathrooms;
  }

  public int getBedrooms() {
    return bedrooms;
  }

  public void setBedrooms(int bedrooms) {
    this.bedrooms = bedrooms;
  }

  public int getBeds() {
    return beds;
  }

  public void setBeds(int beds) {
    this.beds = beds;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }
}
