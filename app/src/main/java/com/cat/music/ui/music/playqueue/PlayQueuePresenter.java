package com.cat.music.ui.music.playqueue;

import com.cat.music.bean.Music;
import com.cat.music.player.PlayManager;
import com.cat.music.ui.base.BasePresenter;
import com.cat.musiclake.bean.Music;
import com.cat.music.player.PlayManager;
import com.cat.music.ui.base.BasePresenter;
import com.cat.music.ui.base.BasePresenter;
import com.cyl.musiclake.bean.Music;
import com.cat.music.player.PlayManager;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by yonglong on 2018/1/7.
 */

public class PlayQueuePresenter extends BasePresenter<PlayQueueContract.View> implements PlayQueueContract.Presenter {

    @Inject
    public PlayQueuePresenter() {
    }

    @Override
    public void attachView(PlayQueueContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadSongs() {
        List<Music> musicList = PlayManager.getPlayList();
        mView.showSongs(musicList);
    }

    @Override
    public void clearQueue() {
        PlayManager.clearQueue();
        loadSongs();
    }
}
