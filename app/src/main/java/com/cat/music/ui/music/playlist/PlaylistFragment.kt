package com.cat.music.ui.music.playlist

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Pair
import android.view.View
import com.cat.music.R
import com.cat.music.bean.Music
import com.cat.music.bean.Playlist
import com.cat.music.common.NavigationHelper
import com.cat.music.ui.base.BaseFragment
import com.cat.music.ui.base.BaseLazyFragment
import com.cat.music.ui.music.charts.PlaylistContract
import com.cat.music.ui.music.charts.PlaylistPresenter
import com.cat.music.ui.music.discover.TopPlaylistAdapter
import kotlinx.android.synthetic.main.fragment_recyclerview_notoolbar.*

/**
 * Created by Monkey on 2015/6/29.
 */
class PlaylistFragment : BaseLazyFragment<PlaylistPresenter>(), PlaylistContract.View {

    /**
     * 适配器
     */
    private var mNeteaseAdapter: TopPlaylistAdapter? = null

    /**
     * 数据集合
     */
    private var playlist = mutableListOf<Playlist>()


    override fun showNeteaseCharts(playlistList: MutableList<Playlist>) {
        this.playlist = playlistList
        if (mNeteaseAdapter == null) {
            //适配器
            mNeteaseAdapter = TopPlaylistAdapter(playlist)
            recyclerView?.layoutManager = GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)
            recyclerView?.adapter = mNeteaseAdapter
            recyclerView?.isFocusable = false
            recyclerView?.isNestedScrollingEnabled = false
            mNeteaseAdapter?.bindToRecyclerView(recyclerView)
            mNeteaseAdapter?.setOnItemClickListener { adapter, view, position ->
                val playlist = adapter.data[position] as Playlist
                NavigationHelper.navigateToPlaylist(mFragmentComponent.activity, playlist, Pair(view.findViewById(R.id.iv_cover), getString(R.string.transition_album)))
            }
        } else {
            mNeteaseAdapter?.setNewData(playlist)
        }
    }


    override fun showPlayList(playlist: Playlist?) {
    }

    override fun showOnlineMusicList(musicList: MutableList<Music>?) {

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_notoolbar
    }

    public override fun initViews() {

    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun loadData() {
    }


    override fun listener() {

    }

    companion object {

        fun newInstance(tag: String): PlaylistFragment {
            val args = Bundle()
            val fragment = PlaylistFragment()
            args.putString("Tag", tag)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onLazyLoad() {
        val tag = arguments?.getString("Tag", "全部")
        tag?.let { mPresenter?.loadNetease(it) }
    }
}
