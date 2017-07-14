package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;
import com.le.stock.stockdemo.bean.KdjLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * KDJ指标又叫随机指标
 * Created by zhangzhenzhong on 2017/6/20.
 */

public class KDJLineCalculateUtils {
    List<Entry> kdj_k_data =new ArrayList<>();
    List<Entry> kdj_d_data =new ArrayList<>();
    List<Entry> kdj_j_data =new ArrayList<>();

    private List<KdjLineBean> mData=new ArrayList<>();

    public KDJLineCalculateUtils(List<KdjLineBean> data){
        this.mData=data;
    }

    /**
     * 未成熟随机指标值
     * KDJ的计算比较复杂，首先要计算周期（n日、n周等）的RSV值，即未成熟随机指标值，然后再计算K值、D值、J值等。
     * 以n日KDJ数值的计算为例，其计算公式为 n日RSV=（Cn－Ln）/（Hn－Ln）×100
     * 公式中，Cn为第n日收盘价；Ln为n日内的最低价；Hn为n日内的最高价。
     *
     * @param day
     */
    public void getKdjRsv(int day){
        day=9;//默认周期值为9,计算未成熟随机值

        for (int i=0;i<mData.size();i++){

            if (i<=day){
                float min=getMin(0,i);
                float max=getMax(0,i);
                mData.get(i).rsv=(mData.get(i).close-min)/(max-min)*100;
            }else{
                float min=getMin(i-9,i);
                float max=getMax(i-9,i);
                mData.get(i).rsv=(mData.get(i).close-min)/(max-min)*100;
            }
        }
        kdj_K_Data();//执行顺序 先有k
        kdj_D_Data();//执行顺序 再有d
        kdj_J_Data();//执行顺序 后有j
    }

    /**
     * n日内的最低价
     * @param a
     * @param b
     * @return
     */
    private float getMin(int a,int b) {
        float min=Float.MAX_VALUE;
        for (int i=a;i<b;i++) {
            if (min>mData.get(i).low){
                min=mData.get(i).low;
            }
        }
        return min;
    }

    /**
     * n日内的最高价
     * @param a
     * @param b
     * @return
     */
    private float getMax(int a,int b) {
        float max=Float.MIN_VALUE;
        for (int i=a;i<b;i++) {
            if (max<mData.get(i).high){
                max=mData.get(i).high;
            }
        }
        return max;
    }


    /**
     * 当日K值=2/3×前一日K值+1/3×当日RSV
     * 当日D值=2/3×前一日D值+1/3×当日K值
     * 若无前一日K 值与D值，则可分别用50来代替。
     */
    private void kdj_K_Data(){

        kdj_k_data =new ArrayList<>();
        for (int i=0;i<mData.size();i++){

            if (i == 0) {
                mData.get(i).k_value=50*2/3-mData.get(i).rsv/3;
            }else{
                mData.get(i).k_value=mData.get(i-1).k_value*2/3-mData.get(i).rsv/3;
            }
            kdj_k_data.add(new Entry(mData.get(i).k_value,i,"#c6a563"));//黄线
        }
    }
    /**
     * 当日K值=2/3×前一日K值+1/3×当日RSV
     * 当日D值=2/3×前一日D值+1/3×当日K值
     * 若无前一日K 值与D值，则可分别用50来代替。
     */
    private void kdj_D_Data(){
        kdj_d_data =new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            if (i == 0) {
                mData.get(i).d_value=50*2/3-mData.get(i).k_value/3;
            }else{
                mData.get(i).d_value=mData.get(i-1).d_value*2/3-mData.get(i).k_value/3;
            }
            kdj_d_data.add(new Entry(mData.get(i).d_value,i,"#0000ff"));//蓝线
        }
    }


    /**
     * J值=3*当日K值-2*当日D值
     */
    private  void kdj_J_Data(){
        kdj_j_data =new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            mData.get(i).j_value=mData.get(i).k_value*3-mData.get(i).d_value*2;
            kdj_j_data.add(new Entry(mData.get(i).j_value,i,"#ff0000"));//红线
        }
    }


    /**
     * 获取kdj 中所有k线指标数据
     * @return
     */
    public List<Entry> getKdj_k_data() {
        return kdj_k_data;
    }
    /**
     * 获取kdj 中所有d线指标数据
     * @return
     */
    public List<Entry> getKdj_d_data() {
        return kdj_d_data;
    }
    /**
     * 获取kdj 中所有j线指标数据
     * @return
     */
    public List<Entry> getKdj_j_data() {
        return kdj_j_data;
    }
}
