package com.cat.music.ui.music.local.fragment


import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cat.music.common.Constants
import com.cat.music.common.Extras
import com.cat.music.player.PlayManager
import com.cat.music.ui.base.BaseFragment
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.ui.music.local.adapter.SongAdapter
import com.cat.music.ui.music.local.contract.FolderSongsContract
import com.cyl.musiclake.ui.music.local.presenter.FolderSongPresenter
import com.cyl.musiclake.ui.music.mv.VideoPlayerActivity
import com.cat.music.ui.widget.ItemDecoration
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

class LocalVideoFragment : BaseFragment<FolderSongPresenter>(), FolderSongsContract.View {

    val mRecyclerView by lazy { rootView?.findViewById<RecyclerView>(R.id.recyclerView) }

    private var mAdapter: SongAdapter? = null
    private var path: String? = null
    private var musicList: List<Music> = ArrayList()

    override fun showEmptyView() {
        mAdapter?.setEmptyView(R.layout.view_song_empty, mRecyclerView)
    }

    override fun loadData() {
        showLoading()
        val name = arguments?.getString(Extras.FOLDER_NAME)
        path?.let { mPresenter?.loadSongs(it, !TextUtils.isEmpty(name)) }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview
    }

    public override fun initViews() {
        mAdapter = SongAdapter(musicList)
        mRecyclerView?.layoutManager = LinearLayoutManager(activity)
        mRecyclerView?.adapter = mAdapter
        mAdapter?.bindToRecyclerView(mRecyclerView)
        setHasOptionsMenu(true)
    }

    override fun getToolBarTitle(): String? {
        if (arguments != null) {
            path = arguments?.getString(Extras.FOLDER_PATH)
            val name = arguments?.getString(Extras.FOLDER_NAME)
            if (!TextUtils.isEmpty(name)) {
                return name
            }
        }
        return context?.getString(R.string.item_video)
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun listener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (view.id != R.id.iv_more) {
                if (Constants.VIDEO == musicList[position].type) {
                    startActivity<VideoPlayerActivity>(Extras.VIDEO_PATH to musicList[position].uri,
                            Extras.MV_TITLE to musicList[position].title)
                } else {
                    PlayManager.play(position, musicList, Constants.PLAYLIST_LOCAL_ID)
                }
            }
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position -> BottomDialogFragment.newInstance(musicList[position]).show(mFragmentComponent.activity as AppCompatActivity) }
    }

    override fun showSongs(musicList: List<Music>) {
        this.musicList = musicList
        mAdapter?.setNewData(musicList)
        hideLoading()
    }

    companion object {
        fun newInstance(path: String): LocalVideoFragment {
            val args = Bundle()
            args.putString(Extras.FOLDER_PATH, path)
            args.putString(Extras.FOLDER_NAME, path)
            val fragment = LocalVideoFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(path: String, name: String): LocalVideoFragment {
            val args = Bundle()
            args.putString(Extras.FOLDER_PATH, path)
            args.putString(Extras.FOLDER_NAME, name)
            val fragment = LocalVideoFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
