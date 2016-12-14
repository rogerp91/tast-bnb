package com.github.testairbnd.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.roger91.mlprogress.MlProgress;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.ItemDetailContract;
import com.github.testairbnd.util.PlayServices;
import com.github.testairbnd.util.Usefulness;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.testairbnd.R.id.map;
import static com.github.testairbnd.R.id.place;

public class DetailFragment extends BaseFragment implements ItemDetailContract.View, OnMapReadyCallback {

    public static final String TAG = DetailFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static DetailFragment newInstance(int id, boolean type) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PARAM1, id);
        bundle.putBoolean(ARG_PARAM2, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.progress)
    MlProgress mProgressView;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    //Error
    @BindView(R.id.layout_message)
    RelativeLayout layout_message;
    @BindView(R.id.text_message)
    TextView text_message;

    private int id;
    private boolean from;
    @Inject
    ItemDetailContract.Presenter presenter;

    @BindView(R.id.card_container)
    View card_container;

    @BindView(R.id.img_backgroud)
    ImageView mImgBackgroud;

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.property_type)
    TextView mPropertyType;

    @BindView(R.id.room_type)
    TextView mRoomType;

    @BindView(R.id.count_guest)
    TextView mCount_guest;

    @BindView(R.id.count_bed)
    TextView mCount_bed;

    @BindView(R.id.count_best)
    TextView mCount_best;

    @BindView(R.id.count_bath)
    TextView mCount_bath;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(place)
    TextView mPlace;

    @BindView(R.id.price)
    TextView mPrice;

    @BindView(R.id.btn_favorite)
    ToggleButton mBtn_favorite;

    private SupportMapFragment mapFragment;

    @BindView(map)
    MapView mapView;
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            from = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        injectView(view);
        presenter.setView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start(id);

//        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
//        mapFragment.getMapAsync(this);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        mapFragment = SupportMapFragment.newInstance();
//        fragmentTransaction.replace(map, mapFragment).commit();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MapsInitializer.initialize(getActivity());

        // Favorite or no
        mBtn_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    presenter.removeFavorite();
                } else {
                    presenter.addFavorite();
                }
            }
        });

    }

    @Override
    public void onResume() {
        if (!PlayServices.isGooglePlayServicesAvailable(getActivity().getApplicationContext())) {
            Usefulness.showMessage(getView(), "Google Play Services required to use the app", Snackbar.LENGTH_LONG);
            getActivity().finish();
            return;
        }
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void setTitle() {
        ab.setTitle("Detail");
    }

    @Override
    public void setBackground(String url) {
        Picasso.with(getActivity()).load(url).noFade().error(R.drawable.logo_airbnb).into(mImgBackgroud);
    }

    @Override
    public void setPrice(String price) {
        mPrice.setText(price);
    }

    @Override
    public void setName(String name) {
        mName.setTextColor(Color.BLACK);
        mName.setText(name);
    }

    @Override
    public void setPropertyType(String propertyType) {
        mPropertyType.setText(propertyType);
    }

    @Override
    public void setRoomType(String roomType) {
        mRoomType.setText(roomType);
    }

    @Override
    public void setCountQuest(String countQuest) {
        mCount_guest.setText(countQuest);
    }

    @Override
    public void setCountBed(String countBed) {
        mCount_bed.setText(countBed);
    }

    @Override
    public void setCountBest(String countBest) {
        mCount_best.setText(countBest);
    }

    @Override
    public void setCountBath(String countBath) {
        mCount_bath.setText(countBath);
    }

    @Override
    public void setDescription(String description) {
        mDescription.setText(description);
        makeTextViewResizable(mDescription, 10, "More", true);
    }

    @Override
    public void setMap(Double lat, Double lon) {
        Log.d(TAG, "setMap: ");

        if (mMap == null) {
            Log.d(TAG, "setMap: null");
            return;
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1, null);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.animateCamera(cameraUpdate);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon));
        mMap.addMarker(marker);

    }

    @Override
    public void setPlace(String place) {
        mPlace.setText(place);
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
    public void showBarLayout(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        appBarLayout.setVisibility(active ? View.VISIBLE : View.GONE);
        appBarLayout.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                appBarLayout.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
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
        text_message.setText(getString(R.string.no_data_available));
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
    public void showContainerView(final boolean active) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        card_container.setVisibility(active ? View.VISIBLE : View.GONE);
        card_container.animate().setDuration(shortAnimTime).alpha(active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                card_container.setVisibility(active ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showFavorite(boolean active) {
        mBtn_favorite.setChecked(active);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public boolean isFavorite() {
        return from;
    }

    /**
     * @param tv         {@link TextView} of Layout
     * @param maxLine    max line
     * @param expandText Text
     * @param viewMore
     */
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                try {
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText, viewMore), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText, viewMore), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText, viewMore), TextView.BufferType.SPANNABLE);
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, "onGlobalLayout: " + e.getMessage());
                }
            }
        });

    }

    /**
     * Expandable tex
     *
     * @param strSpanned
     * @param tv
     * @param maxLine
     * @param spanableText
     * @param viewMore
     * @return
     */
    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv, final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 10, "More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    @Override
    public void showMessage(String message) {
        Usefulness.showMessage(getView(), message, Snackbar.LENGTH_LONG);
    }

    @OnClick(R.id.text_try_again)
    void onClickTryAgain() {//Retry
        presenter.start(id);
    }

}
