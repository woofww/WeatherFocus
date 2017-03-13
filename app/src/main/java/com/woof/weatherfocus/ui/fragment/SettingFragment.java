package com.woof.weatherfocus.ui.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseApplication;
import com.woof.weatherfocus.util.FileRelatedUtil;
import com.woof.weatherfocus.util.GlideHelper;
import com.woof.weatherfocus.util.SharedPreferenceUtil;

import java.io.File;

/**
 * Created by Woof on 3/13/2017.
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{

    private static String TAG = SettingFragment.class.getSimpleName();

    private SharedPreferenceUtil mSharedPreferenceUtil;
    private Preference mChangeUpdate;
    private Preference mClearCache;
    private SwitchPreference mTempUnit;
    private SwitchPreference mAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();

        mChangeUpdate = findPreference(SharedPreferenceUtil.AUTO_UPDATE);
        mClearCache   = findPreference(SharedPreferenceUtil.CACHE_CLEAR);

        mTempUnit     = ((SwitchPreference) findPreference(SharedPreferenceUtil.TEMP_UNIT));
        mAnimation    = ((SwitchPreference) findPreference(SharedPreferenceUtil.ANIMATION_SWITCH));

        mAnimation.setChecked(mSharedPreferenceUtil.getAnimationSwitch());
        mChangeUpdate.setSummary(mSharedPreferenceUtil.getAutoUpdate() == 0 ? "禁止刷新" : "每" +
        mSharedPreferenceUtil.getAutoUpdate() + "小时更新");

        mClearCache.setSummary(FileRelatedUtil.getAutoFileOrFilesSize(BaseApplication.getAppCacheDir() + "/NetCache"));

        mChangeUpdate.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);

        mTempUnit.setOnPreferenceChangeListener(this);
        mAnimation.setOnPreferenceChangeListener(this);

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mClearCache == preference) {
            GlideHelper.imageClear(getActivity());
            if (FileRelatedUtil.delete(new File(BaseApplication.getAppCacheDir() + "/NetCache"))) {
                mClearCache.setSummary(FileRelatedUtil.getAutoFileOrFilesSize(BaseApplication
                        .getAppCacheDir() + "/NetCache"));
                Snackbar.make(getView(), "缓存已清除", Snackbar.LENGTH_LONG).show();
            }
        } else if (mChangeUpdate == preference) {

        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        if (mTempUnit == preference) {
            SharedPreferenceUtil.getInstance().setTempUnit((Boolean) value);
            Log.e("温度转换", String.valueOf(SharedPreferenceUtil.getInstance().getTempUnit()));
        } else if (mAnimation == preference) {
            SharedPreferenceUtil.getInstance().setAnimationSwitch((Boolean) value);
        }
        return true;
    }

}
