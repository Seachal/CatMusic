package com.cat.music.ui.music.mv

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.netease.MvInfoDetail
import com.cat.music.R
import com.cat.music.api.music.netease.NeteaseApiServiceImpl
import com.cat.music.api.net.ApiManager
import com.cat.music.api.net.RequestCallBack
import com.cat.music.bean.VideoInfoBean
import com.cat.music.player.exoplayer.ExoPlayerManager
import com.cat.music.utils.CoverLoader
import com.cat.music.utils.FormatUtil
import com.cat.music.utils.LogUtil
import com.google.android.exoplayer2.ui.PlayerView
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class TopMvListAdapter(list: List<MvInfoDetail>) : BaseQuickAdapter<MvInfoDetail, BaseViewHolder>(R.layout.item_mv_list, list) {

    override fun convert(helper: BaseViewHolder, detail: MvInfoDetail) {
        helper.setText(R.id.tv_title, detail.name)
        if (helper.adapterPosition > 2) {
            helper.setTextColor(R.id.tv_num, Color.WHITE)
        } else {
            helper.setTextColor(R.id.tv_num, Color.RED)
        }
        helper.setText(R.id.tv_num, (helper.adapterPosition + 1).toString())
        helper.setText(R.id.tv_playCount, "播放次数：" + FormatUtil.formatPlayCount(detail.playCount))
        CoverLoader.loadImageView(mContext, detail.cover, helper.getView<ImageView>(R.id.iv_cover))
    }
}

/**
 *
 */
class VideoListAdapter(list: List<VideoInfoBean>) : BaseQuickAdapter<VideoInfoBean, BaseViewHolder>(R.layout.item_video_list, list) {

    var playIndex = -1

    override fun convert(helper: BaseViewHolder, video: VideoInfoBean) {
        helper.setText(R.id.tv_title, video.title)

        val praiseCount = video.praisedCount
        val commentCount = video.commentCount
        val shareCount = video.shareCount
        helper.setText(R.id.videoPraisedCountTv, praiseCount.toString())
        helper.setText(R.id.videoCommentCountTv, commentCount.toString())
        helper.setText(R.id.videoCommentCountTv, shareCount.toString())

        helper.setText(R.id.tv_playCount, FormatUtil.formatPlayCount(video.playCount))

        helper.setVisible(R.id.exo_artwork, true)

        CoverLoader.loadDrawable(mContext, video.coverUrl) { drawable ->
            helper.getView<PlayerView>(R.id.videoView).defaultArtwork = drawable
        }

        CoverLoader.loadImageView(mContext, video.coverUrl, R.drawable.default_cover, helper.getView(R.id.videoCoverIv))
        LogUtil.d(TAG, "${playIndex != helper.adapterPosition} artwork可见 = ${helper.getView<ImageView>(R.id.exo_artwork).visibility == View.VISIBLE}")
        if (playIndex != helper.adapterPosition) {
            helper.getView<PlayerView>(R.id.videoView).player = null
            helper.setVisible(R.id.tv_playCount, true)
            helper.setVisible(R.id.videoView, false)
        } else {
            helper.setVisible(R.id.videoView, true)
            helper.setVisible(R.id.tv_playCount, false)
        }

        if (video.artist.size > 0) {
            val artist = video.artist[0]
            helper.setText(R.id.tv_artist, artist.name)
            CoverLoader.loadImageView(mContext, artist.picUrl, R.drawable.default_cover, helper.getView<CircleImageView>(R.id.civ_cover))
        }

        helper.getView<View>(R.id.frameLayout).setOnClickListener {
            if (TextUtils.isEmpty(video.url)) {
                loadVideoList(helper.getView(R.id.videoView), video)
            } else {
                ExoPlayerManager.setDataSource(video.url)
                ExoPlayerManager.bindView(helper.getView(R.id.videoView))
            }
            playIndex = helper.adapterPosition
            updateVideoInfo()
        }
    }

    private fun loadVideoList(videoView: PlayerView, video: VideoInfoBean) {
        val observable = NeteaseApiServiceImpl.getVideoUrlInfo(video.type, video.vid)
        ApiManager.request(observable, object : RequestCallBack<String> {
            override fun success(result: String) {
                video.url = result
                ExoPlayerManager.setDataSource(result)
                ExoPlayerManager.bindView(videoView)
            }

            override fun error(msg: String) {
                ExoPlayerManager.bindView(videoView)
            }
        })
    }

    /**
     * 适配器内部监听刷新item，外部不需要调用刷新机制
     */
    fun updateVideoInfo() {
        notifyDataSetChanged()
    }

}