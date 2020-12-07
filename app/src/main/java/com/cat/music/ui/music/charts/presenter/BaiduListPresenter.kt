package com.cat.music.ui.music.charts.presenter

import com.cyl.musiclake.api.music.baidu.BaiduApiServiceImpl
import com.cat.music.ui.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cat.music.api.net.ApiManager
import com.cat.music.api.net.RequestCallBack
import com.cat.music.ui.music.charts.contract.BaiduListContract

import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class BaiduListPresenter @Inject
constructor() : BasePresenter<BaiduListContract.View>(), BaiduListContract.Presenter {

    override fun loadOnlineMusicList(type: String, limit: Int, mOffset: Int) {
        ApiManager.request(BaiduApiServiceImpl.getOnlineSongs(type, limit, mOffset), object : RequestCallBack<MutableList<Music>> {
            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showErrorInfo(msg)
            }

            override fun success(result: MutableList<Music>?) {
                result?.forEach {
                    if (it.isCp)
                        result.remove(it)
                }
                mView?.showOnlineMusicList(result)
                mView?.hideLoading()
            }

        })
    }
}
