package com.cat.music.ui.music.mv;

import com.cat.music.ui.base.BaseContract;
import com.cat.music.ui.base.BaseContract;
import com.cyl.musicapi.netease.MvInfoDetail;
import com.cat.music.ui.base.BaseContract;

import java.util.List;


public interface MvListContract {

    interface View extends BaseContract.BaseView {
        void showMvList(List<MvInfoDetail> mvList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadPersonalizedMv();

        void loadMv(int offset);

        void loadRecentMv(int limit);

        void searchMv(String key, int offset);
    }
}
