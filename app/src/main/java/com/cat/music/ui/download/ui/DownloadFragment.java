package com.cat.music.ui.download.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.cat.music.bean.Music;
import com.cat.music.common.Constants;
import com.cat.music.ui.UIUtils;
import com.cat.music.ui.UIUtilsKt;
import com.cat.music.ui.base.BaseLazyFragment;
import com.cat.music.ui.main.PageAdapter;
import com.cat.music.common.Constants;
import com.cat.music.ui.base.BaseLazyFragment;
import com.cat.music.ui.main.PageAdapter;
import com.cat.music.ui.web.WebAppActivity;
import com.cat.music.utils.ClipBoardUtil;
import com.cat.music.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cat.music.R;
import com.cat.music.ui.base.BaseLazyFragment;
import com.cat.music.common.Constants;
import com.cat.music.ui.main.PageAdapter;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadFragment extends BaseLazyFragment {
    @BindView(R.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.btn_download)
    Button mBtnDownLoad;

    public static DownloadFragment newInstance(Boolean isCache) {
        Bundle args = new Bundle();
        DownloadFragment fragment = new DownloadFragment();
        args.putBoolean(Constants.KEY_IS_CACHE, isCache);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_download;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getResources().getString(R.string.item_download));
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() == null) {
                    return;
                }
                getContext().startActivity(new Intent(getContext(), WebAppActivity.class));
            }
        });
        mBtnDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ClipBoardUtil.paste();
                if (TextUtils.isEmpty(url)) {
                    ToastUtils.show("音乐外链为空!");
                } else {
                    if (getActivity() instanceof AppCompatActivity) {
                        AppCompatActivity activity = (AppCompatActivity) getActivity();
                        Music music = new Music();
                        music.setUri(url);
                        music.setType(Constants.NETEASE);
                        music.setMid(new Date().getTime()+"");
                        music.setId(new Date().getTime());
                        music.setFileName(new Date().getTime()+"");
                        UIUtilsKt.downloadMusic(activity, music, true);
                    } else {
                        ToastUtils.show("getActivity 不是 AppCompatActivity!");
                    }

                }
            }
        });
    }

    @Override
    protected void initInjector() {

    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
//        adapter.addFragment(DownloadedFragment.newInstance(true), getString(R.string.cache_complete));
        adapter.addFragment(DownloadedFragment.Companion.newInstance(false), getString(R.string.download_complete));
        adapter.addFragment(DownloadManagerFragment.Companion.newInstance(), getString(R.string.download_processing));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onLazyLoad() {
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
    }
}
