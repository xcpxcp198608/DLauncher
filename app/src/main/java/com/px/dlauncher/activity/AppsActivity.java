package com.px.dlauncher.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.px.dlauncher.R;
import com.px.dlauncher.adapter.FragmentAdapter;
import com.px.dlauncher.fragment.FragmentApps;
import com.px.dlauncher.fragment.FragmentOnline;
import com.px.dlauncher.fragment.FragmentMusic;
import com.px.dlauncher.fragment.FragmentVideos;
import com.px.dlauncher.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;


public class AppsActivity extends AppCompatActivity {

    private List<Fragment> fragmentList;
    private FragmentApps fragmentApps;
    private FragmentOnline fragmentOnline;
    private FragmentMusic fragmentMusic;
    private FragmentVideos fragmentVideos;
    private FragmentAdapter fragmentAdapter;
    private int currentItem = 0;
    private ViewPager viewPager;
    private ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        currentItem = getIntent().getIntExtra("currentItem",0);
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
        }
        if(fragmentApps == null){
            fragmentApps = new FragmentApps();
        }
        if(fragmentOnline == null){
            fragmentOnline = new FragmentOnline();
        }
        if(fragmentMusic == null){
            fragmentMusic = new FragmentMusic();
        }
        if(fragmentVideos == null){
            fragmentVideos = new FragmentVideos();
        }
        fragmentList.add(fragmentApps);
        fragmentList.add(fragmentOnline);
        fragmentList.add(fragmentVideos);
        fragmentList.add(fragmentMusic);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager() , fragmentList);
        viewPager.setAdapter(fragmentAdapter);

        String [] titles = {getString(R.string.apps) ,getString(R.string.games) ,
                getString(R.string.videos), getString(R.string.music)};
        viewPagerIndicator.setItem(5,0f,0f);
        viewPagerIndicator.setButtonTitle(titles , Color.TRANSPARENT
                , R.drawable.icon_bg_selected ,40, 0,0);
        viewPagerIndicator.attachViewPager(viewPager ,currentItem);
    }

}
