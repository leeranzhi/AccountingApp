package com.leecode1988.accountingapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import cn.bmob.v3.BmobUser;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private FloatingActionButton fbAddRecord;
    private TickerView amountText;
    private TextView dateText;
    private Toolbar toolbar;

    private Drawer result = null;
    private AccountHeader headerResult = null;
    //账户设置标识符
    private static final int PROFILE_SETTING_ADD_COUNT = 1;
    private static final int PROFILE_SETTING = 2;
    //个人简介
    private IProfile profile;
    private IProfile profile2;
    private IProfile profile3;

    private int currentPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("首页");
        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().mainActivity = this;

        amountText = findViewById(R.id.amount_text);
        amountText.setCharacterLists(TickerUtils.provideNumberList());
        dateText = findViewById(R.id.date_text);

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(this);

        viewPager.setCurrentItem(pagerAdapter.getLastIndex());
        fbAddRecord = findViewById(R.id.add_amount);
        fbAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        initDrawer(savedInstanceState);

//        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        updateHeader();
    }

    /**
     * 初始化Drawer侧滑栏
     *
     * @param savedInstanceState
     */
    private void initDrawer(Bundle savedInstanceState) {
        profile = new ProfileDrawerItem().withName("Lee Code").withEmail("aiguozhelee@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_drawer_avatar));
//        profile2 = new ProfileDrawerItem().withName("Lee Code2").withEmail("aiguozhelee@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_drawer_avatar));
//        profile3 = new ProfileDrawerItem().withName("Lee Code3").withEmail("aiguozhelee@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_drawer_avatar));
        //创建AccountHeader
        buildHeader(false, savedInstanceState);

        //Now create your drawer and pass the AccountHeader.Result
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(R.drawable.ic_drawer_left_home),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_statistics).withIcon(R.drawable.ic_drawer_left_statistics),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_statistics_year).withIcon(R.drawable.ic_drawer_statistics),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_exit).withIcon(R.drawable.ic_drawer_exit))
                .addStickyDrawerItems()
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                result.closeDrawer();
                                break;
                            case 2:
                                result.setSelectionAtPosition(1);
                                result.closeDrawer();
                                StatisticsActivity.actionStart(MainActivity.this, "");
                                break;
                            case 3:
                                result.setSelectionAtPosition(1);
                                result.closeDrawer();
                                AnnualStatisticsActivity.actionStart(MainActivity.this, "");
                                result.closeDrawer();
                                break;
                            case 4:
                                result.closeDrawer();
                                break;
                            case 5:
                                result.closeDrawer();
                                break;
                            case 6:
                                ActivityCollector.finishAll();
                            default:
                                return false;
                        }
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    /**
     * 重用逻辑来构建AccountHeader
     * 这将用于紧凑/普通标题替换抽屉的标题
     *
     * @param compat
     * @param savedInstanceState
     */
    private void buildHeader(boolean compat, Bundle savedInstanceState) {
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_drawer_header)
                .withCompactStyle(compat)
                .addProfiles(
                        profile,
//                        profile2,
//                        profile3,
//                        new ProfileSettingDrawerItem().withName("添加账户").withDescription("添加一个新的用户").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING_ADD_COUNT),
                        new ProfileSettingDrawerItem().withName("账户管理").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(PROFILE_SETTING)
                )
                //头像监听
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && profile.getIcon() != null) {
                            String isLogin = (String) SPUtil.get("userToken", "");
                            if (TextUtils.isEmpty(isLogin) && BmobUser.getCurrentUser(BmobUser.class) == null) {
                                result.closeDrawer();
                                LoginActivity.actionStart(MainActivity.this, "");
                                return true;
                            }
                            Log.d(TAG, new Gson().toJson(BmobUser.getCurrentUser(BmobUser.class)));
                            AccountCenterActivity.actionStart(MainActivity.this, BmobUser.getCurrentUser(BmobUser.class));
                        }
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withTextColor(ContextCompat.getColor(this, R.color.material_drawer_dark_primary_text))
                //账户监听
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        //如果点击的事件具有事件标识符则添加新的配置文件
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING_ADD_COUNT) {
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("BatMan").withEmail("leeguoqing@foxmail.com").withIcon(R.drawable.ic_drawer_avatar);
//                            if (headerResult.getProfiles() != null) {
//                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
//                            } else {
//                                headerResult.addProfiles(newProfile);
//                            }
                        } else if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            String isLogin = (String) SPUtil.get("userToken", "");
                            if (TextUtils.isEmpty(isLogin)) {
                                result.closeDrawer();
                                LoginActivity.actionStart(MainActivity.this, "");
                                return true;
                            }
                            AccountCenterActivity.actionStart(MainActivity.this, BmobUser.getCurrentUser(BmobUser.class));
                        }

                        //如果事件没有被消耗，且应该关闭Drawer，返回false
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//                mDrawerLayout.openDrawer(GravityCompat.START);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        pagerAdapter.update();
        pagerAdapter.reload();
        updateHeader();
    }


    @Override
    public void onPageSelected(int position) {
        currentPagerPosition = position;
        updateHeader();
    }

    public void updateHeader() {
        String amount = String.valueOf(pagerAdapter.getTotalCost(currentPagerPosition));
        amountText.setText(amount);
        String date = pagerAdapter.getDateStr(currentPagerPosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //将需要保存的值从抽屉添加至Bundle
        outState = result.saveInstanceState(outState);
        //将需要保存的值从accountHeader添加至Bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //先关闭抽屉，如果抽屉关闭了则关闭活动
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.finishAll();
    }
}
