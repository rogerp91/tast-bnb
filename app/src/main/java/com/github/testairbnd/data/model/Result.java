package com.github.testairbnd.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roger on 08/12/16.
 */

public class Result {

    @SerializedName("listing")
    private Listing listing;

    @SerializedName("pricing_quote")
    private PricingQuote pricingQuote;

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public PricingQuote getPricingQuote() {
        return pricingQuote;
    }

    public void setPricingQuote(PricingQuote pricingQuote) {
        this.pricingQuote = pricingQuote;
    }
}
