package com.le.stock.stockdemo.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.le.stock.stockdemo.R;
import com.le.stock.stockdemo.bean.DataParse;
import com.le.stock.stockdemo.bean.MinutesBean;
import com.le.stock.stockdemo.data.ConstantTest;
import com.le.stock.stockdemo.mychart.MyBarChart;
import com.le.stock.stockdemo.mychart.MyBottomMarkerView;
import com.le.stock.stockdemo.mychart.MyLeftMarkerView;
import com.le.stock.stockdemo.mychart.MyLineChart;
import com.le.stock.stockdemo.mychart.MyRightMarkerView;
import com.le.stock.stockdemo.mychart.MyXAxis;
import com.le.stock.stockdemo.mychart.MyYAxis;
import com.le.stock.stockdemo.utils.ChartUtils;
import com.le.stock.stockdemo.utils.MyUtils;
import com.le.stock.stockdemo.utils.VolFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by zhangzhenzhong on 2017/6/12.
 */

public class MinuteChartFragment extends Fragment {

    MyLineChart lineChart;

    MyBarChart barChart;
    private View rootView;
    private LineDataSet d1, d2;
    MyXAxis xAxisLine;
    MyYAxis axisRightLine;
    MyYAxis axisLeftLine;
    BarDataSet barDataSet;

