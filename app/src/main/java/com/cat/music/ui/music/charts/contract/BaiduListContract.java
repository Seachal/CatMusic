package com.cat.music.ui.music.charts.contract;


import com.cat.music.bean.Music;
import com.cat.music.ui.base.BaseContract;

import java.util.List;

public interface BaiduListContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showOnlineMusicList(List<Music> musicList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadOnlineMusicList(String type, int limit, int mOffset);

    }
}
