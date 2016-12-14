package com.github.testairbnd.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.roger91.mlprogress.MlProgress;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.FavoriteContract;
import com.github.testairbnd.data.model.BundleDetail;
import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.ui.activity.DetailActivity;
import com.github.testairbnd.ui.adapte.FavoriteLodgingsAdapte;
import com.github.testairbnd.util.OnItemClickAddLodgings;
import com.github.testairbnd.util.Usefulness;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by roger on 07/12/16.
 */

public class FavoriteFragment extends BaseFragment implements FavoriteContract.View {

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @BindView(R.id.progress)
    MlProgress mProgressView;

    //Error
    @BindView(R.id.layout_message)
    RelativeLayout layout_message;
    @BindView(R.id.text_message)
    TextView text_message;

    private FavoriteLodgingsAdapte adapte;
    @BindView(R.id.recycler_list)
    RecyclerView recycler;

    @Inject
    FavoriteContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        injectView(view);
        presenter.setView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recycler
        recycler.setHasFixedSize(true);
        LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setTitle() {
//    ab.setTitle("Favorite");
    }

    @Override
    public void showProgress(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

    }

    @Override
    public void showModels(List<ListingDetail> listingDetails) {
        adapte = new FavoriteLodgingsAdapte(listingDetails, onClickAddFavorite);
        adapte.setHasStableIds(true);
        recycler.setAdapter(adapte);
    }

    @Override
    public void showNoModels(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.no_data_available_favorite));
    }

    @Override
    public void showNetworkError(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.no_connection));
    }

    @Override
    public void showErrorOcurred(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.error_occurred));
    }

    @Override
    public void showErrorNotSolve(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
        layout_message.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout_message.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
        text_message.setText(getString(R.string.error_many));
    }

    @Override
    public void showMessage(String message) {
        Usefulness.showMessage(getView(), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * From {@link com.github.testairbnd.ui.adapte.LodgingsAdapte}
     */
    public OnItemClickAddLodgings onClickAddFavorite = new OnItemClickAddLodgings() {

        @Override
        public void gotoDetails(BundleDetail bundleDetail) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("favorite", false);
            bundle.putParcelable("detail", bundleDetail);
            DataIntent dataIntent = new DataIntent();
            dataIntent.setActivity(getActivity());
            dataIntent.setClass_(DetailActivity.class);
            dataIntent.setBundle(bundle);
            Usefulness.gotoActivity(dataIntent);
        }
    };

    @OnClick(R.id.text_try_again)
    void onClickTryAgain() {//Retry
        presenter.start();
    }

}
