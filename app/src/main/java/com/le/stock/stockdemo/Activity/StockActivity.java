package com.le.stock.stockdemo.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.le.stock.stockdemo.R;
import com.le.stock.stockdemo.fragment.DayChartFragment;
import com.le.stock.stockdemo.fragment.MinuteChartFragment;
import com.le.stock.stockdemo.view.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangzhenzhong on 2017/6/9.
 */

public class StockActivity extends AppCompatActivity {
    FrameLayout flJrmetalChart;
    Fragment mMinuteChartFragment;
    Fragment mDayChartFragment;
    private FragmentManager fragmentManager;
    /**
     * 周期tab
     **/
    private SmartTabLayout viewTab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity);
        init();
    }

    private void init() {
        flJrmetalChart=(FrameLayout)findViewById(R.id.fl_jrmetal_chart);
        viewTab = (SmartTabLayout) findViewById(R.id.mainchart_smart_tab);
        List<String> titles = new ArrayList<String>();
        titles.add("分时");
        titles.add("日k");
        titles.add("周k");
        titles.add("月k");
        //添加标题
        viewTab.setTabText(titles, 0);
        viewTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                chageFragment(position);
            }
        });

        fragmentManager=this.getFragmentManager();
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        if (mMinuteChartFragment==null){
            mMinuteChartFragment=new MinuteChartFragment();
        }
        transaction.add(R.id.fl_jrmetal_chart, mMinuteChartFragment);
        transaction.commitAllowingStateLoss();
    }


    private void chageFragment(int postion){
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        switch (postion){
            case 0:
                if (mMinuteChartFragment==null){
                    mMinuteChartFragment=new MinuteChartFragment();
                }
                transaction.replace(R.id.fl_jrmetal_chart, mMinuteChartFragment);

                transaction.commitAllowingStateLoss();
                break;
            case 1:
                if (mDayChartFragment==null){
                    mDayChartFragment=new DayChartFragment();
                }
                transaction.replace(R.id.fl_jrmetal_chart, mDayChartFragment);
                transaction.commitAllowingStateLoss();
                break;

            case 2:

                break;
            default:
                    break;
        }
    }
}
