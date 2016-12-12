package com.github.testairbnd.di;

import com.github.testairbnd.contract.DetailContract;
import com.github.testairbnd.contract.FavoriteContract;
import com.github.testairbnd.contract.HomeContract;
import com.github.testairbnd.contract.ItemDetailContract;
import com.github.testairbnd.contract.LoginContract;
import com.github.testairbnd.contract.MainContact;
import com.github.testairbnd.contract.MapContract;
import com.github.testairbnd.presenter.DetailPresenter;
import com.github.testairbnd.presenter.FavoritePresenter;
import com.github.testairbnd.presenter.HomePresenter;
import com.github.testairbnd.presenter.ItemDetailPresenter;
import com.github.testairbnd.presenter.LoginPresenter;
import com.github.testairbnd.presenter.MainPresenter;
import com.github.testairbnd.presenter.MapPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger Patiño on 01/12/2015.
 */
@Module(library = true, complete = false)
public class PresenterModule {

  @Provides
  @Singleton
  LoginContract.Presenter provideLoginPresenter(LoginPresenter presenter) {
    return presenter;
  }

  @Provides
  @Singleton
  MainContact.Presenter provideMainPresenter(MainPresenter presenter) {
    return presenter;
  }

  @Provides
  @Singleton
  HomeContract.Presenter provideHomePresenter(HomePresenter presenter) {
    return presenter;

  }

  @Provides
  @Singleton
  FavoriteContract.Presenter provideFavoritePresenter(FavoritePresenter presenter) {
    return presenter;
  }

  @Provides
  @Singleton
  MapContract.Presenter provideMapPresenter(MapPresenter presenter) {
    return presenter;
  }

  @Provides
  @Singleton
  DetailContract.Presenter provideDetailPresenter(DetailPresenter presenter) {
    return presenter;
  }

  @Provides
  @Singleton
  ItemDetailContract.Presenter provideItemDetailPresenter(ItemDetailPresenter presenter) {
    return presenter;
  }

}
