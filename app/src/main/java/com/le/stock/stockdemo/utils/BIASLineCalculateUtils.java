package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.BIASLineBean;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 乖离率(BIAS)，又称偏离率，简称Y值，是通过计算市场指数或收盘价与某条移动平均线之间的差距百分比，
 * 以反映一定时期内价格与其MA偏离程度的指标，
 * 从而得出价格在剧烈波动时因偏离移动平均趋势而造成回档或反弹的可能性，
 * 以及价格在正常波动范围内移动而形成继续原有势的可信度。
 * 乖离率，是用百分比来表示价格与MA间的偏离程度(差距率)。
 * 乖离率曲线(BIAS)，是将各BIAS值连成线，得到的一条以0值为横向中轴之波动伸延的曲线。
 * Created by zhangzhenzhong on 2017/6/23.
 */

public class BIASLineCalculateUtils {
    private List<Entry> mBIAS1LineValues=new ArrayList<>();
    private List<Entry> mBIAS2LineValues=new ArrayList<>();
    private List<Entry> mBIAS3LineValues=new ArrayList<>();
    private List<BIASLineBean> mData;
    private int day01=6,day02=12,day03=24;
    public BIASLineCalculateUtils(List<BIASLineBean> data){
        this.mData=data;
        getBISALineValue(day01,day02,day03);
    }


    /**
     * BIAS=(收盘价-收盘价的N日简单平均)/收盘价的N日简单平均*100
     * @param day01 N的参数一般设置为6日
     * @param day02 12日
     * @param day03 24日
     */
    private void getBISALineValue(int day01,int day02,int day03){

        for (int i=0;i<mData.size();i++){

            if (i>day01){
                float pj=getSumC(i-day01,i)/day01;
                mBIAS1LineValues.add(new Entry((mData.get(i).close-pj)/pj*100,i));
            }
            if (i>day02){
                float pj=getSumC(i-day02,i)/day01;
                mBIAS1LineValues.add(new Entry((mData.get(i).close-pj)/pj*100,i));
            }
            if (i>day03){
                float pj=getSumC(i-day03,i)/day01;
                mBIAS1LineValues.add(new Entry((mData.get(i).close-pj)/pj*100,i));
            }
        }
    }


    /**
     * 收盘价的N日总和
     * @param a
     * @param b
     * @return
     */
    private  float getSumC(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).close;
        }
        return sum;
    }

    public List<Entry> getmBIAS1LineValues() {
        return mBIAS1LineValues;
    }

    public List<Entry> getmBIAS2LineValues() {
        return mBIAS2LineValues;
    }

    public List<Entry> getmBIAS3LineValues() {
        return mBIAS3LineValues;
    }
}
