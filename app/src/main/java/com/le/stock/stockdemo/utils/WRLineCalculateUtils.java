package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  威廉指标(，WMS、WR)，由美国著名投资家拉里•威廉(Larry•Williams)于1973年在
 *  《我如何赚得一百万》(How I made one million dollars last year trading commodities)中首先发表，
 *  利用“最后一周期”之最高价、最低价、收市价，计算当日收盘价所处“最后一周期”(过去一定时间，
 *  比如7天等)内的价格区间之相对百分位置，即依“当日收盘价”的摆动点，以兼具超买超卖和强弱分界的指标，
 *  其度量市场处于超买还是超卖状态，主要作用在于辅助其他指标确认短期买卖信号。
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class WRLineCalculateUtils {

    private List<Entry> wrMinLineValues=new ArrayList<>();
    private List<Entry> wrMaxLineValues=new ArrayList<>();
    private List<KLineBean> mData;
    public WRLineCalculateUtils(List<KLineBean> data){
        this.mData=data;
    }


    /**
     * WR1一般是10天买卖强弱指标；
     * WR2一般是6天买卖强弱指标。（第二条线可以选择不画）
     * @param day01
     * @param day01
     */
    private void getWRData(int day01,int day02){

        for (int i=0;i<mData.size();i++){

            if (i>day01){
                float max=getMax(i-day01,i);
                wrMinLineValues.add(new Entry(100*(max-mData.get(i).close)/(max-getMin(i-day01,i)),i,"#ff0000"));//红线
            }
//            if (i>day02){
//                float max=getMax(i-day02,i);
//                wrMinLineValues.add(new Entry(100*(max-mData.get(i).close)/(max-getMin(i-day02,i)),i,"#00ff00"));//绿线
//            }
        }
    }


    /**
     * 获取N日内出现的最大值
     * @param start
     * @param end
     */
    private float getMax(int start,int end){
        float max=Float.MIN_VALUE;
        for (int i=start;i<=end;i++){

            if (max<mData.get(i).high){
                max=mData.get(i).high;
            }
        }
        return max;
    }
    /**
     * 获取N日内出现的最低值
     * @param start
     * @param end
     */
    private float getMin(int start,int end){

        float min=Float.MAX_VALUE;
        for (int i=start;i<=end;i++){
            if (min>mData.get(i).low){
                min=mData.get(i).low;
            }
        }
        return min;
    }


}
