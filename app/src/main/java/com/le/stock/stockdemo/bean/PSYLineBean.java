package com.le.stock.stockdemo.bean;

/**
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class PSYLineBean extends KLineBean {
    /**
     * 0、平盘 1、 代表上涨 2、代表下跌
     */
    public int upDown;

    /**
     * PSY=N日内上涨天数/N*100
     */
    public float psy;
}
