package com.cat.music.ui.music.bottom

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cat.music.R
import com.cat.music.api.music.MusicApi
import com.cat.music.bean.Music
import com.cat.music.common.NavigationHelper
import com.cat.music.utils.ConvertUtils
import com.cat.music.utils.CoverLoader


/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class BottomMusicAdapter(musicList: List<Music>) : BaseQuickAdapter<Music, BaseViewHolder>(R.layout.item_bottom_music, musicList) {
    override fun convert(holder: BaseViewHolder, item: Music) {
        CoverLoader.loadImageView(mContext, item.coverUri, holder.getView(R.id.iv_cover))
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))
        //设置歌手专辑名
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.artist, item.album))

        if (item.coverUri != null) {
            CoverLoader.loadImageView(mContext, item.coverUri, R.drawable.default_cover, holder.getView(R.id.iv_cover))
        }
        if (item.coverUri.isNullOrEmpty()) {
            //加载歌曲专辑图
            item.title?.let {
                MusicApi.getMusicAlbumPic(item.title.toString(), success = {
                    item.coverUri = it
                    CoverLoader.loadImageView(mContext, it, R.drawable.default_cover, holder.getView(R.id.iv_cover))
                })
            }
        }
        holder.itemView.setOnClickListener {
            NavigationHelper.navigateToPlaying(mContext as Activity, holder.getView(R.id.iv_cover))
        }
    }
}