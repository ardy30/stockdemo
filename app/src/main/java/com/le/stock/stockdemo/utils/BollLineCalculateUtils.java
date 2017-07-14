package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhenzhong on 2017/6/19.
 */

public class BollLineCalculateUtils {


    private List<KLineBean> data=new ArrayList<>();


    public BollLineCalculateUtils(List<KLineBean> data){
        this.data=data;
    }


    /**
     * MA线，Boll线中的重要参考参数
     * MA=N日内的收盘价之和÷N
     * @param Day 指定周期
     * @return
     */
//    public List<Entry> BollMID(int Day){
//        ArrayList<Entry> lineDayEntries = new ArrayList<>();
//        for (int i = 0, j = 0; i <data.size(); i++, j++) {
//            if (i >= Day) {
//                lineDayEntries.add(new Entry(getSum(i - (Day-1), i) / Day, i));
//            }
//        }
//        return lineDayEntries;
//    }

    /**
     * MA线，Boll线中的重要参考参数
     * MA=N日内的收盘价之和÷N
     * @param Day 指定周期
     * @return
     */
    public List<Float> getBollMID(int Day){
        ArrayList<Float> lineDayEntries = new ArrayList<>();
        for (int i = 0, j = 0; i <data.size(); i++) {
            if (i >= Day) {
                lineDayEntries.add(getSum(i - (Day-1), i)/ Day);
            }
        }
        return lineDayEntries;
    }


    /**
     * 计算标准差MD
     * MD=平方根N日的（C－MA）的两次方之和除以N
     * @param Day 指定周期
     * @return
     */
    public float getBollMD(int Day){

        if (data==null||data.size()==0){
            return 0;
        }
        List<Float> ma=getBollMID(Day);
        float md=0;
        for(int i=0;i<data.size();i++){
            if (i>=Day){
                md+=Math.pow(data.get(i).close-ma.get(i-Day),2);
            }
        }
        md=md/data.size();
        md=(float) Math.sqrt(md);
        return md;
    }

    /**
     *  中轨线 MB=（N－1）日的MA线
     *  中轨线=N日的移动平均线
     * @param Day
     * @return
     */
    public List<Entry> getBollMB(int Day){
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i = 0, j = 0; i <data.size(); i++, j++) {
            if (i >= Day) {
                lineDayEntries.add(new Entry(getSum(i - (Day-1), i) / Day, i));
            }
        }
        return lineDayEntries;
    }


    /**
     * 上轨线 UP=MB+2×MD
     * 上轨线=中轨线+两倍的标准差
     * @param Day
     * @return
     */
    public List<Entry> getBollUp(int Day){
        float md=getBollMD(Day);
        ArrayList<Entry> lineDayEntries = new ArrayList<>();
        for (int i = 0, j = 0; i <data.size(); i++, j++) {
            if (i >= Day) {
                lineDayEntries.add(new Entry(getSum(i - (Day-1), i) / Day+2*md, i));
            }
        }
        return lineDayEntries;
    }

    /**
     * 下轨线 DN=MB－2×MD
     *  下轨线=中轨线－两倍的标准差
     * @param Day
     * @return
     */
    public List<Entry> getBollDN(int Day){
        float md=getBollMD(Day);
        ArrayList<Entry> lineDayEntries = new ArrayList<>();
        for (int i = 0, j = 0; i <data.size(); i++, j++) {
            if (i >= Day) {
                lineDayEntries.add(new Entry(getSum(i - (Day-1), i) / Day-2*md, i));
            }
        }
        return lineDayEntries;
    }
    public  float getSum(Integer a, Integer b) {

        int sum=0;
        for (int i = a; i <= b; i++) {
            sum += data.get(i).close;
        }
        return sum;
    }
}
