package com.cat.music.ui.chat

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cat.music.MusicApp
import com.cat.music.R
import com.cat.music.socket.SocketManager
import com.cat.music.ui.base.BaseActivity
import kotlinx.android.synthetic.main.card_chat_detail.*


/**
 * 消息中心，收发消息
 */
class ChatDetailActivity : BaseActivity<ChatPresenter>() {


    private var mUserAdapter: OnlineUserListAdapter? = null

    override fun setToolbarTitle(): String {
        return getString(R.string.chat_detail)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_chat_detail
    }

    override fun initView() {
        //用户头像列表
        mUserAdapter = OnlineUserListAdapter(SocketManager.onlineUsers)
        usersRsv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersRsv.adapter = mUserAdapter
        usersRsv.isNestedScrollingEnabled = false
        usersRsv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mUserAdapter?.bindToRecyclerView(usersRsv)
        onlineUserTv.text = getString(R.string.online_users,SocketManager.onlineUsers.size)
    }

    override fun initData() {

    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }

}
