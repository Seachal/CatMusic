package com.cat.music.ui.download.ui


import com.cat.music.ui.base.BaseContract
import com.cat.music.bean.Music
import com.cat.music.ui.download.TasksManagerModel

interface DownloadContract {

    interface View : BaseContract.BaseView {
        fun showErrorInfo(msg: String)

        fun showSongs(musicList: List<Music>)

        fun showDownloadList(modelList: List<TasksManagerModel>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadDownloadMusic(isCache: Boolean)

        fun loadDownloading()
    }
}
