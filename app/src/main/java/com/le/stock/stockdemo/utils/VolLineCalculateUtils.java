package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;
import com.le.stock.stockdemo.bean.MinutesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhenzhong on 2017/6/20.
 */

public class VolLineCalculateUtils{


    private List<KLineBean> mData=new ArrayList<>();

    public VolLineCalculateUtils(List<KLineBean> data){
        this.mData=data;
    }


    /**
     * VOL是成交量指标
     * VOL=∑nVi/N
     * @param Day
     * @return
     */
    public List<Entry> getVol(int Day){
        ArrayList<Entry> lineDayEntries = new ArrayList<>();
        for (int i = 0, j = 0; i <mData.size(); i++, j++) {
            if (i >= Day) {
                lineDayEntries.add(new Entry(getSum(i - (Day-1), i) / Day, i));
            }
        }
        return lineDayEntries;
    }


    private  float getSum(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).vol;
        }
        return sum;
    }
}
