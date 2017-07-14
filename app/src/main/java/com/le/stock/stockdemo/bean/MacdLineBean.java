package com.le.stock.stockdemo.bean;

/**
 * Created by zhangzhenzhong on 2017/6/13.
 */
public class MacdLineBean extends KLineBean{
    //今天的指数12日平均值
    public float ema12;
    //今天的指数26日平均值
    public float ema26;
    //今天的指数9日平均值,计算不同于12 和26
    public float ema09;
}
