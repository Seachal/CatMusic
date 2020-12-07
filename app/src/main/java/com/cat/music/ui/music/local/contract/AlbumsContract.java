package com.cat.music.ui.music.local.contract;

import com.cat.music.ui.base.BaseContract;
import com.cat.music.ui.base.BaseContract;
import com.cat.music.bean.Album;
import com.cat.music.ui.base.BaseContract;

import java.util.List;

public interface AlbumsContract {

    interface View extends BaseContract.BaseView {

        void showAlbums(List<Album> albumList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadAlbums(String action);

    }
}
