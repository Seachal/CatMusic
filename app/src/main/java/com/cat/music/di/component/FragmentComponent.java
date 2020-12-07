package com.cat.music.di.component;

import android.app.Activity;
import android.content.Context;

import com.cat.music.di.module.FragmentModule;
import com.cat.music.di.scope.PerFragment;
import com.cat.music.di.module.FragmentModule;
import com.cat.music.di.scope.PerFragment;
import com.cat.music.di.module.FragmentModule;
import com.cat.music.di.scope.ContextLife;
import com.cat.music.di.scope.PerFragment;
import com.cat.music.ui.music.artist.fragment.ArtistInfoFragment;
import com.cat.music.ui.music.discover.artist.QQArtistListFragment;
import com.cat.music.ui.music.discover.DiscoverFragment;
import com.cat.music.ui.download.ui.DownloadManagerFragment;
import com.cat.music.ui.music.local.fragment.AlbumDetailFragment;
import com.cat.music.ui.music.local.fragment.AlbumFragment;
import com.cat.music.ui.music.local.fragment.ArtistFragment;
import com.cat.music.ui.music.artist.fragment.ArtistSongsFragment;
import com.cat.music.ui.music.local.fragment.LocalVideoFragment;
import com.cat.music.ui.music.local.fragment.FoldersFragment;
import com.cat.music.ui.music.mv.MvListFragment;
import com.cat.music.ui.music.charts.fragment.ChartsDetailFragment;
import com.cat.music.ui.music.mv.MvSearchListFragment;
import com.cat.music.ui.music.mv.VideoCommentFragment;
import com.cat.music.ui.music.mv.VideoDetailFragment;
import com.cat.music.ui.music.playlist.love.LoveFragment;
import com.cat.music.ui.music.my.MyMusicFragment;
import com.cat.music.ui.music.bottom.PlayControlFragment;
import com.cat.music.ui.music.playlist.detail.PlaylistDetailFragment;
import com.cat.music.ui.music.playlist.PlaylistFragment;
import com.cat.music.ui.music.playlist.history.RecentlyFragment;
import com.cat.music.ui.music.local.fragment.SongsFragment;
import com.cat.music.ui.download.ui.DownloadedFragment;
import com.cat.music.ui.music.playlist.square.TopPlaylistFragment;
import com.cat.music.ui.music.playqueue.PlayQueueFragment;
import com.cat.music.ui.music.search.fragment.SearchSongsFragment;
import com.cat.music.ui.music.search.fragment.YoutubeSearchFragment;
import com.cat.music.ui.my.BindLoginActivity;

import org.jetbrains.annotations.NotNull;

import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(AlbumDetailFragment fragment);

    void inject(AlbumFragment fragment);

    void inject(ArtistFragment artistFragment);

    void inject(ArtistSongsFragment artistSongsFragment);

    void inject(FoldersFragment foldersFragment);

    void inject(RecentlyFragment recentlyFragment);

    void inject(PlaylistDetailFragment playlistDetailFragment);

    void inject(SongsFragment songsFragment);

    void inject(PlayControlFragment playControlFragment);

    void inject(MyMusicFragment myMusicFragment);

    void inject(LoveFragment loveFragment);

    void inject(LocalVideoFragment localVideoFragment);

    void inject(DownloadedFragment downloadedFragment);

    void inject(DiscoverFragment discoverFragment);

    void inject(PlayQueueFragment playQueueFragment);

    void inject(DownloadManagerFragment downloadManagerFragment);

    void inject(MvListFragment mvListFragment);

    void inject(@NotNull ChartsDetailFragment chartsDetailFragment);

    void inject(@NotNull QQArtistListFragment QQArtistListFragment);

    void inject(MvSearchListFragment mvSearchListFragment);

    void inject(@NotNull BindLoginActivity bindLoginActivity);

    void inject(@NotNull PlaylistFragment playlistFragment);

    void inject(@NotNull TopPlaylistFragment topPlaylistFragment);

    void inject(@NotNull ArtistInfoFragment artistInfoFragment);

    void inject(@NotNull com.cat.music.ui.music.artist.fragment.AlbumFragment albumFragment);

    void inject(@NotNull SearchSongsFragment searchSongsFragment);

    void inject(@NotNull YoutubeSearchFragment youtubeSearchFragment);

    void inject(@NotNull VideoDetailFragment videoDetailFragment);

    void inject(@NotNull VideoCommentFragment videoCommentFragment);
}
