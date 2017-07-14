package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.CRLineBean;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 所谓CR指标指的就是能量指标，CR指标又叫中间意愿指标、价格动量指标，
 * 它和AR、BR指标有很多相似之处，但更有自己独特的研判功能，是分析股市多空双方力量对比、
 * 把握买卖股票时机的一种中长期技术分析工具。
 *
 * CR指标的理论出发点是：中间价是股市最有代表性的价格。
 * Created by zhangzhenzhong on 2017/6/23.
 */

public class CRLineCalculateUtils {

    private List<Entry> mCRLineValues=new ArrayList<>();
    private List<Entry> mCRMa1LineValues=new ArrayList<>();
    private List<Entry> mCRMa2LineValues=new ArrayList<>();
    private List<Entry> mCRMa3LineValues=new ArrayList<>();
    private List<CRLineBean> mData;

    private ThreadLocal<List<CRLineBean>> crBean=new ThreadLocal<>();
    private int day=7;

    public CRLineCalculateUtils(List<CRLineBean> data){
        this.mData=data;
        getCR_M1_Data();
        getCR_M2_Data();
        getCR_M3_Data();
        getCR_M4_Data();
    }

    /**
     * CR（N日）=P1÷P2×100
     * 式中，P1=Σ（H－YM），表示N日以来多方力量的总和；
     * P2=Σ（YM－L），表示N日以来空方力量的总和。
     * H表示今日的最高价，L表示今日的最低价
     * YM表示昨日（上一个交易日）的中间价。
     */
    private List<Entry> getCRLineValues(int day,String color){//再执行算出cr
        List<Entry> mEntry=new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            if (i>day){
                float p1=getSumP1(i-day,i);
                float p2=getSumP2(i-day,i);
                mEntry.add( new Entry(p1/p2*100,i,color));
            }
        }
        return mEntry;
    }




    /**
     * CR计算公式中的中间价其实也是一个指标，
     * 它是通过对昨日（YM）交易的最高价、最低价、开盘家和收盘价进行加权平均而得到的，
     * 其每个价格的权重可以人为地选定。比较常用地中间价计算方法有四种：
     * 1、M=（2C+H+L）÷4
     * 2、M=（C+H+L+O）÷4
     * 3、M=（C+H+L）÷3
     * 4、M=（H+L）÷2
     *
     *
     * 使用m1方案
     */
    private void getCR_M1_Data(){//先执行得到m

        mData=crBean.get();
        for (int i=0;i<mData.size();i++){
            if (i==1){
                mData.get(i).m=0;
            }
            if (i>1){
                mData.get(i).m=getM1(i);//使用M1方案
            }
        }

        mCRLineValues=getCRLineValues(day,"#c6a563");//黄线
    }

    /**
     * 使用M2方案
     */
    private void getCR_M2_Data(){
        mData=crBean.get();
        for (int i=0;i<mData.size();i++){
            if (i==1){
                mData.get(i).m=0;
            }
            if (i>1){
                mData.get(i).m=getM2(i);
            }
        }
        mCRMa1LineValues=getCRLineValues(day,"#ff0000");//红线
    }

    /**
     * 使用M3方案
     */
    private void getCR_M3_Data(){
        mData=crBean.get();
        for (int i=0;i<mData.size();i++){
            if (i==1){
                mData.get(i).m=0;
            }
            if (i>1){
                mData.get(i).m=getM3(i);
            }
        }
        mCRMa2LineValues=getCRLineValues(day,"#00ff00");//绿线
    }

    /**
     * 使用M4方案
     */
    private void getCR_M4_Data(){
        mData=crBean.get();
        for (int i=0;i<mData.size();i++){
            if (i==1){
                mData.get(i).m=0;
            }
            if (i>1){
                mData.get(i).m=getM4(i);
            }
        }
        mCRMa3LineValues=getCRLineValues(day,"#0000ff");//蓝线
    }
    /**
     * 1、M=（2C+H+L）÷4
     */
    private float getM1(int i){
        CRLineBean bean=mData.get(i-1);
        return (bean.close*2+bean.high+bean.low)/4;
    }
    /**
     * 2、M=（C+H+L+O）÷4
     */
    private float getM2(int i){
        CRLineBean bean=mData.get(i-1);
        return (bean.close+bean.high+bean.low+bean.open)/4;
    }
    /**
     * 3、M=（C+H+L）÷3
     */
    private float getM3(int i){
        CRLineBean bean=mData.get(i-1);
        return (bean.close+bean.high+bean.low)/3;
    }
    /**
     * 4、M=（H+L）÷2
     */
    private float getM4(int i){
        CRLineBean bean=mData.get(i-1);
        return (bean.high+bean.low)/2;
    }

    /**
     * P1=Σ（H－YM），表示N日以来多方力量的总和
     * @param a
     * @param b
     * @return
     */
    private  float getSumP1(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).high-mData.get(i).m;
        }
        return sum;
    }

    /**
     * P2=Σ（YM－L），表示N日以来空方力量的总和。
     * @param a
     * @param b
     * @return
     */
    private  float getSumP2(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum +=mData.get(i).m- mData.get(i).low;
        }
        return sum;
    }


    public List<Entry> getmCRLineValues() {
        return mCRLineValues;
    }

    public List<Entry> getmCRMa1LineValues() {
        return mCRMa1LineValues;
    }

    public List<Entry> getmCRMa2LineValues() {
        return mCRMa2LineValues;
    }

    public List<Entry> getmCRMa3LineValues() {
        return mCRMa3LineValues;
    }
}
