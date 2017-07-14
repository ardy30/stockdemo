package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * DMA指标又叫平行线差指标，是目前股市分析技术指标中的一种中短期指标
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class DMALineCalculateUtils {




    private List<KLineBean> mData;
    private List<Entry> mAMAValues=new ArrayList<>();//10日周期移动平均值
    private List<Entry> mDMAValues=new ArrayList<>();//50日周期移动平均值

    private int day01=10;
    private int day02=50;

    public DMALineCalculateUtils(List<KLineBean> data){
        this.mData=data;
        getDMALineVaule(10,50);
    }


    /**
     * DMA指标又叫平行线差指标，是目前股市分析技术指标中的一种中短期指标，它常用于大盘指数和个股的研判。
     * DMA指标的计算方法
     * DMA指标的计算方法比较简单，其计算过程如下：
     * DMA=短期MA平均值—长期MA平均值
     * AMA=短期MA平均值
     * 以求10日、50日为基准周期的DMA指标为例，其计算过程具体如下：
     * DMA（10）=10日MA平均值—50日MA平均值
     * AMA（10）=10日MA平均值
     * @param day01  默认值为10
     * @param day02  默认值为50
     */
    private void getDMALineVaule(int day01,int day02){

        for (int i=0;i<mData.size();i++){

            if (i>day01){//10日不满足50日周期对比

            }
            if (i>day02){
                float ama=getSum(i-10,i)/10;
                float ma50=getSum(i-50,i)/10;
                mAMAValues.add(new Entry(ama,i,"#c6a563"));//黄线
                mDMAValues.add(new Entry(ama-ma50,i,"#0000ff"));//蓝线
            }
        }
    }
    private float getSum(Integer a, Integer b) {

        float sum=0;
        for (int i = a; i <= b; i++) {
            sum += mData.get(i).close;
        }
        return sum;
    }

}
