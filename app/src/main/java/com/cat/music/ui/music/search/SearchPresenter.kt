package com.cat.music.ui.music.search

import com.cyl.musicapi.netease.SearchInfo
import com.cat.music.MusicApp
import com.cat.music.api.music.MusicApiServiceImpl
import com.cat.music.api.music.baidu.BaiduApiServiceImpl
import com.cat.music.api.music.netease.NeteaseApiServiceImpl
import com.cat.music.api.net.ApiManager
import com.cat.music.api.net.RequestCallBack
import com.cat.music.api.youtube.YoutubeDataApi
import com.cat.music.bean.HotSearchBean
import com.cat.music.bean.Music
import com.cat.music.bean.SearchResult
import com.cat.music.data.db.DaoLitepal
import com.cat.music.common.Constants
import com.cat.music.ui.base.BasePresenter
import com.cat.music.utils.LogUtil
import com.google.api.services.youtube.model.SearchListResponse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by yonglong on 2018/1/6.
 */

class SearchPresenter @Inject
constructor() : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    /**
     * 搜索Youtube返回的结果
     */
    fun searchByYouTube(query: String, pageToken: String, success: (SearchResult) -> Unit, fail: () -> Unit) {
        doAsync {
            val startTime = System.currentTimeMillis()
            val result: SearchListResponse? = YoutubeDataApi.search(query, pageToken)
            uiThread {
                result?.let {
                    val searchResult = SearchResult()
                    val songs = mutableListOf<Music>()
                    result.items.mapTo(songs) { t ->
                        val song = Music()
                        song.title = t.snippet.title
                        song.artist = t.snippet.channelTitle
                        song.artistId = t.snippet.channelId
                        song.mid = t.id.videoId
                        song.coverUri = t.snippet.thumbnails.high.url
                        song.type = Constants.YOUTUBE
                        song
                    }
                    searchResult.nextPager = result.nextPageToken
                    searchResult.songs = songs
                    searchResult.duration = System.currentTimeMillis() - startTime
                    success(searchResult)
                    mView?.hideLoading()
                }
                if (result == null) {
                    fail.invoke()
                }
            }
        }
    }

    override fun searchByType(key: String, offset: Int, type: Int) {
        ApiManager.request(NeteaseApiServiceImpl.searchMoreInfo(key, 10, offset, type),
                object : RequestCallBack<SearchInfo> {
                    override fun success(result: SearchInfo) {
                        when (type) {
                            1004 -> {

                            }
                            100 -> {

                            }
                            1000 -> {

                            }
                        }
                    }

                    override fun error(msg: String) {
                    }
                })
    }

    override fun searchLocal(key: String) {
        mView?.showLoading()
        doAsync {
            val result = DaoLitepal.searchLocalMusic(key)
            uiThread {
                mView?.hideLoading()
                mView?.showSearchResult(result)
            }
        }
    }

    override fun search(key: String, type: SearchEngine.Filter, limit: Int, page: Int) {
        LogUtil.e("SearchPresenter", "key :$key type:$type limit:$limit page:$page")
        mView?.showLoading()
        val observable = when (type) {
            SearchEngine.Filter.BAIDU -> BaiduApiServiceImpl.searchBaiduMusic(key, limit, page)
            else -> MusicApiServiceImpl.searchMusic(key, type, limit, page)
        }
        ApiManager.request(observable,
                object : RequestCallBack<MutableList<Music>> {
                    override fun success(result: MutableList<Music>) {
                        LogUtil.e("searchSuccess", result.toString())
                        mView?.hideLoading()
                        mView?.showSearchResult(result)
                    }

                    override fun error(msg: String) {
                        LogUtil.e("searchFail", msg)
                        mView?.hideLoading()
                        mView?.showSearchResult(mutableListOf())
                    }
                })
    }

    override fun getHotSearchInfo() {
        if (MusicApp.hotSearchList != null) {
            mView?.showHotSearchInfo(MusicApp.hotSearchList)
            return
        } else {
            ApiManager.request(NeteaseApiServiceImpl.getHotSearchInfo(), object : RequestCallBack<MutableList<HotSearchBean>> {
                override fun success(result: MutableList<HotSearchBean>?) {
                    result?.let {
                        MusicApp.hotSearchList = it
                        mView?.showHotSearchInfo(it)
                    }
                }

                override fun error(msg: String?) {
                }
            })
        }
    }

    override fun getSearchHistory() {
        doAsync {
            val data = DaoLitepal.getAllSearchInfo()
            uiThread {
                mView?.showSearchHistory(data)
            }
        }
    }

    override fun saveQueryInfo(query: String) {
        DaoLitepal.addSearchInfo(query)
    }
}
