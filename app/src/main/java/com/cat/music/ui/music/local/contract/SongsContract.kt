package com.cat.music.ui.music.local.contract

import com.cat.music.ui.base.BaseContract
import com.cyl.musiclake.bean.Music

interface SongsContract {

    interface View : BaseContract.BaseView {
        fun showSongs(songList: MutableList<Music>)

        fun setEmptyView()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSongs(isReload: Boolean)
    }
}
