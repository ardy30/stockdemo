package com.le.stock.stockdemo.utils;

import com.github.mikephil.charting.data.Entry;
import com.le.stock.stockdemo.bean.DMILineBean;
import com.le.stock.stockdemo.bean.KLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * DMI 指标又叫动向指标或趋向指标,是一种中长期股市技术分析方法。
 * Created by zhangzhenzhong on 2017/6/21.
 */

public class DMILineCalculateUtils {


    List<DMILineBean> mData=new ArrayList<>();

    List<Entry>  pdmLineData=new ArrayList<>();
    List<Entry>  mdiLineData=new ArrayList<>();
    List<Entry>  adxLineData=new ArrayList<>();
    List<Entry>  adxrLineData=new ArrayList<>();
    int day=12;//以12日作为默认计算周期为例

    public DMILineCalculateUtils(List<DMILineBean> data){
        this.mData=data;
        getDmiDMValue();
        getDmiTRValue();
        getDmiDiValue(day);
        getDmiADXValue();
        getDmiADXRValue(day);
    }


    /**
     * 上升动向（+DM）和  下降动向（-DM）
     * A、上升动向（+DM）
     * +DM代表正趋向变动值即上升动向值，其数值等于当日的最高价减去前一日的最高价，如果<=0 则+DM=0。
     * B、下降动向（-DM）
     * ﹣DM代表负趋向变动值即下降动向值，其数值等于前一日的最低价减去当日的最低价，如果<=0 则-DM=0。注意-DM也是非负数。
     * 再比较+DM和-DM，较大的那个数字保持，较小的数字归0。
     * C、无动向
     * 无动向代表当日动向值为“零”的情况，即当日的+DM和﹣DM同时等于零。有两种股价波动情况下可能出现无动向。
     * 一是当当日的最高价低于前一日的最高价并且当日的最低价高于前一日的最低价，二是当上升动向值正好等于下降动向值。
     */
    private void getDmiDMValue(){
        for (int i=0;i<mData.size();i++){
            float pDm=0;
            float mDm=0;
            if (i==0){
                pDm=mData.get(i).high;
                if (pDm<0){
                    pDm=0;
                }
                mDm=0-mData.get(i).low;
                if (mDm<0){
                    mDm=0;
                }
            }else{
                pDm=mData.get(i).high-mData.get(i-1).high;
                if (pDm<0){
                    pDm=0;
                }
                mDm=mData.get(i-1).low-mData.get(i).low;
                if (mDm<0){
                    mDm=0;
                }
            }
            if (pDm>mDm){
                mDm=0;
            }else if (pDm==mDm){
                pDm=0;
                mDm=0;
            }else{
                pDm=0;
            }
            mData.get(i).plusDM=pDm;//最终均为非负数
            mData.get(i).minusDM=mDm;//最终均为非负数
        }
    }


    /**
     * TR代表真实波幅，是当日价格较前一日价格的最大变动值。取以下三项差额的数值中的最大值（取绝对值）为当日的真实波幅：
     * A、当日的最高价减去当日的最低价的价差。
     * B、当日的最高价减去前一日的收盘价的价差。
     * C、当日的最低价减去前一日的收盘价的价差。
     * TR是A、B、C中的数值最大者
     */
    private void getDmiTRValue(){
        for (int i=0;i<mData.size();i++){

            //当日的最高价减去当日的最低价的价差。
            float a=Math.abs(mData.get(i).high-mData.get(i).low);
            //当日的最高价减去前一日的收盘价的价差。
            float b;
            //当日的最低价减去前一日的收盘价的价差。
            float c;
            if (i==0){
                b=Math.abs(mData.get(i).high-0);//为0不可能
                c=Math.abs(mData.get(i).low-0);
            }else{
                b=Math.abs(mData.get(i).high-mData.get(i-1).close);
                c=Math.abs(mData.get(i).low-mData.get(i-1).close);
            }
            //A、B、C中的数值最大者
            float max;
            if (a<b){
                max=b;
            }else{
                max=a;
            }
            if (max<c){
                max=c;
            }
            mData.get(i).tr=max;
        }
    }


