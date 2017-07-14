package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * AR指标是反映市场当前情况下多空双方力量发展对比的结果。
 * 它是以当日的开盘价为基点。与当日最高价相比较，依固定公式计算出来的强弱指标。
 * Created by zhangzhenzhong on 2017/6/23.
 */

public class ARBRLineCalculateUtils {

    List<Entry> mARValues =new ArrayList<>();
    List<Entry> mBRValues =new ArrayList<>();
    private List<KLineBean> mData=new ArrayList<>();
    private int day=26;


    public ARBRLineCalculateUtils(List<KLineBean> data){
        this.mData=data;
        getLineData(day);
    }


    private void getLineData(int day){
        for (int i=0;i<mData.size();i++){
            if (i>day){
                mARValues.add(new Entry(getSumHO(i-day,i)/getSumOL(i-day,i)*100,i));
                mBRValues.add(new Entry(getSumHPC(i-day,i)/getSumPCL(i-day,i)*100,i));
            }
        }
    }


    /**
     * AR计算公式
     * AR=((H - O)26天之和/(O - L)26天之和) * 100
     * H：当天之最高价
     * L：当天之最低价
     * O：当天之开盘价
     * @param a
     * @param b
     * @return
     */
    private float getSumHO(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).high-mData.get(i).open;
        }
        return sum;
    }
    /**
     * AR计算公式
     * AR=((H - O)26天之和/(O - L)26天之和) * 100
     * H：当天之最高价
     * L：当天之最低价
     * O：当天之开盘价
     * @param a
     * @param b
     * @return
     */
    private float getSumOL(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).open-mData.get(i).low;
        }
        return sum;
    }
    /**
     * BR计算公式
     * BR=((H - PC)26天之和/(PC - L)26天之和) * 100
     * H：当天之最高价；
     * L：当天之最低价；
     * PC：昨天之收盘价；
     * @param a
     * @param b
     * @return
     */
    private float getSumHPC(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).high-mData.get(i-1).close;
        }
        return sum;
    }
    /**
     * BR计算公式
     * BR=((H - PC)26天之和/(PC - L)26天之和) * 100
     * H：当天之最高价；
     * L：当天之最低价；
     * PC：昨天之收盘价；
     * @param a
     * @param b
     * @return
     */
    private float getSumPCL(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i-1).close-mData.get(i).low;
        }
        return sum;
    }

    public List<Entry> getmARValues() {
        return mARValues;
    }

    public List<Entry> getmBRValues() {
        return mBRValues;
    }
}
