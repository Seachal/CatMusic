package com.cat.music.ui.download.ui;

import android.text.TextUtils;

import com.cat.music.MusicApp;
import com.cat.music.api.music.MusicApi;
import com.cat.music.api.net.ApiManager;
import com.cat.music.api.net.RequestCallBack;
import com.cat.music.bean.Music;
import com.cat.music.common.Constants;
import com.cat.music.utils.ToastUtils;


public class DownloadManager {

    public void downLoadMusic(Music music,boolean isCache){
        if (music == null) {
            ToastUtils.show(MusicApp.getAppContext(), "外链信息为空!");
            return;
        }
        if (TextUtils.equals(music.getType(), Constants.LOCAL)) {
            ToastUtils.show(MusicApp.getAppContext(), "不能下载本地歌曲!");
            return;
        }

        ApiManager.request(MusicApi.INSTANCE.getMusicDownloadUrl(music, isCache), new RequestCallBack<String>() {
            @Override
            public void success(String result) {

            }

            @Override
            public void error(String msg) {

            }
        });
    }
}