    /**
     * 首先必须求得平滑系数。所谓的系数，则是移动平均周期之单位数，如几天，几周等等。其公式如下：
     *　平滑系数＝2÷（周期单位数＋1 ）如12日EMA的平滑系数＝2÷（12＋1）＝0．1538；
     *
     * 今天的指数平均值＝平滑系数×（今天收盘指数－昨天的指数平均值）＋昨天的指数平均值。
     * 依公式可计算出12日EMA
     * 12日EMA＝2÷13×（今天收盘指数一昨天的指数平均值）＋昨天的指数平均值。
     * ＝（2÷13）×今天收盘指数＋（11÷13）×昨天的指数平均值。
     *
     *
     * 要使方向线具有参考价值，则必须运用平滑移动平均的原理对其进行累积运算。以12日作为计算周期为例，
     * 先将12日内的+DM、-DM及TR平均化，所得数值分别为+DM12，-DM12和TR12，具体如下：
     * +DI（12）=（+DM12÷TR12）×100
     * -DI（12）=（-DM12÷TR12）×100
     * @param day
     */
    private void getDmiDiValue(int day){
        List<Entry> lineDayEntries = new ArrayList<>();
        for (int i=0;i<mData.size();i++){
            float plusDM12 = 0;
            float minusDM12=0;
            float tr12=0;
            if (i==day){
                plusDM12=mData.get(i).plusDM*2/(day+1);
                minusDM12=mData.get(i).minusDM*2/(day+1);
                tr12=mData.get(i).tr*2/(day+1);//为0不可能
            }
            if (i>day){
                plusDM12=mData.get(i).plusDM*2/(day+1)+(day-1)/(day+1)*mData.get(i-1).plusDM;
                minusDM12=mData.get(i).minusDM*2/(day+1)+(day-1)/(day+1)*mData.get(i-1).minusDM;
                tr12=mData.get(i).tr*2/(day+1)+(day-1)/(day+1)*mData.get(i-1).tr;
            }
            //+DI=（+DM÷TR）×100
            mData.get(i).pdm=plusDM12/tr12*100;
            //-DI=（-DM÷TR）×100
            mData.get(i).mdi=minusDM12/tr12*100;
            pdmLineData.add(new Entry(mData.get(i).pdm,i,"#c6a563"));//黄线
            mdiLineData.add(new Entry(mData.get(i).mdi,i,"#0000ff"));//蓝线
        }
    }


    /**
     * 计算动向平均数ADX
     * 依据DI值可以计算出DX指标值。其计算方法是将+DI和—DI间的差的绝对值除以总和的百分比得到动向指数DX。由于DX的波动幅度比较大，一般以一定的周期的平滑计算，
     * 得到平均动向指标ADX。具体过程如下：
     * DX=(DI DIF÷DI SUM) ×100
     * 其中，DI DIF为上升指标和下降指标的差的绝对值
     * DI SUM为上升指标和下降指标的总和
     * ADX就是DX的一定周期n的移动平均值。
     *
     * 由于DI已经计算出
     */
    private void getDmiADXValue(){

        for (int i=0;i<mData.size();i++){
            mData.get(i).adx=Math.abs(mData.get(i).pdm-mData.get(i).mdi)/(mData.get(i).pdm+mData.get(i).mdi)*100;
            adxLineData.add(new Entry(mData.get(i).adx,i,"#db7093"));//紫色线
        }
    }

    /**
     * 计算评估数值ADXR
     * 在DMI指标中还可以添加ADXR指标，以便更有利于行情的研判。
     * ADXR的计算公式为：
     * ADXR=（当日的ADX+前n日的ADX）÷2
     * n为选择的周期数
     *
     * 和其他指标的计算一样，由于选用的计算周期的不同，DMI指标也包括日DMI指标、周DMI指标、月DMI指标年DMI指标以及分钟DMI指标等各种类型。
     * 经常被用于股市研判的是日DMI指标和周DMI指标。
     */
    private void getDmiADXRValue(int day){

        for (int i=0;i<mData.size();i++){
            float adxr=0;
            if (i==day){
                adxr=mData.get(i).adx;
            }
            if (i>day){
                adxr=(mData.get(i).adx+mData.get(i-day).adx)/2;
            }
            adxrLineData.add(new Entry(adxr,i,"#00ff00"));//绿线
        }
    }

    public List<Entry> getPdmLineData() {
        return pdmLineData;
    }

    public List<Entry> getMdiLineData() {
        return mdiLineData;
    }

    public List<Entry> getAdxLineData() {
        return adxLineData;
    }

    public List<Entry> getAdxrLineData() {
        return adxrLineData;
    }
}
