package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;
import com.le.stock.stockdemo.bean.PSYLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 心理线（PSY）指标是研究投资者对股市涨跌产生心理波动的情绪指标。它对股市短期走势的研判具有一定的参考意义。
 * Created by zhangzhenzhong on 2017/6/23.
 */

public class PSYLineCalculateUtils {



    private List<Entry> mPsyMaLineValues=new ArrayList<>();
    private List<PSYLineBean> mData=new ArrayList<>();
    private int dayN=12;//.参数N设置为12日
    private int dayM=6;//参数M设置为6日
    public PSYLineCalculateUtils(List<PSYLineBean> data){

        this.mData=data;
        getUpOrDown();
        getPsyData(dayN);
        getPsyMaLineData(dayN,dayM);
    }


    /**
     *
     */
    private void getPsyData(int day){

        for (int i=0;i<mData.size();i++){
            if (i>day){
                //1.PSY=N日内上涨天数/N*100
                mData.get(i).psy=getUps(i-day,i)/day*100;
            }
        }
    }


    /**
     * 2.PSYMA=PSY的M日简单移动平均
     * 3.参数N设置为12日，参数M设置为6日
     * @param dayM
     * @param dayN
     */
    private void getPsyMaLineData(int dayN,int dayM){
        for (int i=0;i<mData.size();i++){
            if (i>dayN){
                mPsyMaLineValues.add(new Entry(getSum(i-dayM,i)/dayM,i,"#c6a563"));//黄线
            }
        }
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
    private int  getUps(int start ,int end){
        int sum=0;
        for (int i=start;i<=end;i++){

            if (mData.get(i).upDown==1){
                sum++;
            }
        }

        return sum;
    }
//    private int  getDowns(int start ,int end){
//        int sum=0;
//        for (int i=start;i<=end;i++){
//
//            if (mData.get(i).upDown==2){
//                sum++;
//            }
//        }
//
//        return sum;
//    }

    private  float getSum(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).vol;
        }
        return sum;
    }


    public List<Entry> getmPsyMaLineValues() {
        return mPsyMaLineValues;
    }
}
