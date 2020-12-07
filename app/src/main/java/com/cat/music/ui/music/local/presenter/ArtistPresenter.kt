package com.cat.music.ui.music.local.presenter

import com.cat.music.ui.base.BasePresenter
import com.cat.music.data.SongLoader
import com.cat.music.ui.music.local.contract.ArtistContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/10.
 */

class ArtistPresenter @Inject
constructor() : BasePresenter<ArtistContract.View>(), ArtistContract.Presenter {

    override fun loadArtists(action: String) {
        mView?.showLoading()
        doAsync {
            val data = SongLoader.getAllArtists()
            uiThread {
                mView?.hideLoading()
                if (data.size > 0) {
                    mView?.showArtists(data)
                } else {
                    mView?.showEmptyView()
                }
            }
        }
    }
}
