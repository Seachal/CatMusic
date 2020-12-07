package com.cat.music.ui.music.charts.activity

import android.os.Build
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cat.music.R
import com.cat.music.bean.Music
import com.cat.music.bean.Playlist
import com.cat.music.player.PlayManager
import com.cat.music.ui.base.BaseActivity
import com.cat.music.ui.music.charts.PlaylistContract
import com.cat.music.ui.music.charts.PlaylistPresenter
import com.cat.music.ui.music.dialog.BottomDialogFragment
import com.cat.music.ui.music.edit.EditSongListActivity
import com.cat.music.ui.music.local.adapter.SongAdapter
import com.cat.music.ui.music.search.PlaylistSearchActivity
import com.cat.music.utils.CoverLoader
import kotlinx.android.synthetic.main.activity_chart_playlist.*
import org.jetbrains.anko.startActivity

/**
 * 作者：yonglong on 2016/8/24 10:43
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
abstract class BasePlaylistActivity : BaseActivity<PlaylistPresenter>(), PlaylistContract.View {

    var mPlaylist: Playlist? = null
    var mAdapter: SongAdapter? = null
    var musicList = mutableListOf<Music>()

    var mOffset = 0
    var mCurrentCounter = 0
    var TOTAL_COUNTER = 0
    val limit = 10

    abstract fun getToolBarTitle(): String?

    override fun getLayoutResID(): Int {
        return R.layout.activity_chart_playlist
    }

    abstract fun getmPlaylist(): Playlist?

    override fun initView() {
        mAdapter = SongAdapter(musicList)
        mAdapter?.setEnableLoadMore(setEnableMore())
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = mAdapter
        mAdapter?.bindToRecyclerView(recyclerView)
    }

    override fun setToolbarTitle(): String? {
        return getToolBarTitle()
    }

    abstract fun setEnableMore(): Boolean

    override fun initData() {
        showHeaderInfo(getmPlaylist())
    }


    override fun initInjector() {
        mActivityComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { _, view, position ->
            if (view.id != R.id.iv_more) {
                PlayManager.play(position, musicList, mPlaylist?.name + mPlaylist?.pid)
            }
        }
        mAdapter?.setOnItemChildClickListener { _, _, position ->
            val music = musicList[position]
            BottomDialogFragment.newInstance(music, mPlaylist?.type).show(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_batch -> {
                EditSongListActivity.musicList = musicList
                startActivity<EditSongListActivity>()
            }
            R.id.action_search -> {
                PlaylistSearchActivity.musicList = musicList
                startActivity<PlaylistSearchActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_playlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showHeaderInfo(playlist: Playlist?) {
        if (playlist != null) {
            CoverLoader.loadBitmap(this, playlist.coverUrl) {
                coverIv.setImageBitmap(it)
                coverBgIv.setImageDrawable(CoverLoader.createBlurredImageFromBitmap(it))
            }
            //接口未返回date，暂时注释
//            if (playlist.date != 0L) {
//                dateTv.visibility = View.VISIBLE
//                dateTv.text = getString(R.string.recent_update, FormatUtil.distime(playlist.date))
//            } else {
//                dateTv.visibility = View.GONE
//            }
            playlist.des?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    descTv.text = Html.fromHtml(playlist.des, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    descTv.text = Html.fromHtml(playlist.des)
                }
            }
        }
    }

    override fun showPlayList(playlist: Playlist) {
        mAdapter?.setEnableLoadMore(false)
        mPlaylist = playlist
        musicList = playlist.musicList
        showHeaderInfo(playlist)
        mAdapter?.setNewData(playlist.musicList)
    }

    override fun showOnlineMusicList(musicList: MutableList<Music>) {
        this.musicList.addAll(musicList)
        mAdapter?.setNewData(this.musicList)
        mOffset += limit
        mCurrentCounter = mAdapter?.data?.size ?: 0
        TOTAL_COUNTER = mOffset
        mAdapter?.loadMoreComplete()
    }

    override fun showNeteaseCharts(playlistList: MutableList<Playlist>?) {

    }

}
