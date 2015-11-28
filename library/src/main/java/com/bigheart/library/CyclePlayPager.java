package com.bigheart.library;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigHeart on 15/11/16.
 */


public class CyclePlayPager extends ViewPager {
    public static final String INFO = "CyclePlayView";


    /**
     * @param viewList recommend use the view could fill pager
     */
    public void setViewList(List<View> viewList) {
        this.viewList = viewList;
        notifyDataSetChanged();
    }

    public boolean isAutoCycle() {
        return isAutoCycle;
    }

    public int getShiftDuration() {
        return shiftDuration;
    }

    public PlayType getPlayType() {
        return playType;
    }

    public int getStayDuration() {
        return stayDuration;
    }

    public List<View> getViewList() {
        return viewList;
    }

    public void setCyclePagerChangeListener(CyclePagerChangeListener cyclePagerChangeListener) {
        this.cyclePagerChangeListener = cyclePagerChangeListener;
    }

    public final int CYC_MSG_ID = 1000;

    public enum PlayType {
        BackToStart, KeepGoing;
    }

    /**
     * the view auto shift or not
     */
    private boolean isAutoCycle = true;
    private final int SCROLLING = 0, STAYING = 1;
    private int pagerState = STAYING;
    private PlayType playType = PlayType.KeepGoing;
    private int stayDuration = 3000, shiftDuration = 800;
    private List<View> viewList;
    private CycleAdapter adapter;


    private CyclePagerChangeListener cyclePagerChangeListener;

    private volatile boolean cancelLastShift = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case CYC_MSG_ID:
                    if (playType == PlayType.BackToStart) {
                        if (pagerState == STAYING) {
                            setCurrentItem((getCurrentItem() + 1) % viewList.size());
                        }
                        handler.sendEmptyMessageDelayed(CYC_MSG_ID, stayDuration);
                    } else if (playType == PlayType.KeepGoing) {
                        if (pagerState == STAYING && getCurrentItem() + 1 < Integer.MAX_VALUE) {
                            setCurrentItem(getCurrentItem() + 1);
                        }
                        handler.sendEmptyMessageDelayed(CYC_MSG_ID, stayDuration);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public CyclePlayPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CyclePlayPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        viewList = new ArrayList<>();
        adapter = new CycleAdapter();

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);

            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);


            FixedSpeedScroller scroller = new FixedSpeedScroller(this.getContext(), (Interpolator) interpolator.get(null));
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }


    public void start(List<View> viewList) {

        this.setAdapter(adapter);

        if (viewList == null) {
            throw new NullPointerException();
        }

        setViewList(viewList);

        if (isAutoCycle) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessageDelayed(CYC_MSG_ID, stayDuration);
                }
            }).start();
        }

        if (playType == PlayType.KeepGoing) {
            setCurrentItem(Integer.MAX_VALUE / 2);
        }

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (cyclePagerChangeListener != null)
                    cyclePagerChangeListener.pageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (cyclePagerChangeListener != null)
                    cyclePagerChangeListener.pageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case SCROLL_STATE_DRAGGING:
                        pagerState = SCROLLING;
//                        cancelLastShift = true;
                        break;
                    case SCROLL_STATE_SETTLING:
                        pagerState = STAYING;
                        break;
                    case SCROLL_STATE_IDLE:
                        pagerState = STAYING;
//                        if (cancelLastShift) {
//                            Log.i(INFO, "start new shift thread");
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    handler.sendEmptyMessageDelayed(CYC_MSG_ID, stayDuration);
//                                }
//                            }).start();
//                        }
                        break;
                }
                if (cyclePagerChangeListener != null)
                    cyclePagerChangeListener.pageScrollStateChanged(state);

            }
        });
    }


    public void start(List<View> viewList, boolean isAutoCycle) {
        this.isAutoCycle = isAutoCycle;
        start(viewList);
    }

    public void start(List<View> viewList, boolean isAutoCycle, int stayDuration, int shiftDuration) {
        this.stayDuration = stayDuration;
        this.shiftDuration = shiftDuration;
        start(viewList, isAutoCycle);
    }

    public void start(List<View> viewList, boolean isAutoCycle, PlayType playType, int stayDuration, int shiftDuration) {
        this.playType = playType;
        start(viewList, isAutoCycle, stayDuration, shiftDuration);
    }


    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }


    private class CycleAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (playType == PlayType.KeepGoing)
                return Integer.MAX_VALUE;
            else
                return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (playType == PlayType.BackToStart)
                container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;


            if (playType == PlayType.KeepGoing) {
                view = viewList.get(position % viewList.size());
                if (view.getParent() != null) {
                    container.removeView(view);
                }
            } else {
                view = viewList.get(position);
            }
            container.addView(view);
            return view;
        }

    }


    private class FixedSpeedScroller extends Scroller {

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, shiftDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, getShiftDuration());
        }
    }

}
