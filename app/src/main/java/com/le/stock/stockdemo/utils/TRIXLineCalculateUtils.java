package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.CCLLineBean;
import com.le.stock.stockdemo.bean.ChartColorUtils;
import com.le.stock.stockdemo.bean.TRIXLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 三重指数平滑平均线（TRIX）属于长线指标。它过滤掉许多不必要的波动来反映股价的长期波动趋势。
 * 在使用均线系统的交叉时，有时会出现骗线的情况，有时还会出现频繁交叉的情况，通常还有一个时间上的确认。
 * 为了解决这些问题，因而发明了TRIX这个指标把均线的数值再一次地算出平均数，并在此基础上算出第三重的平均数。
 * 这样就可以比较有效地避免频繁出现交叉信号。 TRIX指标又叫三重指数平滑移动平均指标，
 * 其英文全名为“Triple Exponentially Smoothed Average”，是一种研究股价趋势的长期技术分析工具。
 * Created by zhangzhenzhong on 2017/6/23.
 */

public class TRIXLineCalculateUtils {

    private List<TRIXLineBean> mData=new ArrayList<>();

    List<Entry> mTRIXValues =new ArrayList<>();
    List<Entry> mTRMAValues02 =new ArrayList<>();

    private int dayN=12;
    private int dayM=20;

    public TRIXLineCalculateUtils(List<TRIXLineBean> data){
        this.mData=data;

        getTr(dayN);
        getTRIX(dayN);
        getTRMA(dayM);
    }


    /**
     * TR=收盘价的N日指数移动平均值
     *
     * @param day
     */
    public List<Entry> getTr(int day){
        day=12;
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            if (i==0){
                mData.get(i).tr=mData.get(i).close;
            }
            if (i>0&&i<=day){
                mData.get(i).tr=mData.get(i).close*2/(i+1)+mData.get(i-1).tr*(i-1)/(i+1);
            }
            if (i>day){
                mData.get(i).tr=mData.get(i).close*2/(day+1)+mData.get(i-1).tr*(day-1)/(day+1);
            }
            lineDayEntries.add(new Entry(mData.get(i).tr,i));
        }
        return lineDayEntries;
    }


    /**
     * TRIX=(TR-昨日TR)/昨日TR*100
     * @param dayN
     */
    private void getTRIX(int dayN){
        for (int i=0;i<mData.size();i++){
            if (i>dayN){
                float trix=(mData.get(i).tr-mData.get(i-1).tr)/mData.get(i-1).tr*100;
                mData.get(i).trix=trix;
                mTRIXValues.add(new Entry(trix,i, ChartColorUtils.yellow_c6a563));
            }
        }
    }




    /**
     *  MATRIX=TRIX的M日简单移动平均
     * @param dayM
     */
    private void getTRMA(int dayM){
        for (int i=0;i<mData.size();i++){
            if (i>dayM){
                mTRIXValues.add(new Entry(getSum(i-dayM,i)/dayM,i, ChartColorUtils.blue_2591ff));
            }
        }
    }


    /**
     * TRIX的M日总和
     * @param start
     * @param end
     * @return
     */
    private float getSum(int start ,int end){
        float sum=0;
        for (int i=start;i<end;i++){
            sum+=mData.get(i).trix;
        }
        return sum;
    }

    public List<Entry> getmTRIXValues() {
        return mTRIXValues;
    }

    public List<Entry> getmTRMAValues02() {
        return mTRMAValues02;
    }
}
