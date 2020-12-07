package com.cat.music.ui.music.local.contract;

import com.cat.music.bean.FolderInfo;
import com.cat.music.bean.Music;
import com.cat.music.ui.base.BaseContract;

import java.util.List;

/**
 * Created by D22434 on 2018/1/8.
 */

public interface FoldersContract {

    interface View extends BaseContract.BaseView {

        void showEmptyView();
        void showSongs(List<Music> musicList);

        void showFolders(List<FolderInfo> folderInfos);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadSongs(String path);
        void loadFolders();
    }
}
