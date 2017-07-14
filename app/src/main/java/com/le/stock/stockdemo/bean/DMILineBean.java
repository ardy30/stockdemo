package com.le.stock.stockdemo.bean;

/**
 * Created by zhangzhenzhong on 2017/6/13.
 */
public class DMILineBean extends KLineBean{

    /**
     * 上升动向（+DM）
     */
    public float plusDM;

    /**
     * 下降动向（-DM）
     */
    public float minusDM;

    /**
     * 计算真实波幅（TR）
     */
    public float tr;


    /**
     * +DI（即PDI）
     * +DI代表上升方向线
     */
    public float pdm;
    /**
     * -DI（即MDI）
     * -DI代表下降方向线
     */
    public float mdi;

    /**
     * 计算动向平均数ADX
     * ADX就是DX的一定周期n的移动平均值。
     * DX=(DI DIF÷DI SUM) ×100
     */
    public float adx;

}
