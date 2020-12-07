package com.cat.music.ui.music.charts.contract;


import com.cat.music.ui.base.BaseContract;
import com.cat.music.ui.base.BaseContract;
import com.cat.music.ui.base.BaseContract;
import com.cat.music.bean.Playlist;
import com.cat.music.ui.music.charts.GroupItemData;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showNeteaseCharts(List<GroupItemData> charts);

        void showQQCharts(List<GroupItemData> charts);

        void showBaiduCharts(List<GroupItemData> charts);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadBaiDuPlaylist();

        void loadTopList();

        void loadQQList();
    }
}
