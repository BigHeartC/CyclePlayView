package com.bigheart.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by BigHeart on 15/11/17.
 */
public class CyclePlayView extends RelativeLayout {

    public String INFO = "CyclePlayView";

    private LinearLayout hintPoints;
    private Bitmap selectedBmp, unSelectedBmp;
    private LinearLayout.LayoutParams pointsParams;
    private CycleViewChangeListener cycleViewChangeListener;
    private CyclePlayPager playPager;


    private int selectedColor = Color.WHITE, unSelectedColor = Color.GRAY, rad = 10;


    private final int pointsInterval = 3;


    public CyclePlayView(Context context) {
        super(context);
        init();
    }

    public CyclePlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CyclePlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CyclePlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void setViewList(List<View> viewList) {
        playPager.setViewList(viewList);
        adjustHintPoints(viewList);
    }

    public boolean isAutoCycle() {
        return playPager.isAutoCycle();
    }

    public int getShiftDuration() {
        return playPager.getShiftDuration();
    }

    public CyclePlayPager.PlayType getPlayType() {
        return playPager.getPlayType();
    }

    public int getStayDuration() {
        return playPager.getStayDuration();
    }

    public List<View> getViewList() {
        return playPager.getViewList();
    }

    public void setCycleViewChangeListener(CycleViewChangeListener cycleViewChangeListener) {
        this.cycleViewChangeListener = cycleViewChangeListener;
    }

    public int getCyclePlayPagerId() {
        return playPager.getId();
    }

    public int getHintPointsLayoutId() {
        return hintPoints.getId();
    }


    //----------------------

    private void init() {
        LayoutParams pagerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        playPager = new CyclePlayPager(getContext());
        playPager.setCyclePagerChangeListener(new CyclePagerChangeListener() {
            @Override
            public void pageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (cycleViewChangeListener != null)
                    cycleViewChangeListener.pageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void pageSelected(int position) {
                for (int i = 0; i < hintPoints.getChildCount(); i++) {
                    ((ImageView) hintPoints.getChildAt(i)).setImageBitmap(unSelectedBmp);
                }
                ((ImageView) hintPoints.getChildAt(position % hintPoints.getChildCount())).setImageBitmap(selectedBmp);

                if (cycleViewChangeListener != null)
                    cycleViewChangeListener.pageSelected(position);
            }

            @Override
            public void pageScrollStateChanged(int state) {
                if (cycleViewChangeListener != null)
                    cycleViewChangeListener.pageScrollStateChanged(state);
            }
        });
        playPager.setId(generateViewId());
        addView(playPager, pagerParams);

        LayoutParams hintLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        hintLayoutParams.bottomMargin = (int) (getContext().getResources().getDisplayMetrics().density * 10 + 0.5f);
        hintPoints = new LinearLayout(getContext());
        hintPoints.setId(generateViewId());
        hintLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, playPager.getId());
        hintLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        addView(hintPoints, hintLayoutParams);

        pointsParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pointsParams.leftMargin = (int) (pointsInterval * getResources().getDisplayMetrics().density);
        pointsParams.rightMargin = (int) (pointsInterval * getResources().getDisplayMetrics().density);
    }

    public void setPageParams(LayoutParams pagerParams) {
        playPager.setLayoutParams(pagerParams);
    }

    public void setPagerPadding(int left, int top, int right, int bottom) {
        playPager.setPadding(left, top, right, bottom);
    }

    public void setHintPointsLayoutParams(LayoutParams pagerParams) {
        hintPoints.setLayoutParams(pagerParams);

    }

    public void setHintPointsLayoutPadding(int left, int top, int right, int bottom) {
        hintPoints.setPadding(left, top, right, bottom);
    }

    public void notifyDataSetChanged() {
        playPager.notifyDataSetChanged();
    }


    public void initHintPointsStyle(int rad) {
        this.rad = rad;
    }

    public void initHintPointsStyle(int rad, int selectedColor, int unSelectedColor) {
        this.selectedColor = selectedColor;
        this.unSelectedColor = unSelectedColor;
        initHintPointsStyle(rad);
    }

    public void initHintPointsStyle(int rad, Bitmap selectedBmp, Bitmap unSelectedBmp) {
        this.selectedBmp = selectedBmp;
        this.unSelectedBmp = unSelectedBmp;
        initHintPointsStyle(rad);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initHintPointsStyle(int rad, int selectedColor, int unSelectedColor, Drawable hintPointsLayoutBg, LinearLayout.LayoutParams pointsParams) {
        hintPoints.setBackground(hintPointsLayoutBg);
        this.pointsParams = pointsParams;
        initHintPointsStyle(rad, selectedColor, unSelectedColor);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initHintPointsStyle(int rad, Bitmap selectedBmp, Bitmap unSelectedBmp, Drawable hintPointsLayoutBg, LinearLayout.LayoutParams pointsParams) {
        hintPoints.setBackground(hintPointsLayoutBg);
        this.pointsParams = pointsParams;
        initHintPointsStyle(rad, selectedBmp, unSelectedBmp);
    }


    public void start(List<View> viewList) {
        adjustHintPoints(viewList);
        playPager.start(viewList);
    }

    public void start(List<View> viewList, boolean isAutoCycle) {
        playPager.start(viewList, isAutoCycle);
    }

    public void start(List<View> viewList, boolean isAutoCycle, int stayDuration, int shiftDuration) {
        playPager.start(viewList, isAutoCycle, stayDuration, shiftDuration);
    }

    public void start(List<View> viewList, boolean isAutoCycle, CyclePlayPager.PlayType playType, int stayDuration, int shiftDuration) {
        playPager.start(viewList, isAutoCycle, playType, stayDuration, shiftDuration);
    }


    //-----------------------------

    /**
     * draw a circle point according to color and radius
     *
     * @param color
     * @param radius
     * @return
     */
    private Bitmap drawCirclePoint(int color, int radius) {
        Bitmap bitmap = Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        canvas.drawCircle(radius, radius, radius, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }


    /**
     * update points
     *
     * @param views views of showing in pager
     */
    private void adjustHintPoints(List<View> views) {
        if (views.size() <= 1) {
            hintPoints.setVisibility(GONE);
        } else {
            hintPoints.setVisibility(VISIBLE);
            if (unSelectedBmp == null) {
                unSelectedBmp = drawCirclePoint(unSelectedColor, rad);
            }
            if (selectedBmp == null) {
                selectedBmp = drawCirclePoint(selectedColor, rad);
            }

            int lastLen = hintPoints.getChildCount();
            if (lastLen < views.size()) {
                for (int i = 0; i < lastLen; i++) {
                    ((ImageView) hintPoints.getChildAt(i)).setImageBitmap(unSelectedBmp);
                }


                for (int ADDi = lastLen; ADDi < views.size(); ADDi++) {
                    ImageView tmpIv = new ImageView(getContext());
                    tmpIv.setImageBitmap(unSelectedBmp);
//                tmpIv.setGravity(Gravity.RIGHT);
                    hintPoints.addView(tmpIv, pointsParams);
                }

            } else if (lastLen > views.size()) {
                hintPoints.removeViews(views.size(), lastLen - views.size());


                for (int i = 0; i < hintPoints.getChildCount(); i++) {
                    ((ImageView) hintPoints.getChildAt(i)).setImageBitmap(unSelectedBmp);
                }

            }

            ((ImageView) hintPoints.getChildAt(playPager.getCurrentItem() % hintPoints.getChildCount())).setImageBitmap(selectedBmp);
        }
    }


    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    @SuppressLint("NewApi")
    public static int generateViewId() {

        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }
}
