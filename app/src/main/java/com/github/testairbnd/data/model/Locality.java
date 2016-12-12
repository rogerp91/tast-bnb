package com.github.testairbnd.data.model;

/**
 * Created by AndrewX on 12/12/2016.
 */

public class Locality {

  private int id;
  private int price;
  private String localized_currency;
  private double latitud;
  private double longitud;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getLocalized_currency() {
    return localized_currency;
  }

  public void setLocalized_currency(String localized_currency) {
    this.localized_currency = localized_currency;
  }

  public double getLatitud() {
    return latitud;
  }

  public void setLatitud(double latitud) {
    this.latitud = latitud;
  }

  public double getLongitud() {
    return longitud;
  }

  public void setLongitud(double longitud) {
    this.longitud = longitud;
  }
}
