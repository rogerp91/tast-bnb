package com.github.testairbnd.contract;

import com.github.testairbnd.data.model.Locality;
import com.github.testairbnd.presenter.BasePresenter;
import com.github.testairbnd.ui.BaseView;

import java.util.List;

/**
 * Created by roger on 08/12/16.
 */

public interface MapContract {

    /**
     * TODO: View
     */
    public interface View extends BaseView<List<Locality>> {

    }

    /**
     * TODO: Presenter
     */
    public interface Presenter extends BasePresenter<MapContract.View> {

    }

}
