package com.cat.music.ui.music.playlist.detail

import com.cat.music.bean.Album
import com.cat.music.bean.Artist
import com.cat.music.bean.Music
import com.cat.music.bean.Playlist
import com.cat.music.ui.base.BaseContract

interface PlaylistDetailContract {

    interface View : BaseContract.BaseView {

        fun showPlaylistSongs(songList: MutableList<Music>?)

        fun showTitle(title: String) {}

        fun showCover(cover: String) {}

        fun showDescInfo(title: String) {}

        fun removeMusic(position: Int) {}

        fun success(type: Int)

        fun showEmptyView(msg: String)
        //显示歌单异常，提示
        fun showErrorTips(msg: String, hasTry: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadPlaylistSongs(playlist: Playlist)

        fun loadArtistSongs(artist: Artist)

        fun loadAlbumSongs(album: Album)

        fun deletePlaylist(playlist: Playlist)

        fun renamePlaylist(playlist: Playlist, title: String)
    }
}
