package com.example.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import shenghaiyang.indicator.CircleChangeIndicator;
import shenghaiyang.indicator.LineChangeIndicator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        CircleChangeIndicator indicator = (CircleChangeIndicator) findViewById(R.id.circleIndicator);
        LineChangeIndicator lineChangeIndicator = (LineChangeIndicator) findViewById(R.id.lineIndicator);
        Fragment f2 = ImageFragment.newInstance(R.drawable.bg_2);
        Fragment f3 = ImageFragment.newInstance(R.drawable.bg_3);
        Fragment f4 = ImageFragment.newInstance(R.drawable.bg_4);
        Fragment f5 = ImageFragment.newInstance(R.drawable.bg_5);
        Fragment f6 = ImageFragment.newInstance(R.drawable.bg_6);
        final Fragment[] fragments = {f2, f3, f4, f5, f6};
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
//        indicator.setupWithViewPager(pager);
        lineChangeIndicator.setupWithViewPager(pager);

    }
}
