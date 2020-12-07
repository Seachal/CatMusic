package com.cat.music.ui.music.local.contract;


import com.cat.music.ui.base.BaseContract;
import com.cat.music.ui.base.BaseContract;
import com.cat.music.bean.Artist;
import com.cat.music.ui.base.BaseContract;

import java.util.List;

public interface ArtistContract {

    interface View extends BaseContract.BaseView {

        void showArtists(List<Artist> artists);

        void showEmptyView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadArtists(String action);
    }
}
