package com.cat.music.ui.music.artist.presenter

import com.cat.music.ui.base.BasePresenter
import com.cat.music.bean.Music
import com.cat.music.ui.music.artist.contract.ArtistInfoContract

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class ArtistInfoPresenter @Inject
constructor() : BasePresenter<ArtistInfoContract.View>(), ArtistInfoContract.Presenter {

    override fun loadArtistInfo(music: Music) {
        val info = music.title + "-" + music.artist

    }
}
