package com.le.stock.stockdemo.bean;

/**
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class BIASLineBean extends KLineBean {
    /**
     * 乖离率，是用百分比来表示价格与MA间的偏离程度(差距率)。
     * BIAS=(收盘价-收盘价的N日简单平均)/收盘价的N日简单平均*100
     */
    public float BIAS1_L1;
    /**
     * 乖离率，是用百分比来表示价格与MA间的偏离程度(差距率)。
     * BIAS=(收盘价-收盘价的N日简单平均)/收盘价的N日简单平均*100
     */
    public float BIAS1_L2;
    /**
     * 乖离率，是用百分比来表示价格与MA间的偏离程度(差距率)。
     * BIAS=(收盘价-收盘价的N日简单平均)/收盘价的N日简单平均*100
     */
    public float BIAS1_L3;
}
