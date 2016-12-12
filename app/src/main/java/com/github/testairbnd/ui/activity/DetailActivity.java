package com.github.testairbnd.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.testairbnd.R;
import com.github.testairbnd.contract.DetailContract;
import com.github.testairbnd.data.model.BundleDetail;
import com.github.testairbnd.data.model.DataReplace;
import com.github.testairbnd.ui.fragment.DetailFragment;
import com.github.testairbnd.util.Usefulness;

import javax.inject.Inject;

import butterknife.BindView;

public class DetailActivity extends BaseActivity implements DetailContract.View {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Inject
  DetailContract.Presenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    injectView();
    presenter.setView(this);

    if (savedInstanceState == null) {
      Bundle bundle = getIntent().getExtras();
      BundleDetail bundleDetail = bundle.getParcelable("detail");
      boolean from = bundle.getBoolean("favorite");
      if (bundleDetail == null) {
        finish();
        return;
      }
      Usefulness.gotoFragment(new DataReplace(getSupportFragmentManager(),
        DetailFragment.newInstance(bundleDetail.getId(), from), R.id.main_content));
    }

  }

  @Override
  public void setToolbar() {
    setSupportActionBar(toolbar);
  }

  @Override
  public void setActionBarUp() {
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

}