    MyXAxis xAxisBar;
    MyYAxis axisLeftBar;
    MyYAxis axisRightBar;
    SparseArray<String> stringSparseArray;
    private DataParse mData;
    Integer sum = 0;
    List<Integer> listA, listB;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_minutes, null);
            init(rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void init(View view){

        initChart(view);

        stringSparseArray = setXLabels();

        /*网络数据*/
        //getMinutesData();
        /*离线测试数据*/
        getOffLineData();
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                barChart.setHighlightValue(new Highlight(h.getXIndex(), 0));

                barChart.highlightValue(new Highlight(h.getXIndex(), 0));

                // lineChart.setHighlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValue(null);
            }
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                lineChart.highlightValue(new Highlight(h.getXIndex(), 0));
                // lineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));//此函数已经返回highlightBValues的变量，并且刷新，故上面方法可以注释
                //barChart.setHighlightValue(h);
            }

            @Override
            public void onNothingSelected() {
                lineChart.highlightValue(null);
            }
        });
    }
    private void initChart(View view) {
        lineChart=(MyLineChart)view.findViewById(R.id.line_chart);
        barChart=(MyBarChart)view.findViewById(R.id.bar_chart);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderWidth(1);
        lineChart.setMinOffset(0);//设置默认图标边距
        barChart.setMinOffset(0);//设置默认图标边距
        lineChart.setBorderColor(getResources().getColor(R.color.gray_cbcbcb));
        lineChart.setDescription("");
//        lineChart.setHighlightFullBarEnabled(true);
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxisLine.setLabelsToSkip(59);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.enableGridDashedLine(10f,5f,0f);
        xAxisLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));


        //左边y
        axisLeftLine = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(true);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);
        axisLeftLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine.enableGridDashedLine(10f,5f,0f);
        axisLeftLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        //y轴样式
        this.axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });
        //右边y
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(5,true);
//        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        axisRightLine.setDrawLabels(true);
        axisRightLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        axisRightLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });



        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.gray_cbcbcb));
        barChart.setDescription("");

        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);

        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);
        xAxisBar.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisBar.enableGridDashedLine(10f,5f,0f);

        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(true);
        axisLeftBar.setLabelCount(3,true);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftBar.enableGridDashedLine(10f,5f,0f);

        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
    }


    private void setData(DataParse mData) {
        setMarkerView(mData);
        setShowLabels(stringSparseArray);
        Log.e("###", mData.getDatas().size() + "ee");
        if (mData.getDatas().size() == 0) {
            lineChart.setNoDataText("暂无数据");
            return;
        }
        //设置y左右两轴最大最小值
        axisLeftLine.setAxisMinValue(mData.getMin());
        axisLeftLine.setAxisMaxValue(mData.getMax());
        axisRightLine.setAxisMinValue(mData.getPercentMin());
        axisRightLine.setAxisMaxValue(mData.getPercentMax());


        axisLeftBar.setAxisMaxValue(mData.getVolmax());
        /*单位*/
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
//        axisLeftBar.setShowMaxAndUnit(unit);
        axisLeftBar.setDrawLabels(true);
        //axisLeftBar.setAxisMinValue(0);//即使最小是不是0，也无碍
//        axisLeftBar.setShowOnlyMinMax(true);
//        axisRightBar.setAxisMaxValue(mData.getVolmax());
//   axisRightBar.setAxisMinValue(mData.getVolmin);//即使最小是不是0，也无碍
//        axisRightBar.setShowOnlyMinMax(true);

        //基准线
//        LimitLine ll = new LimitLine(0);
//        ll.setLineWidth(1f);
//        ll.setLineColor(getResources().getColor(R.color.minute_jizhun));
//        ll.enableDashedLine(10f, 10f, 0f);
//        ll.setLineWidth(1);
//        axisRightLine.addLimitLine(ll);
//        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        Log.e("##", Integer.toString(xVals.size()));

        ArrayList<MinutesBean> beanDatas =mData.getDatas();
        for (int i = 0, j = 0; i < beanDatas.size(); i++, j++) {
           /* //避免数据重复，skip也能正常显示
            if (mData.getDatas().get(i).time.equals("13:30")) {
                continue;
            }*/
            MinutesBean t = beanDatas.get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }
            lineCJEntries.add(new Entry(beanDatas.get(i).cjprice, i,beanDatas));
            lineJJEntries.add(new Entry(beanDatas.get(i).avprice, i,beanDatas));

            int colorInt=0;//0、代表红色，1、代表绿色
            if (i%2==1){
                colorInt=0;
            }else{
                colorInt=1;
            }
            barEntries.add(new BarEntry(beanDatas.get(i).cjnum, i,colorInt));
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        barDataSet = new BarDataSet(barEntries, "成交量");

        d1.setHighLightColor(Color.BLACK);//设置选中时十字线的颜色
        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(getResources().getColor(R.color.minute_blue));
        d2.setColor(getResources().getColor(R.color.minute_yellow));
        d2.setHighlightEnabled(false);
//        d1.setDrawFilled(true);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(getMinutesCount(), sets);
        lineChart.setData(cd);

        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(Color.BLACK);//设置选中时十字线的颜色
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
//        barDataSet.setColor(Color.RED);
//        List<Integer> list=new ArrayList<>();
//        list.add(Color.RED);
//        list.add(Color.GREEN);
//        list.add(Color.BLUE);
//        barDataSet.setColors(list);

        BarData barData = new BarData(getMinutesCount(), barDataSet);
        barChart.setData(barData);

//        setOffset(lineChart,barChart);
        ChartUtils.setOffset(lineChart,barChart);
        lineChart.invalidate();//刷新图
        barChart.invalidate();
    }


    private void getOffLineData() {
           /*方便测试，加入假数据*/
        mData = new DataParse();
        JSONObject object = null;
        try {
            object = new JSONObject(ConstantTest.MINUTESURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mData.parseMinutes(object);
        setData(mData);
    }

    private SparseArray<String> setXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(121, "11:30/13:00");
        xLabels.put(182, "14:00");
        xLabels.put(241, "15:00");
        return xLabels;
    }



    public void setShowLabels(SparseArray<String> labels) {
        xAxisLine.setXLabels(labels);
        xAxisBar.setXLabels(labels);
    }

    public String[] getMinutesCount() {
        return new String[242];
    }

    private void setMarkerView(DataParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(getActivity(), R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(getActivity(), R.layout.mymarkerview);
        MyBottomMarkerView bottomMarkerView = new MyBottomMarkerView(getActivity(), R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView,bottomMarkerView, mData);
        barChart.setMarker(leftMarkerView, rightMarkerView,bottomMarkerView, mData);
    }
}
