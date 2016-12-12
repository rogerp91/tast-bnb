package com.github.testairbnd.data.source.remote;

import com.github.testairbnd.data.model.Detail;
import com.github.testairbnd.data.model.Lodging;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Roger Patiño on 06/01/2016.
 */
public interface HttpRestClient {

  public static String BASE_URL = "https://api.airbnb.com/";

  String VERSION = "v2/";
  String TOKEN = "3092nxybyb0otqw18e8nh5nty";
  String FORMA = "v1_legacy_for_p3";

//    @Headers("Content-Type: application/json; charset=UTF-8")
//    @GET(VERSION + "search_results")
//    Call<Lodging> getLodgings2(@Query("client_id") String client_id);

  @Headers("Content-Type: application/json; charset=UTF-8")
  @GET(VERSION + "search_results")
  Call<Lodging> getLodgings(@Query("client_id") String client_id, @Query("location") String location);

  @Headers("Content-Type: application/json; charset=UTF-8")
  @GET(VERSION + "listings/{id}")
  Call<Detail> getLodgingsForId(@Path("id") int id, @Query("client_id") String client_id, @Query("_format") String _format);

}
