package com.github.testairbnd.data.model;

import com.google.gson.annotations.SerializedName;

public class PricingQuote{

	private int id_listing;

	@SerializedName("guest_details")
	private GuestDetails details;

	@SerializedName("listing_currency")
	private String listing_currency;

	@SerializedName("localized_currency")
	private String localized_currency;

	@SerializedName("localized_nightly_price")
	private int localized_nightly_price;

	@SerializedName("localized_service_fee")
	private int localized_service_fee;

	@SerializedName("localized_total_price")
	private int localized_total_price;

	@SerializedName("long_term_discount_amount_as_guest")
	private int long_term_discount_amount_as_guest;

	@SerializedName("nightly_price")
	private int nightly_price;

	@SerializedName("service_fee")
	private int service_fee;

	@SerializedName("total_price")
	private int total_price;

	public int getId_listing() {
		return id_listing;
	}

	public void setId_listing(int id_listing) {
		this.id_listing = id_listing;
	}

	public GuestDetails getDetails() {
		return details;
	}

	public void setDetails(GuestDetails details) {
		this.details = details;
	}

	public String getListing_currency() {
		return listing_currency;
	}

	public void setListing_currency(String listing_currency) {
		this.listing_currency = listing_currency;
	}

	public String getLocalized_currency() {
		return localized_currency;
	}

	public void setLocalized_currency(String localized_currency) {
		this.localized_currency = localized_currency;
	}

	public int getLocalized_nightly_price() {
		return localized_nightly_price;
	}

	public void setLocalized_nightly_price(int localized_nightly_price) {
		this.localized_nightly_price = localized_nightly_price;
	}

	public int getLocalized_service_fee() {
		return localized_service_fee;
	}

	public void setLocalized_service_fee(int localized_service_fee) {
		this.localized_service_fee = localized_service_fee;
	}

	public int getLocalized_total_price() {
		return localized_total_price;
	}

	public void setLocalized_total_price(int localized_total_price) {
		this.localized_total_price = localized_total_price;
	}

	public int getLong_term_discount_amount_as_guest() {
		return long_term_discount_amount_as_guest;
	}

	public void setLong_term_discount_amount_as_guest(int long_term_discount_amount_as_guest) {
		this.long_term_discount_amount_as_guest = long_term_discount_amount_as_guest;
	}

	public int getNightly_price() {
		return nightly_price;
	}

	public void setNightly_price(int nightly_price) {
		this.nightly_price = nightly_price;
	}

	public int getService_fee() {
		return service_fee;
	}

	public void setService_fee(int service_fee) {
		this.service_fee = service_fee;
	}

	public int getTotal_price() {
		return total_price;
	}

	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}
}
