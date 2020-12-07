package com.cat.music.ui.chat

import com.cat.music.MusicApp
import com.cat.music.R
import com.cat.music.api.music.MusicUtils
import com.cat.music.ui.base.BasePresenter
import com.cat.music.common.Constants
import com.cat.music.player.PlayManager
import com.cat.music.socket.SocketManager
import com.cat.music.utils.FormatUtil
import com.cat.music.utils.ToastUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/9/27 .
 */
class ChatPresenter @Inject
constructor() : BasePresenter<ChatContract.View>(), ChatContract.Presenter {

    val model by lazy { ChatModel() }
    override fun deleteMessages() {
        doAsync {
            val data = model.deleteAllMessages()
            uiThread {
                mView?.deleteSuccessful()
            }
        }
    }

    /**
     * 加载云消息,默认加载云消息
     */
    override fun loadMessages(end: String?) {
        var endTime: String? = null
        val start = end?.let {
            val time = FormatUtil.getChatParseDateTime(it)
            endTime = FormatUtil.getChatDateTime(time - 1)
            FormatUtil.getChatDateTime(time - 1000 * 60 * 60 * 24 * 2)
        }
        model.getChatHistory(start, endTime, success = {
            it?.let { it1 -> mView?.showHistortMessages(it1) }
        }, fail = {
            mView?.hideLoading()
            ToastUtils.show(it)
        })
    }

    /**
     * 加载本地消息
     */
    override fun loadLocalMessages() {
        doAsync {
            val data = model.loadHistoryMessages()
            uiThread {
                mView?.showMessages(data)
            }
        }
    }

    /**
     * 发送正在播放的音乐
     */
    override fun sendMusicMessage() {
        val music = PlayManager.getPlayingMusic()
        when {
            music?.type == Constants.LOCAL -> {
                val message = MusicApp.getAppContext().getString(R.string.share_local_song, music.artist, music.title)
                SocketManager.sendSocketMessage(message, SocketManager.MESSAGE_BROADCAST)
            }
            music?.mid != null -> {
                val message = MusicApp.GSON.toJson(MusicUtils.getMusicInfo(music))
                SocketManager.sendSocketMessage(message, SocketManager.MESSAGE_SHARE)
            }
            else -> ToastUtils.show(MusicApp.getAppContext().getString(R.string.playing_empty))
        }
    }

}