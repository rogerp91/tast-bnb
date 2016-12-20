package com.github.testairbnd.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.rogerp91.pref.SP;
import com.github.testairbnd.R;
import com.github.testairbnd.contract.MainContact;
import com.github.testairbnd.data.model.DataIntent;
import com.github.testairbnd.data.model.DataReplace;
import com.github.testairbnd.ui.fragment.FavoriteFragment;
import com.github.testairbnd.ui.fragment.HomeFragment;
import com.github.testairbnd.ui.fragment.MapFragment;
import com.github.testairbnd.util.AccessTokenFacebook;
import com.github.testairbnd.util.Devices;
import com.github.testairbnd.util.ProfileView;
import com.github.testairbnd.util.Usefulness;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.github.testairbnd.util.Constants.FACEBOOK_ID;
import static com.github.testairbnd.util.Constants.FACEBOOK_NAME;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_NOT_GPS_ACTIVE;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_PERMISSION_FAILED;
import static com.github.testairbnd.util.Constants.INTENT_ACTION_PERMISSION_SUCCESS;
import static com.github.testairbnd.util.Constants.NO_DIALOG_SHOW;


public class MainActivity extends BaseActivity implements MainContact.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    MainContact.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!AccessTokenFacebook.isLoggedIn()) {
            Usefulness.gotoActivity(new DataIntent(this, LoginActivity.class, true));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        injectView();
        presenter.setView(this);
        presenter.init();
        setNav(savedInstanceState);

        Devices.isMarshmallowCheckCoarseFine(this);

    }


    @Override
    public void setToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public void setActionBarIcon() {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void showDialogGPS() {
//        new MaterialDialog.Builder(this).title(R.string.setting_gps_title).
//                content(R.string.setting_gps_content)
//                .positiveText(R.string.setting_gps_disagree)
//                .negativeText(R.string.setting_gps_agree)
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                }).onPositive(new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                SP.putBoolean(NO_DIALOG_SHOW, true);
//                sendBroadcast(new Intent(INTENT_ACTION_NOT_GPS_ACTIVE));
//            }
//        }).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(R.string.setting_gps_title);
        alertDialogBuilder.setMessage(R.string.setting_gps_content)
                .setPositiveButton(R.string.setting_gps_disagree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SP.putBoolean(NO_DIALOG_SHOW, true);
                        sendBroadcast(new Intent(INTENT_ACTION_NOT_GPS_ACTIVE));
                    }
                })
                .setNegativeButton(R.string.setting_gps_agree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @PermissionSuccess(requestCode = 100)
    @Override
    public void locationSuccess() {
        sendBroadcast(new Intent(INTENT_ACTION_PERMISSION_SUCCESS));
    }

    @PermissionFail(requestCode = 100)
    @Override
    public void locationFail() {
        sendBroadcast(new Intent(INTENT_ACTION_PERMISSION_FAILED));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Assign menu
     *
     * @param savedInstanceState @{@link Bundle}
     */
    private void setNav(Bundle savedInstanceState) {
        if (navigationView != null) { // Set nav
            setupDrawerContent(navigationView);
        }

        assert navigationView != null;
        View headerNav = navigationView.inflateHeaderView(R.layout.nav_header); // Inflate the drawer header
        ProfileView findProfile = new ProfileView(); // Assign the view to the object
        findProfile.setNames((TextView) headerNav.findViewById(R.id.names));
        findProfile.setCircleImageView((CircleImageView) headerNav.findViewById(R.id.circle_image));
        showProfile(findProfile);
        if (savedInstanceState == null) {
            selectItem(R.id.nav_home);
//            Usefulness.gotoFragment(new DataReplace(getSupportFragmentManager(), HomeFragment.newInstance(), R.id.main_content));
        }
    }

    /**
     * Set Profile
     *
     * @param profile @{@link ProfileView}
     */
    private void showProfile(ProfileView profile) {
        String name = SP.getString(FACEBOOK_NAME, "");
        String id = SP.getString(FACEBOOK_ID, "");
        if (name.trim().length() == 0) {
            name = "Undefined";
        }
        profile.getNames().setText(name);
        String url = "https://graph.facebook.com/" + id + "/picture?type=large";
        Log.d(TAG, "profileURL:" + url);
        Picasso.with(this).load(url).noFade().placeholder(R.drawable.logo_airbnb).error(R.drawable.logo_airbnb).into(profile.getCircleImageView());
    }

    /**
     * Setup Drawer Content
     *
     * @param navigationView @{@link NavigationView}
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        menuItem.setChecked(true);
                        // fix: Problem load fragment
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                selectItem(menuItem.getItemId());
                            }
                        }, 300);

                        return true;
                    }
                }
        );
    }

    /**
     * Select Item
     *
     * @param itemDrawer @{@link MenuItem}i5
     */
    private void selectItem(int itemDrawer) {
        switch (itemDrawer) {
            case R.id.nav_home:
                setTitle("Home");
                Usefulness.gotoFragment(new DataReplace(getSupportFragmentManager(), HomeFragment.newInstance(), R.id.main_content));
                break;
            case R.id.nav_favorite:
                setTitle("Favorite");
                Usefulness.gotoFragment(new DataReplace(getSupportFragmentManager(), FavoriteFragment.newInstance(), R.id.main_content));
                break;
            case R.id.nav_map:
                setTitle("Map");
                Usefulness.gotoFragment(new DataReplace(getSupportFragmentManager(), MapFragment.newInstance(), R.id.main_content));
                break;
        }
    }
}
