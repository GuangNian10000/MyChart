package com.guangnian.chartlibrary;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

public class LineChartEntity extends BaseChartEntity<Entry> {

    public LineChartEntity (LineChart lineChart, List<List<Entry>>entries, String[] labels,
                            int []chartColor, int valueColor, float textSize,List<String> timeList) {
        super(lineChart, entries, labels, chartColor, valueColor, textSize,timeList);
    }

    public LineChartEntity (LineChart lineChart, List<List<Entry>>entries, String[] labels, boolean[] hasDotted,
                            int []chartColor, int valueColor, float textSize, List<String> timeList) {
        super(lineChart, entries, labels, chartColor, valueColor, textSize,hasDotted,timeList);
        this.hasDotted = hasDotted;
    }

    @Override
    protected void initChart() {
        super.initChart();
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getAxisLeft().enableGridDashedLine(10f, 15f, 0f);
        mChart.getAxisLeft().setGridLineWidth(0.5f);
        mChart.getAxisLeft().setGridColor(Color.parseColor("#f5f5f5"));
        mChart.getAxisLeft().setDrawZeroLine(false);
        mChart.getAxisRight().setDrawZeroLine(false);
        mChart.getAxisRight().setZeroLineWidth(0f);
        mChart.getAxisLeft().setZeroLineWidth(0f);
        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getXAxis().setDrawAxisLine(false);
    }

    @Override
    protected void setChartData() {
        LineDataSet[]lineDataSet = new LineDataSet[mEntries.size()];
            if (mChart.getData() != null && mChart.getData().getDataSetCount() == mEntries.size()) {
                for(int index = 0, len = mEntries.size(); index < len; index ++) {
                    List<Entry> list = mEntries.get(index);
                    lineDataSet[index] = (LineDataSet) mChart.getData().getDataSetByIndex(index);
                    lineDataSet[index].setValues(list);
                }
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            }  else {
                for (int index = 0, len = mEntries.size(); index < len; index ++) {
                    lineDataSet[index] = new LineDataSet(mEntries.get(index), labels[index]);
                    lineDataSet[index].setAxisDependency(YAxis.AxisDependency.LEFT);
                    lineDataSet[index].setColor(mChartColors[index]);
                    lineDataSet[index].setLineWidth(1.5f);
                    lineDataSet[index].setCircleRadius(3.5f);
                    lineDataSet[index].setCircleColor(mChartColors[index]);
                    lineDataSet[index].setFillAlpha(25);
                    lineDataSet[index].setDrawCircleHole(false);
                    lineDataSet[index].setValueTextColor(mChartColors[index]);
                    if (hasDotted!=null&&hasDotted[index]) {
                        lineDataSet[index].setDrawCircles(false);
                        lineDataSet[index].setCircleColor(new Color().WHITE);
                        lineDataSet[index].enableDashedLine(10f, 15f, 0f);
                        lineDataSet[index].enableDashedHighlightLine(10f, 15f, 0f);
                    }

                }
                LineData data = new LineData(lineDataSet);
                data.setValueTextSize(mTextSize);
                mChart.setData(data);
                mChart.invalidate();
                mChart.animateX(2000, Easing.EasingOption.EaseInOutQuad);
        }

    }

    /**
     * <p>填充曲线以下区域</p>
     * @param drawable 填充drawable
     * @param filledColor 填充颜色值
     * @param fill true:填充
     */
    public void toggleFilled(Drawable[]drawable, int []filledColor, boolean fill) {
        List<ILineDataSet> sets = ((LineChart)mChart).getData().getDataSets();

        for (int index = 0, len = sets.size(); index < len; index ++ ) {
            LineDataSet set = (LineDataSet) sets.get(index);
            if (drawable != null) {
                set.setFillDrawable(drawable[index]);
            } else if (filledColor != null){
                set.setFillColor(filledColor[index]);
            }
            set.setDrawFilled(fill);
        }
        mChart.invalidate();
    }

    /**
     * <p>绘制曲线上点</p>
     * @param draw true:绘制
     */
    public void drawCircle ( boolean draw) {
        List<ILineDataSet> sets = ((LineChart)mChart).getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            set.setDrawCircles(draw);
        }
        mChart.invalidate();
    }

    /**
     * 设置图表颜色值
     * @param mode LineDataSet.Mode
     */
    public void setLineMode (LineDataSet.Mode mode) {
        List<ILineDataSet> sets = ((LineChart)mChart).getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            set.setMode(mode);
        }
        mChart.invalidate();
    }

    /**
     * 设置图表颜色值,组合图
     * @param modes LineDataSet.Mode
     */
    public void setLineMode (LineDataSet.Mode[] modes) {
        List<ILineDataSet> sets = ((LineChart)mChart).getData().getDataSets();
        for (int index = 0, len = sets.size(); index < len; index ++) {
            LineDataSet set = (LineDataSet) sets.get(index);
            if (index < modes.length) {
                set.setMode(modes[index]);
            }
        }
        mChart.invalidate();
    }

    public void setEnableDashedLine (boolean enable) {
        List<ILineDataSet> sets = ((LineChart)mChart).getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            if (enable) {
                set.disableDashedLine();
            } else {
                set.enableDashedLine(10f, 5f, 0f);
                set.enableDashedHighlightLine(10f, 5f, 0f);
            }
        }
        mChart.invalidate();

    }

    /**设置x缩放的最小最大值*/
    public void setMinMaxScaleX(float minScaleX, float maxScaleX) {
        mChart.getViewPortHandler().setMinMaxScaleX(minScaleX, maxScaleX);
    }
}
