package com.github.testairbnd.ui.adapte;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.testairbnd.R;
import com.github.testairbnd.TestAirbnb;
import com.github.testairbnd.data.model.BundleDetail;
import com.github.testairbnd.data.model.Result;
import com.github.testairbnd.util.OnItemClickAddLodgings;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Roger Patiño on 11/12/2016.
 */
public class LodgingsAdapte extends RecyclerView.Adapter<LodgingsAdapte.ViewHolder> {

  private List<Result> results = null;
  private Context context;

  private OnItemClickAddLodgings onItemClickAddLodgings;

  public LodgingsAdapte(List<Result> results, OnItemClickAddLodgings onItemClickAddFavoriteListener) {
    context = TestAirbnb.getContext();
    this.results = results;
    this.onItemClickAddLodgings = onItemClickAddFavoriteListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lodgings, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Result result = results.get(position);
    holder.mName.setText(result.getListing().getName());
    holder.mType.setText(result.getListing().getRoom_type());

    String currency;
    if (!result.getPricingQuote().getLocalized_currency().isEmpty()) {
      currency = result.getPricingQuote().getLocalized_currency();
    } else {
      if (!result.getPricingQuote().getListing_currency().isEmpty()) {
        currency = result.getPricingQuote().getListing_currency();
      } else {
        currency = "USD";
      }
    }
    holder.mPrice.setText(Integer.toString(result.getPricingQuote().getLocalized_nightly_price()) + " " + currency);
    String url = result.getListing().getPicture_url();
    Picasso.with(context).load(url)
      .noFade()
      .error(R.drawable.logo_airbnb)
      .into(holder.mImgBackgroud);

    holder.mContainerCard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onItemClickAddLodgings.gotoDetails(new BundleDetail(result.getListing().getId(), result.getListing().getName(), result.getListing().getPicture_url()));
      }
    });
  }

  @Override
  public int getItemCount() {
    return (results != null ? results.size() : 0);
  }

  @Override
  public long getItemId(int position) {
    return results.get(position).getListing().getId();
  }

  /**
   * {@link com.github.testairbnd.ui.fragment.HomeFragment} onResume
   */
  public void clearData() {
    results.clear(); //clear list
    notifyDataSetChanged(); //let your adapter know about the changes and reload view.
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.container_card)
    CardView mContainerCard;

    @BindView(R.id.img_backgroud)
    ImageView mImgBackgroud;

    @BindView(R.id.name)
    TextView mName;

    @BindView(R.id.type)
    TextView mType;

    @BindView(R.id.price)
    TextView mPrice;

    @BindView(R.id.btn_favorite)
    ToggleButton mBtnFavorite;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

}
