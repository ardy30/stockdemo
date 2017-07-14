package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.utils.Utils;

/**
 * Created by zhangzhenzhong on 2017/6/12.
 */

public class ChartUtils {

    /*设置量表对齐*/
    public  static void  setOffset(BarLineChartBase chart1, BarLineChartBase chart2){

        float lineLeft = chart1.getViewPortHandler().offsetLeft();
        float barLeft = chart2.getViewPortHandler().offsetLeft();
        float lineRight = chart1.getViewPortHandler().offsetRight();
        float barRight = chart2.getViewPortHandler().offsetRight();
        float barBottom = chart2.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            // barChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            chart1.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }

  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            //offsetRight = Utils.convertPixelsToDp(lineRight);
            //barChart.setExtraRightOffset(offsetRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            chart1.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        chart2.setViewPortOffsets(transLeft, 5, transRight, barBottom);

    }

}
