package com.le.stock.stockdemo.bean;

/**
 * Created by zhangzhenzhong on 2017/6/22.
 */

public class CRLineBean extends KLineBean {
    /**
     * YM表示昨日（上一个交易日）的中间价
     * 1、M=（2C+H+L）÷4
     * 2、M=（C+H+L+O）÷4
     * 3、M=（C+H+L）÷3
     * 4、M=（H+L）÷2
     */
    public float m;
}
