package com.cat.music.ui.music.my

import com.cat.music.ui.base.BaseContract
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.NoticeInfo
import com.cyl.musiclake.bean.Playlist

interface MyMusicContract {

    interface View : BaseContract.BaseView {

        fun showSongs(songList: MutableList<Music>)

        fun showVideoList(videoList: MutableList<Music>)

        fun showLocalPlaylist(playlists: MutableList<Playlist>)

        fun showPlaylist(playlists: MutableList<Playlist>)

        fun showWyPlaylist(playlists: MutableList<Playlist>)

        fun showHistory(musicList: MutableList<Music>)

        fun showLoveList(musicList: MutableList<Music>)

        fun showDownloadList(musicList: MutableList<Music>)

        fun showNoticeInfo(notice: NoticeInfo)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSongs()

        fun loadPlaylist(playlist: Playlist? = null)
    }
}
