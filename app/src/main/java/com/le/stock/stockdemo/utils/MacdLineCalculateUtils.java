package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.MacdLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * MACD称为指数平滑移动平均线，是从双指数移动平均线发展而来的，
 * 由快的指数移动平均线（EMA12）减去慢的指数移动平均线（EMA26）得到快线DIF，
 * 再用2×（快线DIF-DIF的9日加权移动均线DEA）得到MACD柱。
 * Created by zhangzhenzhong on 2017/6/20.
 */

public class MacdLineCalculateUtils {


    private List<MacdLineBean> mData=new ArrayList<>();
    public MacdLineCalculateUtils(List<MacdLineBean> data){
        this.mData=data;
    }

    /**
     * MACD称为指数平滑移动平均线
     * 快的指数移动平均线（EMA12）
     * @param day
     */
    public List<Entry> getMacdEMA12(int day){
        day=12;
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            if (i==0){
                mData.get(i).ema12=mData.get(i).close;
            }
            if (i>0&&i<=day){
                mData.get(i).ema12=mData.get(i).close*2/(i+1)+mData.get(i-1).ema12*(i-1)/(i+1);
            }
            if (i>day){
                mData.get(i).ema12=mData.get(i).close*2/(day+1)+mData.get(i-1).ema12*(day-1)/(day+1);
            }
            lineDayEntries.add(new Entry(mData.get(i).ema12,i));
        }
        return lineDayEntries;
    }

    /**
     * MACD称为指数平滑移动平均线
     * 快的指数移动平均线（EMA26）
     * @param day
     */
    public List<Entry> getMacdEMA26(int day){
        day=26;
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            if (i==0){
                mData.get(i).ema26=mData.get(i).close;
            }
            if (i>0&&i<=day){
                mData.get(i).ema26=mData.get(i).close*2/(i+1)+mData.get(i-1).ema26*(i-1)/(i+1);
            }
            if (i>day){
                mData.get(i).ema26=mData.get(i).close*2/27+mData.get(i-1).ema26*25/27;
            }
            lineDayEntries.add(new Entry(mData.get(i).ema26,i));
        }
        return lineDayEntries;
    }


    /**
     * MACD称为指数平滑移动平均线
     * 指数移动平均线（EMA09）
     * 差离值（DIF）的计算：DIF = EMA（12） - EMA（26） 。
     * 根据差离值计算其9日的EMA，即离差平均值，是所求的DEA值。
     * 今日DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
     * 为了不与指标原名相混淆，此值又名DEA或DEM。
     * @param day
     */
    public void getMacdEMA09(int day){
        day=9;
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            if (i==0){
                mData.get(i).ema09=mData.get(i).ema12-mData.get(i).ema26;//DIF
            }
            if (i>0&&i<=day){
                mData.get(i).ema09=(mData.get(i).ema12-mData.get(i).ema26)*2/(i+1)+mData.get(i-1).ema09*(i-1)/(i+1);
            }
            if (i>day){
                mData.get(i).ema26=(mData.get(i).ema12-mData.get(i).ema26)*2/10+mData.get(i-1).ema09*8/10;
            }
//            lineDayEntries.add(new Entry(mData.get(i).ema09,i));
        }
//        return lineDayEntries;
    }


    /**
     * MACD柱状图。
     * 差离值（DIF）的计算：DIF = EMA（12） - EMA（26） 。
     * 根据差离值计算其9日的EMA，即离差平均值，是所求的DEA值。
     * 用（DIF-DEA）*2即为MACD柱状图。
     */
    public List<Entry> getMacdBarData(){
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i=0;i<mData.size();i++){

            lineDayEntries.add(new Entry((mData.get(i).ema12-mData.get(i).ema26-mData.get(i).ema09)*2,i));

        }
        return lineDayEntries;
    }

}
