package com.github.testairbnd.contract;

import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.presenter.BasePresenter;
import com.github.testairbnd.ui.BaseView;

import java.util.List;

/**
 * Created by roger on 08/12/16.
 */

public interface FavoriteContract {

    /**
     * TODO: View
     */
    public interface View extends BaseView<List<ListingDetail>> {

    }

    /**
     * TODO: Presenter
     */
    public interface Presenter extends BasePresenter<FavoriteContract.View> {

    }

}
