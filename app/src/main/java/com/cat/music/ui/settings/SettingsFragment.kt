package com.cat.music.ui.settings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.preference.*
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.cat.music.Config
import com.cat.music.MusicApp
import com.cat.music.R
import com.cat.music.common.Constants
import com.cat.music.socket.SocketManager
import com.cat.music.ui.base.BaseActivity
import com.cat.music.ui.main.MainActivity
import com.cat.music.ui.theme.ThemeStore
import com.cat.music.utils.*
import com.cat.music.utils.*

/**
 * Author   : D22434
 * version  : 2018/3/8
 * function :
 */
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    private var mPreferenceDownloadFile: Preference? = null
    private var mPreferenceCacheFile: Preference? = null
    private var mPreferenceCache: PreferenceScreen? = null
    private var mWifiSwitch: SwitchPreference? = null
    private var mSocketSwitch: SwitchPreference? = null
    private var mNightSwitch: ListPreference? = null
    private var mCacheSwitch: SwitchPreference? = null
    private var mLyricCheckBox: CheckBoxPreference? = null
    private var mMusicQualityPreference: ListPreference? = null
    private var mMusicApiPreference: EditTextPreference? = null
    private var mNeteaseApiPreference: EditTextPreference? = null

    private var musicApi: String? = null// = SPUtils.getAnyByKey(SPUtils.SP_KEY_PLATER_API_URL, Constants.BASE_PLAYER_URL);
    private var neteaseApi: String? = null// = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_API_URL, Constants.BASE_NETEASE_URL);

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)

        initView()

        Handler().post {
            val size = DataClearManager.getTotalCacheSize(MusicApp.getAppContext())
            mPreferenceCache!!.summary = size

        }

        mWifiSwitch?.isChecked = SPUtils.getWifiMode()
        mWifiSwitch?.setOnPreferenceChangeListener { preference, newValue ->
            LogUtil.e("sss", newValue.toString())
            val wifiMode = newValue as Boolean
            mWifiSwitch?.isChecked = wifiMode
            SPUtils.saveWifiMode(wifiMode)
            false
        }
        mLyricCheckBox?.isChecked = SystemUtils.isOpenFloatWindow()
        mSocketSwitch?.isChecked = Config.isOpenSocket
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        mPreferenceCache = findPreference("key_cache")
        mPreferenceDownloadFile = findPreference("key_download_file")
        mPreferenceCacheFile = findPreference("key_cache_file")
        mWifiSwitch = findPreference("wifi_mode")
        mSocketSwitch = findPreference("key_socket")
        mNightSwitch = findPreference("key_night_mode")
        mCacheSwitch = findPreference("key_cache_mode")
        mLyricCheckBox = findPreference("key_lyric")
        mMusicQualityPreference = findPreference("key_music_quality")
        mMusicApiPreference = findPreference("key_music_api")
        mNeteaseApiPreference = findPreference("key_netease_api")

        mPreferenceCache?.onPreferenceClickListener = this
        mSocketSwitch?.onPreferenceClickListener = this
        mLyricCheckBox?.onPreferenceClickListener = this

        mPreferenceDownloadFile?.summary = FileUtils.getMusicDir()
        mPreferenceCacheFile?.summary = FileUtils.getMusicCacheDir()

        mMusicQualityPreference?.summary = mMusicQualityPreference?.entry
        mMusicQualityPreference?.setOnPreferenceChangeListener { preference, newValue ->
            //把preference这个Preference强制转化为ListPreference类型
            val listPreference = preference as ListPreference
            //获取ListPreference中的实体内容
            val entries = listPreference.entries
            //获取ListPreference中的实体内容的下标值
            val index = listPreference.findIndexOfValue(newValue as String)
            //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
            listPreference.summary = entries[index]
            ToastUtils.show("优先播放音质为：" + entries[index])
            false
        }
        mNightSwitch?.summary = mNightSwitch?.entry
        mNightSwitch?.setOnPreferenceChangeListener { preference, newValue ->
            //把preference这个Preference强制转化为ListPreference类型
            val listPreference = preference as ListPreference
            //获取ListPreference中的实体内容
            val entries = listPreference.entries
            //获取ListPreference中的实体内容的下标值
            val index = listPreference.findIndexOfValue(newValue as String)
            //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
            listPreference.summary = entries[index]
            mNightSwitch?.setValueIndex(index)
            (activity as BaseActivity<*>).updateAppTheme(index)
            false
        }
        initCacheSettings()
        initApiSettings()
    }

    private fun initCacheSettings() {
        mPreferenceCacheFile?.isEnabled = mCacheSwitch?.isChecked ?: false
        mCacheSwitch?.setOnPreferenceChangeListener { preference, newValue ->
            val isChecked = newValue as Boolean
            mCacheSwitch?.isChecked = isChecked
            mPreferenceCacheFile?.isEnabled = isChecked
            false
        }
    }

    /**
     * 更新主题配置
     */
    private fun updateTheme() {
        for (i in MusicApp.activities.indices) {
            if (MusicApp.activities[i] is MainActivity) {
                MusicApp.activities[i].recreate()
            }
        }
        startActivity(Intent(activity, SettingsActivity::class.java))
        activity?.overridePendingTransition(0, 0)
        activity?.finish()
    }


    /**
     * 初始化Api设置
     */
    private fun initApiSettings() {
        //获取本地api地址
        musicApi = SPUtils.getAnyByKey(SPUtils.SP_KEY_PLATER_API_URL, Constants.BASE_PLAYER_URL)
        neteaseApi = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_API_URL, Constants.BASE_NETEASE_URL)
        //
        mMusicApiPreference?.summary = musicApi
        mMusicApiPreference?.text = musicApi
        mNeteaseApiPreference?.summary = neteaseApi
        mNeteaseApiPreference?.text = neteaseApi

        mMusicApiPreference?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue.toString() != neteaseApi) {
                musicApi = newValue.toString()
                preference.summary = musicApi
                SPUtils.putAnyCommit(SPUtils.SP_KEY_PLATER_API_URL, musicApi)
                ToastUtils.show(getString(R.string.settings_restart_app))
            }
            false
        }
        mNeteaseApiPreference?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue.toString() != neteaseApi) {
                neteaseApi = newValue.toString()
                preference.summary = neteaseApi
                SPUtils.putAnyCommit(SPUtils.SP_KEY_NETEASE_API_URL, neteaseApi)
                ToastUtils.show(getString(R.string.settings_restart_app))
            }
            false
        }
    }


    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "key_about" -> {
                val intent = Intent(activity, AboutActivity::class.java)
                startActivity(intent)
            }
            "key_cache" ->
                activity?.let {
                    MaterialDialog(it).show {
                        title(R.string.title_warning)
                        message(R.string.setting_clear_cache)
                        positiveButton(R.string.sure) {
                            Handler().post {
                                try {
                                    //清除缓存
                                    DataClearManager.cleanApplicationData(MusicApp.getAppContext())
                                    ToastUtils.show(activity, "清除成功")
                                    val size = DataClearManager.getTotalCacheSize(MusicApp.getAppContext())
                                    mPreferenceCache!!.summary = size
                                } catch (e: Exception) {
                                    //清除失败
                                    ToastUtils.show(activity, "清除失败")
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            "key_socket" -> {
                Config.isOpenSocket = !Config.isOpenSocket
                mSocketSwitch?.isChecked = Config.isOpenSocket
                SocketManager.toggleSocket(Config.isOpenSocket)
            }
            "key_lyric" -> checkLyricPermission()
        }
        return true
    }

    /**
     * 检查桌面歌词所需的权限
     */
    private fun checkLyricPermission() {
        try {
            if (!SystemUtils.isOpenFloatWindow()) {
                ToastUtils.show(getString(R.string.float_window_manual_open))
                SystemUtils.applySystemWindow()
                mLyricCheckBox?.isChecked = true
            } else {
                mLyricCheckBox?.isChecked = true
                ToastUtils.show(getString(R.string.float_window_is_ready))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        mLyricCheckBox?.isChecked = SystemUtils.isOpenFloatWindow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_FLOAT_WINDOW) {
            if (SystemUtils.isOpenFloatWindow()) {
                checkLyricPermission()
            } else {
                ToastUtils.show(MusicApp.getAppContext(), getString(R.string.float_window_is_refused))
            }
        }
    }

    companion object {


        fun newInstance(): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
