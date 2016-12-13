package com.github.testairbnd.di;

import com.github.testairbnd.data.source.remote.HttpRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Roger Patiño on 08/07/2016.
 */

@Module(complete = false, library = true)
public class ClientModule {

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder().create();
  }

  @Provides
  @Singleton
  HttpLoggingInterceptor provideInterceptor() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return interceptor;
  }


  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
    return new OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .connectTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .build();
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
      .baseUrl(HttpRestClient.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(okHttpClient)
      .build();
  }

  @Provides
  HttpRestClient provideHttpRestClient(Retrofit retrofit) {
    return retrofit.create(HttpRestClient.class);
  }
}
