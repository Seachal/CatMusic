package com.cat.music.di.component;

import android.app.Activity;
import android.content.Context;

import com.cat.music.di.module.ActivityModule;
import com.cat.music.di.scope.ContextLife;
import com.cat.music.di.scope.PerActivity;
import com.cat.music.ui.chat.ChatDetailActivity;
import com.cat.music.ui.my.BindLoginActivity;
import com.cat.music.ui.my.LoginActivity;
import com.cat.music.ui.my.RegisterActivity;
import com.cat.music.ui.chat.ChatActivity;
import com.cat.music.ui.music.artist.activity.ArtistDetailActivity;
import com.cat.music.ui.music.charts.activity.BaiduMusicListActivity;
import com.cat.music.ui.music.charts.activity.BasePlaylistActivity;
import com.cat.music.ui.music.edit.EditSongListActivity;
import com.cat.music.ui.music.mv.VideoDetailActivity;
import com.cat.music.ui.music.mv.VideoPlayerActivity;
import com.cat.music.ui.music.playlist.detail.PlaylistDetailActivity;
import com.cat.music.ui.music.playpage.LockScreenPlayerActivity;
import com.cat.music.ui.music.playpage.PlayerActivity;
import com.cat.music.ui.music.search.SearchActivity;

import org.jetbrains.annotations.NotNull;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(BaiduMusicListActivity baiduMusicListActivity);


    void inject(RegisterActivity registerActivity);

    void inject(LoginActivity loginActivity);

    void inject(SearchActivity searchActivity);

    void inject(BasePlaylistActivity basePlaylistActivity);

//    void inject(VideoDetailActivity videoDetailActivity);
//
    void inject(@NotNull PlayerActivity playerActivity);

    void inject(PlaylistDetailActivity playlistDetailActivity);

    void inject(ArtistDetailActivity playlistDetailActivity);

    void inject(EditSongListActivity editMusicActivity);

    void inject(@NotNull ChatActivity chatActivity);

    void inject(@NotNull ChatDetailActivity chatDetailActivity);

    void inject(@NotNull VideoPlayerActivity baiduMvDetailActivity);

    void inject(@NotNull LockScreenPlayerActivity lockScreenPlayerActivity);

    void inject(@NotNull BindLoginActivity bindLoginActivity);

    void inject(@NotNull VideoDetailActivity videoDetailActivity);
}
