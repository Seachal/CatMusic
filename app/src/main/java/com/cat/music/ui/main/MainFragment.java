package com.cat.music.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cat.music.ui.base.BaseFragment;
import com.cat.music.ui.base.BaseFragment;
import com.cyl.musiclake.R;
import com.cat.music.ui.base.BaseFragment;
import com.cyl.musiclake.ui.music.charts.fragment.ChartsDetailFragment;
import com.cyl.musiclake.ui.music.discover.DiscoverFragment;
import com.cyl.musiclake.ui.music.local.fragment.FoldersFragment;
import com.cyl.musiclake.ui.music.my.MyMusicFragment;
import com.cyl.musiclake.ui.music.video.VideoSquareFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public class MainFragment extends BaseFragment {
    @BindView(R.id.m_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        if (getActivity() != null) {
            mToolbar.setTitle(R.string.app_name);
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            final ActionBar toggle = appCompatActivity.getSupportActionBar();
            if (toggle != null) {
                toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white);
                toggle.setDisplayHomeAsUpEnabled(true);
            }
        }
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 3) {
//                    mTabLayout.setVisibility(View.GONE);
//                    mToolbar.setTitle("音乐MV");
//                } else {
//                    mTabLayout.setVisibility(View.VISIBLE);
//                    mToolbar.setTitle("音乐湖");
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void initInjector() {

    }


    private void setupViewPager(ViewPager mViewPager) {
        PageAdapter mAdapter = new PageAdapter(getChildFragmentManager());
        mAdapter.addFragment(MyMusicFragment.Companion.newInstance(), getContext().getString(R.string.my));
        mAdapter.addFragment(DiscoverFragment.Companion.newInstance(), getContext().getString(R.string.discover));
        mAdapter.addFragment(ChartsDetailFragment.Companion.newInstance(), getContext().getString(R.string.charts));
        mAdapter.addFragment(VideoSquareFragment.Companion.newInstance(), getContext().getString(R.string.video_title));
        mAdapter.addFragment(FoldersFragment.Companion.newInstance(), getString(R.string.folder_title));
        mViewPager.setAdapter(mAdapter);
    }

}