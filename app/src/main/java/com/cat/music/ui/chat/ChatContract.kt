package com.cat.music.ui.chat

import com.cat.music.ui.base.BaseContract
import com.cyl.musiclake.bean.MessageInfoBean


interface ChatContract {

    interface View : BaseContract.BaseView {
        fun showHistortMessages(msgList: MutableList<MessageInfoBean>)
        fun showMessages(msgList: MutableList<MessageInfoBean>)
        fun deleteSuccessful()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun sendMusicMessage()
        fun loadMessages( end: String? = null)
        fun loadLocalMessages()
        fun deleteMessages()
    }
}
