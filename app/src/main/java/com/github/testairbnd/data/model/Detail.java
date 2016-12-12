package com.github.testairbnd.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roger on 09/12/16.
 */

public class Detail {

    @SerializedName("listing")
    private ListingDetail listing;

    public ListingDetail getListing() {
        return listing;
    }

    public void setListing(ListingDetail listing) {
        this.listing = listing;
    }
}
