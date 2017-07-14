package com.le.stock.stockdemo.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.le.stock.stockdemo.R;
import com.le.stock.stockdemo.bean.DataParse;
import com.le.stock.stockdemo.bean.KLineBean;
import com.le.stock.stockdemo.data.ConstantTest;
import com.le.stock.stockdemo.mychart.CoupleChartGestureListener;
import com.le.stock.stockdemo.mychart.MyCombinedChart;
import com.le.stock.stockdemo.mychart.MyYAxis;
import com.le.stock.stockdemo.utils.ChartUtils;
import com.le.stock.stockdemo.utils.MyUtils;
import com.le.stock.stockdemo.utils.VolFormatter;
import com.le.stock.stockdemo.view.SmartTabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhenzhong on 2017/6/12.
 */

public class DayChartFragment extends Fragment{

    private MyCombinedChart combinedchart;//k线图
    private MyCombinedChart indexCombinedchart;//指标图标
    private DataParse mData;
    private ArrayList<KLineBean> kLineDatas;
    private XAxis xAxisBar, xAxisK;
    MyYAxis axisLeftBar, axisLeftK;
    MyYAxis axisRightBar, axisRightK;
    BarDataSet barDataSet;
    /**
     * 指标tab
     **/
    private SmartTabLayout viewTab;
    float sum = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            indexCombinedchart.setAutoScaleMinMaxEnabled(true);
            combinedchart.setAutoScaleMinMaxEnabled(true);

            combinedchart.notifyDataSetChanged();
            indexCombinedchart.notifyDataSetChanged();

