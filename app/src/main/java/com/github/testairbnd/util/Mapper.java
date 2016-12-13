package com.github.testairbnd.util;

import com.github.testairbnd.data.model.Listing;
import com.github.testairbnd.data.model.Locality;
import com.github.testairbnd.data.model.PricingQuote;
import com.github.testairbnd.data.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndrewX on 11/12/2016.
 */

public class Mapper {

  public static List<Locality> getLocalityOfResult(List<Result> results) {
    List<Locality> localities = new ArrayList<>();
    if (results.isEmpty()) {
      return null;
    }
    for (Result result1 : results) {
      Locality locality = new Locality();

      Listing listing = result1.getListing();
      PricingQuote pricingQuote = result1.getPricingQuote();

      locality.setId(listing.getId());
      locality.setLatitud(listing.getLat());
      locality.setLongitud(listing.getLng());

      locality.setLocalized_currency(pricingQuote.getLocalized_currency());
      locality.setPrice(pricingQuote.getLocalized_nightly_price());

      localities.add(locality);
    }
    return localities;
  }

}
