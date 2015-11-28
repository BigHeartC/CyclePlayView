package com.bigheart.cycleplayviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigheart.library.CyclePlayPager;
import com.bigheart.library.CyclePlayView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btBegin;
    private CyclePlayView cyclePlayView, cyclePlayView2;
    private CyclePlayPager playPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btBegin = (Button) findViewById(R.id.bt_begin);
        btBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cyclePlayView2.setViewList(getView2());
            }
        });


        cyclePlayView = (CyclePlayView) findViewById(R.id.cy_view1);
        cyclePlayView.start(getView());


        cyclePlayView2 = (CyclePlayView) findViewById(R.id.cy_view2);
        RelativeLayout.LayoutParams hintPointsLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        hintPointsLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, cyclePlayView2.getCyclePlayPagerId());
        hintPointsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cyclePlayView2.setHintPointsLayoutParams(hintPointsLayoutParams);


        LinearLayout.LayoutParams pointsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointsParams.gravity = Gravity.CENTER_VERTICAL;
        pointsParams.leftMargin = 10;
        pointsParams.rightMargin = 10;
        pointsParams.topMargin = 10;
        pointsParams.bottomMargin = 10;
        cyclePlayView2.initHintPointsStyle(15, Color.WHITE, Color.GRAY, new ColorDrawable(Color.parseColor("#aa000000")), pointsParams);
        cyclePlayView2.start(getView());

        playPager = (CyclePlayPager) findViewById(R.id.cy_pager);
        playPager.start(getView2());


    }


    private ArrayList<View> getView() {
        ArrayList<View> views = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        for (int i = 0; i < 8; i++) {
            View view = inflater.inflate(R.layout.cy_item, null, false);
            ((ImageView) view.findViewById(R.id.iv_pic)).setImageDrawable(getResources().getDrawable(R.drawable.pic2));
            ((TextView) view.findViewById(R.id.tv_con)).setText(i + "");
            views.add(view);
        }

        return views;
    }

    private ArrayList<View> getView2() {
        ArrayList<View> views = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        for (int i = 0; i < 2; i++) {
            View view = inflater.inflate(R.layout.cy_item, null, false);
            ((ImageView) view.findViewById(R.id.iv_pic)).setImageDrawable(getResources().getDrawable(R.drawable.pic1));
            ((TextView) view.findViewById(R.id.tv_con)).setText(i + "");
            views.add(view);
        }

        return views;
    }
}
