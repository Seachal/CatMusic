package com.cat.music.ui.music.playpage

import com.cat.music.ui.base.BasePresenter
import com.cat.music.bean.Music
import com.cat.music.player.MusicPlayerService
import com.cat.music.player.playback.PlayProgressListener
import com.cat.music.utils.CoverLoader
import com.cat.music.utils.ImageUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter, PlayProgressListener {
    override fun onProgressUpdate(position: Long, duration: Long) {
        mView?.updateProgress(position, duration)
    }

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        MusicPlayerService.addProgressListener(this)
    }

    override fun detachView() {
        super.detachView()
        MusicPlayerService.removeProgressListener(this)
    }

    override fun updateNowPlaying(music: Music?, isInit: Boolean?) {
        mView?.showNowPlaying(music)
        CoverLoader.loadBigImageView(mView?.context, music) { bitmap ->
            doAsync {
                val blur = ImageUtils.createBlurredImageFromBitmap(bitmap, 10)
                uiThread {
                    mView?.setPlayingBg(blur, isInit)
                    mView?.setPlayingBitmap(bitmap)
                }
            }
        }
    }
}
