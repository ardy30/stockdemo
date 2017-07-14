package com.le.stock.stockdemo.bean;

/**
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class TRIXLineBean extends KLineBean {
    /**
     * TR=收盘价的N日指数移动平均值(平滑移动平均值)
     */
    public float tr;


    /**
     * N日的TRIX值，用于计算TRIX的M日简单移动平均
     */
    public float trix;

}
