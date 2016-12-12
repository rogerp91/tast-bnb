package com.github.testairbnd.data.source;

import android.support.annotation.NonNull;

import com.github.testairbnd.data.model.Detail;
import com.github.testairbnd.data.model.ListingDetail;
import com.github.testairbnd.data.model.Lodging;

import java.util.List;

public interface LodgingsDataSource {

    interface LoadLodgingCallback {// Remote

        void onLoaded(Lodging lodgings);

        void onDataNotAvailable();

        void onErrorOcurred();

        void onErrorNotSolve();

        void onErrorNetwork();
    }

    interface LoadForIdLodgingCallback {// Remote for id

        void onLoaded(Detail detail);

        void onDataNotAvailable();

        void onErrorOcurred();

        void onErrorNotSolve();

        void onErrorNetwork();
    }

    interface GetLodgingCallback {// Local

        void onLoaded(List<ListingDetail> lodgings);

        void onDataNotAvailable();

    }

    interface GetLodgingFavoriteCallback {// Local Favorito

        void onLoaded(List<ListingDetail> lodgings);

        void onDataNotAvailable();

    }

    interface AddLodgingFavoriteCallback {// add favorite

        void onSuccess();

        void onDataNotAvailable();

        void onError();
    }

    interface RemoveLodgingFavoriteCallback {// Remover favorite

        void onSuccess();

        void onDataNotAvailable();

        void onError();
    }

    interface GetLodgingSimpleCallback {// Load one

        void onLoaded(ListingDetail listingDetail);

        void onDataNotAvailable();

    }

    void getLodgings(@NonNull String location, @NonNull LoadLodgingCallback callback);

    void refleshLodgings(@NonNull LoadLodgingCallback callback);

    void getLodgingsFavorite(@NonNull GetLodgingFavoriteCallback callback);

    void addLodgingsFavorite(@NonNull ListingDetail listingDetail, @NonNull AddLodgingFavoriteCallback callback);

    void removeLodgingsFavorite(int lodgingId, @NonNull RemoveLodgingFavoriteCallback callback);

    void getFavoriteSimpleForId(int lodgingId, @NonNull GetLodgingSimpleCallback callback);

    void getLodgingForId(int lodgingId, @NonNull LoadForIdLodgingCallback callback);

    void deleteAllLodging();
}
