package com.woof.weatherfocus.ui;

import android.os.Bundle;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.ToolBarActivity;
import com.woof.weatherfocus.ui.fragment.SettingFragment;

/**
 * Created by Woof on 3/13/2017.
 */

public class SettingActivity extends ToolBarActivity {

    @Override
    protected int provideContentViewId() {
        return R.layout.setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setTitle("设置");
        getFragmentManager().beginTransaction().replace(R.id.framelayout, new SettingFragment()).commit();
    }

    @Override
    public boolean canBack() {
        return true;
    }
}
