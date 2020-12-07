package com.cat.music.ui.download

import android.text.TextUtils
import com.cat.music.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.bean.Music
import com.cat.music.utils.LogUtil
import com.cyl.musiclake.utils.Mp3Util
import com.cat.music.utils.ToastUtils
import com.liulishuo.filedownloader.util.FileDownloadUtils
import org.litepal.LitePal

object DownloadLoader {
    private val TAG = "PlayQueueLoader"

    /**
     * 获取已下载列表
     */
    fun getDownloadList(isCached: Boolean = false): MutableList<Music> {
        val musicList = mutableListOf<Music>()
        val data = LitePal.where("finish = 1 and cache = ?", if (isCached) "1" else "0").find(TasksManagerModel::class.java)
        data.forEach {
            val music = it.mid?.let { it1 ->
                try {
                    DaoLitepal.getMusicInfo(it1)
                } catch (e: Throwable) {
                    null
                }
            }
//            music?.forEach { origin ->
//                if (origin.uri != null || origin.uri?.startsWith("http:")!!) {
//                    origin.uri = it.path
//                    origin.isOnline = false
//                }
//                SongLoader.updateMusic(music = origin)
//            }
            music?.let { it1 -> musicList.add(it1) }
        }
        return musicList
    }

    /**
     * 获取已下载列表
     */
    fun getDownloadList(): MutableList<Music> {
        val musicList = mutableListOf<Music>()
        val data = LitePal.where("finish = 1").find(TasksManagerModel::class.java)
        data.forEach {
            val music = it.mid?.let { it1 ->
                try {
                    DaoLitepal.getMusicInfo(it1)
                } catch (e: Throwable) {
                    null
                }
            }
//            music?.forEach { origin ->
//                if (origin.uri != null || origin.uri?.startsWith("http:")!!) {
//                    origin.uri = it.path
//                    origin.isOnline = false
//                }
//                SongLoader.updateMusic(music = origin)
//            }
            music?.let { it1 -> musicList.add(it1) }
        }
        return musicList
    }

    /**
     * 获取下载列表
     */
    fun getDownloadingList(): MutableList<TasksManagerModel> {
        return LitePal.where("finish = 0").find(TasksManagerModel::class.java)
    }

    /**
     * 清空下载列表
     */
    fun clearDownloadList(): MutableList<Music> {
        LitePal.deleteAll(TasksManagerModel::class.java)
        return getDownloadList()
    }

    /**
     * 是否已在下载列表
     */
    fun isHasMusic(mid: String?): Boolean {
        return LitePal.isExist(TasksManagerModel::class.java, "mid = ?", mid)
    }

    fun addTask(tid: Int, mid: String?, name: String?, url: String?, path: String, cached: Boolean): TasksManagerModel? {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null
        }
        //判断是否已下载过
        if (!cached && isHasMusic(mid)) {
            ToastUtils.show(MusicApp.getAppContext().getString(R.string.download_exits, name))
            return null
        }
        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        val id = FileDownloadUtils.generateId(url, path)
        val model = TasksManagerModel()
        model.tid = id
        model.mid = mid
        model.name = name
        model.url = url
        model.path = path
        model.finish = false
        model.cache = cached
        model.saveOrUpdate("tid = ?", tid.toString())
        return model
    }

    /**
     * 更新数据库下载任务状态
     */
    fun updateTask(tid: Int) {
        val model = LitePal.where("tid = ?", tid.toString()).findFirst(TasksManagerModel::class.java)
        val music = model.mid?.let { DaoLitepal.getMusicInfo(it) }
        model.finish = true
        model.saveOrUpdate("tid = ?", tid.toString())
        //更新mp3文件标签
        music?.let {
            model.path?.let { it1 ->
                LogUtil.e(it1)
                Mp3Util.updateTagInfo(it1, music)
                Mp3Util.getTagInfo(it1)
            }
        }
    }
}
