package com.github.testairbnd.ui.adapte;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.testairbnd.R;
import com.github.testairbnd.TestAirbnb;
import com.github.testairbnd.data.model.BundleDetail;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.util.OnItemClickAddLodgings;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Roger Patiño on 08/12/2016.
 */
public class FavoriteLodgingsAdapte extends RecyclerView.Adapter<FavoriteLodgingsAdapte.ViewHolder> {

  private List<ListingDetail> listingDetails = null;
  private Context context;

  private OnItemClickAddLodgings onItemClickAddLodgings;

  public FavoriteLodgingsAdapte(List<ListingDetail> listingDetails, OnItemClickAddLodgings onItemClickAddFavoriteListener) {
    context = TestAirbnb.getContext();
    this.listingDetails = listingDetails;
    this.onItemClickAddLodgings = onItemClickAddFavoriteListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lodgings, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final ListingDetail listingDetail = listingDetails.get(position);
    holder.mName.setText(listingDetail.getName());
    holder.mType.setText(listingDetail.getRoom_type());
    holder.mPrice.setText(Integer.toString(listingDetail.getPrice()) + " " + listingDetail.getNative_currency());

    String url = listingDetail.getPicture_url();
    Picasso.with(context).load(url).noFade().error(R.drawable.logo_airbnb)
      .into(holder.mImgBackgroud);

    holder.mContainerCard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onItemClickAddLodgings.gotoDetails(new BundleDetail(listingDetail.getId(), listingDetail.getName(), listingDetail.getPicture_url()));
      }
    });
  }

  @Override
  public int getItemCount() {
    return (listingDetails != null ? listingDetails.size() : 0);
  }

  @Override
  public long getItemId(int position) {
    return listingDetails.get(position).getId();
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

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
