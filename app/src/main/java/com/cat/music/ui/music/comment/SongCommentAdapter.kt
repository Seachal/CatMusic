package com.cat.music.ui.music.comment

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.bean.SongComment
import com.cat.music.R
import com.cat.music.utils.CoverLoader
import com.cat.music.utils.FormatUtil

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class SongCommentAdapter(list: List<SongComment>) : BaseQuickAdapter<SongComment, BaseViewHolder>(R.layout.item_comment, list) {

    override fun convert(helper: BaseViewHolder, item: SongComment) {
        helper.setText(R.id.tv_comment_user, item.nick)
        helper.setText(R.id.tv_comment_time, FormatUtil.formatDate(item.time))
        helper.setText(R.id.tv_comment_content, item.content)
        CoverLoader.loadImageView(mContext, item.avatarUrl, helper.getView(R.id.civ_cover))
    }

}