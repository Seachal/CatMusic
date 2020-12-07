package com.cat.music.ui.music.artist.contract;


import com.cat.music.bean.Music;
import com.cat.music.ui.base.BaseContract;

public interface ArtistInfoContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadArtistInfo(Music music);
    }

}
