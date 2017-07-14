package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.CCLLineBean;
import com.le.stock.stockdemo.bean.ChartColorUtils;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 顺势指标又叫CCI指标，CCI指标是美国股市技术分析 家唐纳德•蓝伯特(Donald Lambert)于20世纪80年代提出的，
 * 专门测量股价、外汇或者贵金属交易是否已超出常态分布范围。属于超买超卖类指标中较特殊的一种。
 * 波动于正无穷大和负无穷大之间。但是，又不需要以0为中轴线，这一点也和波动于正无穷大和负无穷大的指标不同。
 * Created by zhangzhenzhong on 2017/6/23.
 */

public class CCLLineCalculateUtils {


    private List<CCLLineBean> mData=new ArrayList<>();

    List<Entry> mCCIValues =new ArrayList<>();
    List<Entry> mCCIValues02 =new ArrayList<>();

    private int day;
    public CCLLineCalculateUtils(List<CCLLineBean> data){
        this.mData=data;
        setCCI_MA_Data(day);
        getCCIData01(day);
//        getCCIData02(day);备选方式
    }

    /**
     * 以日CCI计算为例，其计算方法有两种。
     * 第一种计算过程如下：
     * CCI（N日）=（TP－MA）÷MD÷0.015
     * 其中，TP=（最高价+最低价+收盘价）÷3
     * MA=近N日收盘价的累计之和÷N
     * MD=近N日（MA－收盘价）的累计之和÷N
     * 0.015为计算系数，N为计算周期
     */
    private void getCCIData01(int day){

        for (int i=0;i<mData.size();i++){

            if (i>day){
                float md=getSumMD(i-day,i)/day;
                mCCIValues.add(new Entry((float)(md/0.015),i, ChartColorUtils.yellow_c6a563));
            }
        }
    }
    /**
     * 第二种计算方法表述为中价与中价的N日内移动平均的差除以0.015*N日内中价的平均绝对偏差
     * 其中，中价等于最高价、最低价和收盘价之和除以3
     * typePrice=(H+C+L)/3
     * 平均绝对偏差为统计函数
     *
     * 备选方案
     */
    private void getCCIData02(int day){
        for (int i=0;i<mData.size();i++){

            if (i>day){
                CCLLineBean bean=mData.get(i);
                mCCIValues02.add(new Entry((bean.high+bean.close+bean.low)/3,i, ChartColorUtils.yellow_c6a563));
            }
        }
    }

    /**
     * MA=近N日收盘价的累计之和÷N
     * @param day
     */
    private void setCCI_MA_Data(int day){
        for (int i=0;i<mData.size();i++){
            if (i>day){
                mData.get(i).ma=getSumMA(i-day,i)/day;
            }
        }
    }

    private  float getSumMA(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).close;
        }
        return sum;
    }
    private  float getSumMD(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).ma-mData.get(i).close;
        }
        return sum;
    }

    public List<Entry> getmCCIValues() {
        return mCCIValues;
    }

    public List<Entry> getmCCIValues02() {
        return mCCIValues02;
    }
}
