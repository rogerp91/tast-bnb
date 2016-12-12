package com.github.testairbnd.util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.data.model.DataReplace;

/**
 * Created by roger on 07/12/16.
 */
public class Usefulness {

    /**
     * Ir a una activity
     *
     * @param dataIntent @{@link DataIntent}
     */
    public static void gotoActivity(@NonNull DataIntent dataIntent) {

        if (dataIntent.getActivity() == null) {
            throw new IllegalArgumentException("Requires context");
        }

        if (dataIntent.getClass_() == null) {
            throw new IllegalArgumentException("Requires class");
        }

        Intent intent = new Intent(dataIntent.getActivity(), dataIntent.getClass_());

        if (dataIntent.getFlag() != 0) {
            intent.addFlags(dataIntent.getFlag());//Flat
        }

        if (dataIntent.getBundle() != null) {
            intent.putExtras(dataIntent.getBundle());//Bundle
        }

        dataIntent.getActivity().startActivity(intent);//Start

        if (dataIntent.isAnim()) {
            dataIntent.getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//Anim
        }

        if (dataIntent.isFinish()) {
            dataIntent.getActivity().finish();//Finish
        }
    }

    /**
     * Ir al un Fragment
     *
     * @param dataReplace @{@link DataReplace}
     */
    public static void gotoFragment(DataReplace dataReplace) {

        if (dataReplace.getFragmentManager() == null) {
            throw new IllegalArgumentException("Requires context");
        }

        if (dataReplace.getFragment() == null) {
            throw new IllegalArgumentException("Requires fragment");
        }
        dataReplace.getFragmentManager().beginTransaction().replace(dataReplace.getContainer(), dataReplace.getFragment())
                .commit();

    }

    /**
     * Mostrar  mensaje
     *
     * @param view     @{@link View}
     * @param msg      @{@link String}
     * @param duration @{@link Snackbar.Duration}
     */
    public static void showMessage(View view, String msg, int duration) {
        Snackbar.make(view, msg, duration).show();
    }

}