            combinedchart.invalidate();
            indexCombinedchart.invalidate();
        }
    };

    private  View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView==null){
            rootView=inflater.inflate(R.layout.fragment_kline,null);
            initView(rootView);
            initChart(rootView);
            if (combinedchart!=null){
//            combinedchart.clearValues();
                combinedchart.clear();
            }
            if (indexCombinedchart!=null){
//            indexCombinedchart.clearValues();
                indexCombinedchart.clear();
            }
            getOffLineData();
        }
        return rootView;
    }

    private void initView(View view){
        viewTab = (SmartTabLayout) view.findViewById(R.id.indicatrix_smart_tab);
        List<String> titles = new ArrayList<String>();
        titles.add("MA");
        titles.add("MACD");
        titles.add("BOLL");
        titles.add("KDJ");
        titles.add("WR");
        titles.add("RSI");
        titles.add("BIAS");
        titles.add("ARBR");
        titles.add("CCI");
        //添加标题
        viewTab.setTabText(titles, 0);
        viewTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getOffLineData() {
           /*方便测试，加入假数据*/
        mData = new DataParse();
        JSONObject object = null;
        try {
            object = new JSONObject(ConstantTest.KLINEURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mData.parseKLine(object);

//        mData.getKLineDatas();
        setData(mData);
    }
    private void initChart(View view) {
        combinedchart=(MyCombinedChart) view.findViewById(R.id.combinedchart);
        indexCombinedchart=(MyCombinedChart) view.findViewById(R.id.bottemcombinedchart);
        indexCombinedchart.setExtraBottomOffset(0);
        indexCombinedchart.setDrawBorders(true);
        indexCombinedchart.setBorderWidth(1);
        indexCombinedchart.setBorderColor(getResources().getColor(R.color.gray_cbcbcb));
        indexCombinedchart.setDescription("");
        indexCombinedchart.setDragEnabled(true);
        indexCombinedchart.setScaleYEnabled(false);
        indexCombinedchart.setMinOffset(0);//设置默认图标边距
        combinedchart.setMinOffset(0);//设置默认图标边距


        Legend barChartLegend = indexCombinedchart.getLegend();
        barChartLegend.setEnabled(false);

        //BarYAxisFormatter  barYAxisFormatter=new BarYAxisFormatter();
        //bar x y轴
        xAxisBar = indexCombinedchart.getXAxis();
        xAxisBar.setEnabled(false);
//        xAxisBar.setDrawLabels(false);
//        xAxisBar.setDrawGridLines(true);
//        xAxisBar.setDrawAxisLine(false);
//        xAxisBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
//        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
//        xAxisBar.enableGridDashedLine(10f,5f,0f);

        axisLeftBar = indexCombinedchart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setLabelCount(3,true);
        axisLeftBar.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftBar.setDrawGridLines(true);
//        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setDrawAxisLine(true);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.enableGridDashedLine(10f,5f,0f);


        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setSpaceTop(0);
//        axisLeftBar.setShowOnlyMinMax(true);//只显示两行覆盖setLabelCount()方法
        axisRightBar = indexCombinedchart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        axisRightBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisRightBar.enableGridDashedLine(10f,5f,0f);

        /****************************************************************/
        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(getResources().getColor(R.color.gray_cbcbcb));
        combinedchart.setDescription("");
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);

        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(true);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisK.enableGridDashedLine(10f,5f,0f);
        xAxisK.setAvoidFirstLastClipping(true);

        axisLeftK = combinedchart.getAxisLeft();
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftK.setDrawGridLines(true);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setLabelCount(5,true);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftK.enableGridDashedLine(10f,5f,0f);
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightK = combinedchart.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(false);
        axisRightK.setDrawAxisLine(false);
        combinedchart.setDragDecelerationEnabled(true);
        indexCombinedchart.setDragDecelerationEnabled(true);
//        combinedchart.setDragDecelerationFrictionCoef(0.2f);//设置滑动后速度，0---1，值越大滑动的越远
//        bottemcombinedchart.setDragDecelerationFrictionCoef(0.2f);//设置滑动后速度，0---1，值越大滑动的越远


        // 将K线控的滑动事件传递给交易量控件
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{indexCombinedchart}));
        // 将交易量控件的滑动事件传递给K线控件
        indexCombinedchart.setOnChartGestureListener(new CoupleChartGestureListener(indexCombinedchart, new Chart[]{combinedchart}));

        indexCombinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Log.e("%%%%", h.getXIndex() + "");
                combinedchart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
            }
        });
        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                indexCombinedchart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                indexCombinedchart.highlightValue(null);
            }
        });


    }

    private float getSum(Integer a, Integer b) {

        for (int i = a; i <= b; i++) {
            sum += mData.getKLineDatas().get(i).close;
        }
        return sum;
    }

    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }

    private void setData(DataParse mData) {

        kLineDatas = mData.getKLineDatas();
        int size = kLineDatas.size();   //点的个数
        // axisLeftBar.setAxisMaxValue(mData.getVolmax());
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }        //设置y左右两轴最大最小值
        axisLeftBar.setAxisMaxValue(mData.getVolmax());
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setLabelCount(3,true);
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        // axisRightBar.setAxisMaxValue(mData.getVolmax());
        Log.e("@@@", mData.getVolmax() + "da");

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line30Entries = new ArrayList<>();
        for (int i = 0, j = 0; i <kLineDatas.size(); i++, j++) {
            xVals.add(mData.getKLineDatas().get(i).date + "");
            barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i));
            candleEntries.add(new CandleEntry(i, kLineDatas.get(i).high,kLineDatas.get(i).low,kLineDatas.get(i).open, kLineDatas.get(i).close));
            if (i >= 4) {
                sum = 0;
                line5Entries.add(new Entry(getSum(i - 4, i) / 5, i));
            }
            if (i >= 9) {
                sum = 0;
                line10Entries.add(new Entry(getSum(i - 9, i) / 10, i));
            }
            if (i >= 29) {
                sum = 0;
                line30Entries.add(new Entry(getSum(i - 29, i) / 30, i));
            }

        }
        barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setDrawValues(true);
        barDataSet.setColor(Color.RED);
        BarData barData = new BarData(xVals, barDataSet);
        CombinedData bottemCombinedData=new CombinedData(xVals);//不传值无法运行
        bottemCombinedData.setData(barData);
        indexCombinedchart.setData(bottemCombinedData);

        final ViewPortHandler viewPortHandlerBar = indexCombinedchart.getViewPortHandler();
        viewPortHandlerBar.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        final float xscale = 3;
        touchmatrix.postScale(xscale, 1f);


        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(Color.BLACK);
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setDrawValues(true);
        candleDataSet.setColor(Color.RED);
        candleDataSet.setShadowWidth(1f);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        CandleData candleData = new CandleData(xVals, candleDataSet);


        ArrayList<ILineDataSet> sets = new ArrayList<>();

        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        if(size>=30){
            sets.add(setMaLine(5, xVals, line5Entries));
            sets.add(setMaLine(10, xVals, line10Entries));
            sets.add(setMaLine(30, xVals, line30Entries));
        }else if (size>=10&&size<30){
            sets.add(setMaLine(5, xVals, line5Entries));
            sets.add(setMaLine(10, xVals, line10Entries));
        }else if (size>=5&&size<10) {
            sets.add(setMaLine(5, xVals, line5Entries));
        }


        CombinedData combinedData = new CombinedData(xVals);
        LineData lineData = new LineData(xVals, sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);
        combinedchart.moveViewToX(kLineDatas.size() - 1);
        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size()));


        //设置图表默认的放大倍数
        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
        final float xscaleCombin = 3;
        matrixCombin.postScale(xscaleCombin, 1f);

        combinedchart.moveViewToX(kLineDatas.size() - 1);
        indexCombinedchart.moveViewToX(kLineDatas.size() - 1);
        ChartUtils.setOffset(combinedchart,indexCombinedchart);

        /****************************************************************************************
         此处解决方法来源于CombinedChartDemo，k线图y轴显示问题，图表滑动后才能对齐的bug，希望有人给出解决方法
         (注：此bug现已修复，感谢和chenguang79一起研究)
         ****************************************************************************************/

        handler.sendEmptyMessageDelayed(0, 300);

    }

    @NonNull
    private LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(Color.WHITE);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(Color.GREEN);
        } else if (ma == 10) {
            lineDataSetMa.setColor(Color.GRAY);
        } else {
            lineDataSetMa.setColor(Color.YELLOW);
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }
}
