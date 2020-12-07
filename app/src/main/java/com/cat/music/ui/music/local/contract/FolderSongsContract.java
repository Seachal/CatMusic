package com.cat.music.ui.music.local.contract;

import com.cat.music.bean.Music;
import com.cat.music.ui.base.BaseContract;
import com.cat.musiclake.bean.Music;
import com.cat.music.ui.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cat.music.ui.base.BaseContract;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public interface FolderSongsContract {

     interface View extends BaseContract.BaseView {

        void showEmptyView();

        void showSongs(List<Music> musicList);

    }

     interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs(String path);

    }
}
