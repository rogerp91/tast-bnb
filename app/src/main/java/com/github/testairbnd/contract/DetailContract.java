package com.github.testairbnd.contract;

import android.support.annotation.NonNull;

/**
 * Created by roger on 09/12/16.
 */

public interface DetailContract {

    /**
     * TODO: View
     */
    public interface View {


        void setToolbar();

        void setActionBarUp();

    }

    /**
     * TODO: Presenter
     */
    public interface Presenter {

        void setView(@NonNull View view);

        void onResume();

        void init();

    }

}
