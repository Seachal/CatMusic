package com.cat.music.ui.music.playlist.history;

import com.cat.music.bean.Music;
import com.cat.music.ui.base.BaseContract;
import com.cat.musiclake.bean.Music;
import com.cat.music.ui.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cat.music.ui.base.BaseContract;

import java.util.List;

public interface RecentlyContract {

    interface View extends BaseContract.BaseView {

        void showSongs(List<Music> songs);

        void showEmptyView();

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void loadSongs();

        void clearHistory();
    }
}
