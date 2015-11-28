package com.bigheart.library;

/**
 * three functions correspond to three functions in OnPageChangeListener of OnPageChangeListener,those be invoked when functions in OnPageChangeListener invoked
 *
 * @author :BigHeart
 * @version :1.0
 */


public interface CyclePagerChangeListener {

    void pageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void pageSelected(int position);

    void pageScrollStateChanged(int state);

}