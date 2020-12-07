package com.cat.music.ui.main;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cat.music.bean.Music;
import com.cat.music.common.Constants;
import com.cat.music.common.NavigationHelper;
import com.cat.music.event.CountDownEvent;
import com.cat.music.event.LoginEvent;
import com.cat.music.event.MetaChangedEvent;
import com.cat.music.player.PlayManager;
import com.cat.music.socket.SocketManager;
import com.cat.music.ui.base.BaseActivity;
import com.cat.music.ui.chat.ChatActivity;
import com.cat.music.ui.my.BindLoginActivity;
import com.cat.music.ui.my.LoginActivity;
import com.cat.music.ui.my.user.User;
import com.cat.music.ui.my.user.UserStatus;
import com.cat.music.utils.CountDownUtils;
import com.cat.music.utils.CoverLoader;
import com.cat.music.utils.LogUtil;
import com.cat.music.utils.SPUtils;
import com.cat.music.utils.ToastUtils;
import com.cat.music.utils.Tools;
import com.cat.musiclake.bean.Music;
import com.cat.music.common.Constants;
import com.cat.musiclake.common.NavigationHelper;
import com.cat.musiclake.event.CountDownEvent;
import com.cat.musiclake.event.LoginEvent;
import com.cat.musiclake.event.MetaChangedEvent;
import com.cat.music.player.PlayManager;
import com.cat.musiclake.socket.SocketManager;
import com.cat.music.ui.base.BaseActivity;
import com.cat.musiclake.ui.chat.ChatActivity;
import com.cat.musiclake.ui.my.BindLoginActivity;
import com.cat.music.ui.my.LoginActivity;
import com.cat.musiclake.ui.my.user.User;
import com.cat.music.ui.my.user.UserStatus;
import com.cat.musiclake.utils.CountDownUtils;
import com.cat.musiclake.utils.CoverLoader;
import com.cat.music.utils.LogUtil;
import com.cat.music.utils.SPUtils;
import com.cat.music.utils.ToastUtils;
import com.cat.musiclake.utils.Tools;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.SocketOnlineEvent;
import com.cat.music.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.event.CountDownEvent;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cat.music.player.PlayManager;
import com.cyl.musiclake.socket.SocketManager;
import com.cyl.musiclake.ui.UIUtilsKt;
import com.cat.music.ui.base.BaseActivity;
import com.cyl.musiclake.ui.chat.ChatActivity;
import com.cyl.musiclake.ui.music.importplaylist.ImportPlaylistActivity;
import com.cyl.musiclake.ui.music.search.SearchActivity;
import com.cyl.musiclake.ui.my.BindLoginActivity;
import com.cat.music.ui.my.LoginActivity;
import com.cyl.musiclake.ui.my.user.User;
import com.cat.music.ui.my.user.UserStatus;
import com.cyl.musiclake.ui.settings.AboutActivity;
import com.cyl.musiclake.ui.settings.SettingsActivity;
import com.cyl.musiclake.ui.timing.SleepTimerActivity;
import com.cyl.musiclake.ui.widget.CountDownTimerTextView;
import com.cyl.musiclake.utils.CountDownUtils;
import com.cyl.musiclake.utils.CoverLoader;
import com.cat.music.utils.LogUtil;
import com.cat.music.utils.SPUtils;
import com.cat.music.utils.ToastUtils;
import com.cyl.musiclake.utils.Tools;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.cyl.musiclake.ui.UIUtilsKt.logout;
import static com.cyl.musiclake.ui.UIUtilsKt.updateLoginToken;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    Switch mSwitchCountDown;
    CountDownTimerTextView mSwitchCountDownTv;

    public ImageView mImageView;
    CircleImageView mAvatarIcon;
    TextView mName;
    //    TextView mLoginTv;
    ImageView mShowBindIv;
    CircleImageView mBindNeteaseView;
    TextView mOnlineNumTv;

    private static final String TAG = "MainActivity";

    private boolean mIsCountDown = false;
    private boolean mIsLogin = false;

    Class<?> mTargetClass = null;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //菜单栏的头部控件初始化
        initNavView();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        disableNavigationViewScrollbars(mNavigationView);
        checkLoginStatus();
        initCountDownView();
        //检查更新
        Beta.checkUpgrade(false, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetaChangedEvent(MetaChangedEvent event) {
        updatePlaySongInfo(event.getMusic());
    }

    private void updatePlaySongInfo(Music music) {
        if (music != null) {
            CoverLoader.INSTANCE.loadBigImageView(this, music, mImageView);
        }
    }

    private void initNavView() {
        View mHeaderView = mNavigationView.getHeaderView(0);
        mImageView = mHeaderView.findViewById(R.id.header_bg);
        mAvatarIcon = mHeaderView.findViewById(R.id.header_face);
        mName = mHeaderView.findViewById(R.id.header_name);
        mBindNeteaseView = mHeaderView.findViewById(R.id.heard_netease);
        mShowBindIv = mHeaderView.findViewById(R.id.show_sync_iv);
        mShowBindIv.setOnClickListener(v -> {
            if (mNavigationView.getMenu().findItem(R.id.nav_bind_wy).isVisible()) {
                mShowBindIv.setImageResource(R.drawable.ic_arrow_drop_up);
            } else {
                mShowBindIv.setImageResource(R.drawable.ic_arrow_drop_down);
            }
        });

    }

    /**
     * 检查是否绑定网易云音乐
     *
     * @param isInit
     */
    public void checkBindNeteaseStatus(Boolean isInit, Function1<Boolean, Unit> function1) {
        UIUtilsKt.getNeteaseLoginStatus(
                user -> {
                    ToastUtils.show("已绑定网易云音乐");
                    LogUtil.d(TAG, "success " + user.getId());
                    if (isInit) {
                        User user1 = new User();
                        user1.setType(Constants.NETEASE);
                        user1.setAvatar(user.getAvatar());
                        user1.setName(user.getName());
                        EventBus.getDefault().post(new LoginEvent(true, user1));
                    }
                    if (function1 != null) {
                        function1.invoke(true);
                    }
                    return null;
                }, () -> {
                    LogUtil.d(TAG, "fail ");
                    if (!isInit) {
                        Intent intent = new Intent(MainActivity.this, BindLoginActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_LOGIN);
                    }
                    if (function1 != null) {
                        function1.invoke(false);
                    }
                    return null;
                });
    }


    @Override
    protected void initData() {
        updatePlaySongInfo(PlayManager.getPlayingMusic());
        //加载主fragment
        initShortCutsIntent();
    }

    /**
     * 初始shortCuts点击事件
     */
    private void initShortCutsIntent() {
        String action = getIntent().getAction();
        if (action != null) {
            LogUtil.d(TAG, "收到 启动ACTION  " + action);
            if (action.contains("ACTION_SEARCH")) {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            } else if (action.contains("ACTION_LOCAL")) {
            } else if (action.contains("ACTION_HISTORY")) {
            } else {
                navigateLibrary.run();
            }
        }
    }

    @Override
    protected void initInjector() {
    }


    @Override
    protected void listener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mSwitchCountDown.setChecked(CountDownUtils.INSTANCE.getType() != 0);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (mTargetClass != null) {
                    turnToActivity(mTargetClass);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 菜单条目点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_login_status:
                if (mIsLogin) {
                    UIUtilsKt.showInfoDialog(this, getString(R.string.app_name), getString(R.string.logout_prompt), () -> {
                        logout();
                        return null;
                    });
                } else {
                    mTargetClass = LoginActivity.class;
                }
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_menu_playQueue:
                NavigationHelper.INSTANCE.navigatePlayQueue(this);
                break;
            case R.id.nav_bind_wy:
                checkBindNeteaseStatus(false, null);
                break;
            case R.id.nav_menu_import:
                mTargetClass = ImportPlaylistActivity.class;
                break;
            case R.id.nav_menu_setting:
                mTargetClass = SettingsActivity.class;
                break;
            case R.id.nav_menu_online_num:
                mTargetClass = ChatActivity.class;
                break;
            case R.id.nav_menu_count_down:
                mTargetClass = SleepTimerActivity.class;
                break;
            case R.id.nav_menu_feedback:
                Tools.INSTANCE.feeback(this);
                break;
            case R.id.nav_menu_about:
                mTargetClass = AboutActivity.class;
                break;
            case R.id.nav_menu_test:
                mTargetClass = TestActivity.class;
                break;
            case R.id.nav_menu_equalizer:
                NavigationHelper.INSTANCE.navigateToSoundEffect(this);
                break;
            case R.id.nav_menu_exit:
                mTargetClass = null;
                finish();
                break;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    /**
     * 跳转Activity
     *
     * @param cls
     */
    private void turnToActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
        mTargetClass = null;
    }

    /**
     * 启动界面
     */
    private Runnable navigateLibrary = () -> {
        Fragment fragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
    };

    //返回键
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else if (isBackToDesktop()) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isBackToDesktop()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
                return true;
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 是否返回到桌面
     */
    private boolean isBackToDesktop() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return (currentFragment instanceof MainFragment);
    }

    /**
     * 设置用户状态信息
     */
    private void setUserStatusInfo(Boolean isLogin, User user) {
        mIsLogin = isLogin;
        if (mIsLogin && user != null) {
            SocketManager.INSTANCE.toggleSocket(true);
            String url = user.getAvatar();
            CoverLoader.INSTANCE.loadImageView(this, url, R.drawable.ic_account_circle, mAvatarIcon);
            mName.setText(user.getNick());
//            mShowBindIv.setVisibility(View.VISIBLE);
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle(getResources().getString(R.string.logout_hint))
                    .setIcon(R.drawable.ic_exit);
        } else {
            SocketManager.INSTANCE.toggleSocket(false);
            mAvatarIcon.setImageResource(R.drawable.ic_account_circle);
            mName.setText(getResources().getString(R.string.app_name));
//            mShowBindIv.setVisibility(View.GONE);
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle(getResources().getString(R.string.login_hint))
                    .setIcon(R.drawable.ic_exit);

            mNavigationView.getMenu().removeItem(R.id.nav_menu_online_num);
        }
    }

    /**
     * 登陆成功重新设置用户
     * 绑定成功更新menu
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LoginEvent event) {
        if (event.getUser() != null) {
            //更新绑定UI
            if (event.getStatus() && event.getUser().getType().equals(Constants.NETEASE)) {
                mNavigationView.getMenu().findItem(R.id.nav_bind_wy).setTitle("已绑定网易云音乐(" + event.getUser().getName() + ")");
                if (event.getUser().getAvatar().length() > 0) {
                    CoverLoader.INSTANCE.loadDrawable(this, event.getUser().getAvatar(), drawable -> {
                        mNavigationView.getMenu().findItem(R.id.nav_bind_wy).setIcon(drawable);
                        return null;
                    });
                }
                return;
            }
        }
        //更新用户头像
        setUserStatusInfo(event.getStatus(), event.getUser());

    }

    /**
     * 更新在线用户数量
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOnlineInfo(SocketOnlineEvent event) {
        if (mOnlineNumTv != null) {
            mOnlineNumTv.setText(String.valueOf(event.getNum()));
        }
    }

    /**
     * 检查QQ登录状态
     */
    private void checkLoginStatus() {
        if (UserStatus.getLoginStatus() && !UserStatus.getTokenStatus()) {
            updateLoginToken();
        } else if (UserStatus.getLoginStatus()) {
            updateUserInfo(new LoginEvent(true, UserStatus.getUserInfo()));
        }
        checkBindNeteaseStatus(true, null);
    }

    /**
     * 去掉navigationView的滚动条
     *
     * @param navigationView
     */
    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    /**
     * 更新歌单
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishCountDown(CountDownEvent event) {
        if (event.isStop()) {
            mSwitchCountDown.setChecked(false);
            mSwitchCountDownTv.setVisibility(View.GONE);
        } else {
            mSwitchCountDown.setChecked(true);
            mSwitchCountDownTv.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 初始化倒计时
     */
    private void initCountDownView() {
        View numItem = mNavigationView.getMenu().findItem(R.id.nav_menu_online_num).getActionView();
        mOnlineNumTv = numItem.findViewById(R.id.msg_num_tv);
        mOnlineNumTv.setText("0");
        View item = mNavigationView.getMenu().findItem(R.id.nav_menu_count_down).getActionView();
        mSwitchCountDown = item.findViewById(R.id.count_down_switch);
        mSwitchCountDownTv = item.findViewById(R.id.count_down_tv);
        mSwitchCountDown.setOnClickListener(v -> UIUtilsKt.showCountDown(MainActivity.this, checked -> {
            mSwitchCountDown.setChecked(checked);
            return null;
        }));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent");
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_LOGIN) {
            //绑定网易云音乐成功。刷新UI
            String uid = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_UID, "");
            if (uid != null && uid.length() > 0) {
                LogUtil.d(TAG, "绑定成功 uid = " + uid);
                checkBindNeteaseStatus(true, null);
            }
        }
    }

}
