package shenghaiyang.indicator;

import android.support.v4.view.ViewPager;

/**
 * @author shenghaiyang
 */
public interface PagerIndicator extends ViewPager.OnPageChangeListener {

    void setupWithViewPager(ViewPager viewPager);

    void notifyDataSetChanged();
}
