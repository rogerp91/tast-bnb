package com.github.testairbnd.data.model;

import com.google.gson.annotations.SerializedName;

public class Listing  {

    private boolean favorite;

    @SerializedName("name")
    private String name;

    @SerializedName("public_address")
    private String public_address;

    @SerializedName("room_type")
    private String room_type;

    @SerializedName("room_type_category")
    private String room_type_category;

    @SerializedName("id")
    private int id;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("scrim_color")
    private String scrim_color;

    @SerializedName("picture_url")
    private String picture_url;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublic_address() {
        return public_address;
    }

    public void setPublic_address(String public_address) {
        this.public_address = public_address;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_type_category() {
        return room_type_category;
    }

    public void setRoom_type_category(String room_type_category) {
        this.room_type_category = room_type_category;
    }

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

    public String getScrim_color() {
        return scrim_color;
    }

    public void setScrim_color(String scrim_color) {
        this.scrim_color = scrim_color;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
}
