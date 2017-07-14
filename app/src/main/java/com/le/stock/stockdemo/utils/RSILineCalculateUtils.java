package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.le.stock.stockdemo.bean.KLineBean;
import com.le.stock.stockdemo.bean.RSILineBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 相对强弱指标：RSI (Relative Strength Index) 强弱指标是由威尔斯.威尔德（Welles Wilder)最早应用于期货买卖，
 * 后来人们发现在众多的图表技术分析中，强弱指标的理论和实践极其适合于股票市场的短线投资，
 * 于是被用于股票升跌的测量和分析中。
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class RSILineCalculateUtils {

    private List<Entry> rsiMinLineValue=new ArrayList<>();
    private List<Entry> rsiMidLineValue=new ArrayList<>();
    private List<Entry> rsiMaxLineValue=new ArrayList<>();
    private List<RSILineBean> mData=new ArrayList<>();

    private int dayMin=6;
    private int dayMid=12;
    private int dayMax=24;
    public RSILineCalculateUtils(List<RSILineBean> data){
        this.mData=data;
        getUpOrDown();
        getRSILineValue(dayMin,dayMid,dayMax);
    }


    /**
     * 将数据点中的上涨、下跌请求计算出来
     * 0、平盘 1、 代表上涨 2、代表下跌
     */
    private void getUpOrDown(){
        for (int i=0;i<mData.size();i++){
            if (mData.get(i).open<mData.get(i).close){
                mData.get(i).upDown=1;
            }else if (mData.get(i).open>mData.get(i).close){
                mData.get(i).upDown=2;
            }
        }
    }


    /**
     * RSI6一般是6日相对强弱指标
     * RSI12 一般是12日相对强弱指标
     * RSI24一般是24日相对强弱指标
     * @param dayMin
     * @param dayMid
     * @param dayMax
     */
    private void getRSILineValue(int dayMin,int dayMid,int dayMax){

        for (int i=0;i<mData.size();i++){
            if (i>dayMin){
                rsiMinLineValue.add(new Entry(100-100/(1+getUps(i-dayMin,i)/getDowns(i-dayMin,i)),i));
            }
            if (i>dayMid){
                rsiMidLineValue.add(new Entry(100-100/(1+getUps(i-dayMid,i)/getDowns(i-dayMid,i)),i));
            }
            if (i>dayMax){
                rsiMaxLineValue.add(new Entry(100-100/(1+getUps(i-dayMax,i)/getDowns(i-dayMax,i)),i));
            }
        }
    }

    private int  getUps(int start ,int end){
        int sum=0;
        for (int i=start;i<=end;i++){

            if (mData.get(i).upDown==1){
                sum++;
            }
        }

        return sum;
    }
    private int  getDowns(int start ,int end){
        int sum=0;
        for (int i=start;i<=end;i++){

            if (mData.get(i).upDown==2){
                sum++;
            }
        }

        return sum;
    }

    /**
     * 默认 RSI6一般是6日相对强弱指标
     * @return
     */
    public List<Entry> getRsiMinLineValue() {
        return rsiMinLineValue;
    }
    /**
     * 默认 RSI12一般是12日相对强弱指标
     * @return
     */
    public List<Entry> getRsiMidLineValue() {
        return rsiMidLineValue;
    }
    /**
     * 默认 RSI24一般是24日相对强弱指标
     * @return
     */
    public List<Entry> getRsiMaxLineValue() {
        return rsiMaxLineValue;
    }
}
