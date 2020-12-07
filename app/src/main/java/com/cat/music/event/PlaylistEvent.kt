package com.cat.music.event

import com.cyl.musiclake.bean.Playlist
import com.cat.music.common.Constants

/**
 * Author   : D22434
 * version  : 2018/3/1
 * function : 歌单改变
 */
class PlaylistEvent(var type: String? = Constants.PLAYLIST_CUSTOM_ID, val playlist: Playlist? = null)
