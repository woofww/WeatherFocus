package com.woof.weatherfocus.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseActivity;
import com.woof.weatherfocus.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.version_tv)
    TextView mVersion;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        // bind the component and inject
        ButterKnife.bind(this);
        StatusBarUtil.setImmersiveStatusBarToolbar(mToolbar, this);
        initViews();
    }


    private void initViews() {

        // toolbar的setTitle方法应该在setSupportActionBar之前进行调用，否则便会无效
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        // 添加左上角返回图标
        if (mActionBar != null) mActionBar.setDisplayHomeAsUpEnabled(true);
        mToolbarLayout.setTitleEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home){
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
