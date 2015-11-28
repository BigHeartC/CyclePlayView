package com.bigheart.library;

/**
 * three functions correspond to three functions in CyclePagerChangeListener of CyclePlayPager,those be invoked when functions in CyclePagerChangeListener invoked
 *
 * @author BigHeart
 * @since 1.0
 */
public interface CycleViewChangeListener {

    void pageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void pageSelected(int position);

    void pageScrollStateChanged(int state);

}
