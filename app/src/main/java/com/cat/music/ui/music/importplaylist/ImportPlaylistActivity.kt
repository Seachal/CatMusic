package com.cat.music.ui.music.importplaylist

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.cat.music.R
import com.cat.music.api.music.MusicApiServiceImpl
import com.cat.music.api.net.ApiManager
import com.cat.music.api.net.RequestCallBack
import com.cat.music.bean.Music
import com.cat.music.bean.Playlist
import com.cat.music.common.Constants
import com.cat.music.common.NavigationHelper
import com.cat.music.player.PlayManager
import com.cat.music.ui.music.edit.PlaylistManagerUtils
import com.cat.music.ui.base.BaseActivity
import com.cat.music.ui.base.BaseContract
import com.cat.music.ui.base.BasePresenter
import com.cat.music.ui.music.dialog.BottomDialogFragment
import com.cat.music.ui.music.local.adapter.SongAdapter
import com.cat.music.utils.LogUtil
import com.cat.music.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_import_playlist.*


@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class ImportPlaylistActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {

    var mAdapter: SongAdapter? = null
    var name: String? = null
    var vendor: String? = null
    var musicList = mutableListOf<Music>()

    override fun getLayoutResID(): Int {
        return R.layout.activity_import_playlist
    }

    override fun setToolbarTitle(): String {
        return getString(R.string.import_playlist)
    }

    override fun initView() {
    }

    override fun initData() {
        if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if (Constants.TEXT_PLAIN == intent.type) {
                val title = intent?.getStringExtra(Intent.EXTRA_TEXT)
                playlistInputView.editText?.setText(title)
            }
        }
    }

    override fun initInjector() {
    }

    override fun listener() {
        super.listener()
        //同步歌单
        syncBtn.setOnClickListener {
            showLoading(true)
            val playlistLink = playlistInputView.editText?.text.toString()
            getPlaylistId(playlistLink)
        }
        //导入歌单
        importBtn.setOnClickListener {
            if (name == null) {
                ToastUtils.show("请先同步获取歌曲！")
                return@setOnClickListener
            }
            if (vendor == null) {
                ToastUtils.show("请先同步获取歌曲！")
                return@setOnClickListener
            }
            if (musicList.size == 0) return@setOnClickListener
            PlaylistManagerUtils.addToPlaylist(this, musicList)
        }
    }

    private fun getPlaylistId(link: String) {
        try {
            when {
                link.contains("music.163.com") -> {
                    val len = link.lastIndexOf("playlist/") + "playlist/".length
                    val id = link.substring(len, len + link.substring(len).indexOf("/"))
                    importMusic(Constants.NETEASE, id)
                }
                link.contains("y.qq.com") -> {
                    val len = link.lastIndexOf("id=") + "id=".length

                    val idString = link.substring(len)
                    val id = if (idString.indexOf("&") > 0) {
                        link.substring(len, len + link.substring(len).indexOf("&")).trim()
                    } else {
                        idString
                    }
                    importMusic(Constants.QQ, id)
                }
                link.contains("www.xiami.com") -> {
                    val len = link.lastIndexOf("collect/") + "collect/".length
                    val end = if (link.indexOf("?") == -1) link.indexOf("(") else link.indexOf("?")
                    val id = link.substring(len, end).trim()
                    importMusic(Constants.XIAMI, id)
                }
                link.contains("h.xiami.com") -> {
                    val start = link.lastIndexOf("id=") + "id=".length
                    val end = link.substring(start).indexOf(" ")
                    val id = link.substring(start, start + end).trim()
                    importMusic(Constants.XIAMI, id)
                }
                else -> {
                    showLoading(false)
                    ToastUtils.show("请输入有效的链接！")
                }
            }
        } catch (e: Throwable) {
            showLoading(false)
            ToastUtils.show("请输入有效的链接！")
        }
    }

    private fun importMusic(vendor: String, pid: String) {
        this.vendor = vendor
        LogUtil.d(TAG, "正在导入歌单 $vendor $pid")
        val observable = MusicApiServiceImpl.getPlaylistSongs(vendor, pid)
        ApiManager.request(observable, object : RequestCallBack<Playlist> {
            override fun success(result: Playlist) {
                showLoading(false)
                musicList.clear()
                musicList = result.musicList
                showResultAdapter(result)
            }

            override fun error(msg: String) {
                showLoading(false)
                ToastUtils.show(msg)
            }
        })
    }

    fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * 显示结果
     */
    fun showResultAdapter(result: Playlist) {
        mAdapter = SongAdapter(result.musicList)
        this.name = result.name
        resultRsv.adapter = mAdapter
        resultRsv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        mAdapter?.bindToRecyclerView(resultRsv)

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, result.musicList, Constants.PLAYLIST_DOWNLOAD_ID + result.pid)
                mAdapter?.notifyDataSetChanged()
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            BottomDialogFragment.newInstance(result.musicList[position], Constants.PLAYLIST_IMPORT_ID).show(this@ImportPlaylistActivity)
        }

    }

}
