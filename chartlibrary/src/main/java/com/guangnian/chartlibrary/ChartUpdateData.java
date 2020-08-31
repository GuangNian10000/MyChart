package com.guangnian.chartlibrary;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 曲线图更新数据
 * */
public class ChartUpdateData {
    DecimalFormat mFormat = new DecimalFormat("#,###.##");
    /**
     * 双平滑曲线传入数据，添加markview，添加实体类单位
     *
     * @param yoyList
     * @param realList
     * @param lineChart
     * @param unit
     * @param labels
     */
    public void updateLinehart(Activity activity, final List<String> yoyList, final List<String> realList, List<List<Entry>> entries , LineChart lineChart,
                                final String unit, final List<String>timeList, final String[] labels,int chartType ) {
        Drawable[] drawables = {
                ContextCompat.getDrawable(activity, R.drawable.chart_thisyear_blue),
                ContextCompat.getDrawable(activity, R.drawable.chart_callserice_call_casecount)
        };
        int[] colors = {Color.parseColor("#45A2FF"), Color.parseColor("#5fd1cc")};
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors, Color.parseColor("#999999"), 12f,timeList);
        /**
         * 这里切换平滑曲线或者折现图
         */
        switch (chartType){
            case 1:
                lineChartEntity.setLineMode(LineDataSet.Mode.LINEAR);
                break;
            case 2:
                lineChartEntity.setLineMode(LineDataSet.Mode.STEPPED);
                break;
            case 3:
                lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER);
                break;
            case 4:
                lineChartEntity.setLineMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                break;
        }
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);

        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        if(labels.length<=1){
            lineChart.getLegend().setEnabled(false);
        }else{
            lineChart.getLegend().setEnabled(true);
        }
        lineChartEntity.setAxisFormatter(
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(timeList.size()==0){
                            return"";
                        }
                        return timeList.get((int) value % timeList.size());
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                },
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mFormat.format(value) + unit;
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });

        lineChartEntity.setDataValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value) + unit;
            }
        });

        final NewMarkerView markerView = new NewMarkerView(activity, R.layout.custom_marker_view_layout);
        markerView.setCallBack(new NewMarkerView.CallBack() {
            @Override
            public void onCallBack(float x, String value) {
                int index = (int) (x);
                if (index < 0) {
                    return;
                }
                if (index > yoyList.size() && index > realList.size()) {
                    return;
                }
                String textTemp = "";

                if (index <= yoyList.size()&&yoyList.size()>0) {
                    if (!StringUtils.isEmpty(textTemp)) {
                    }
                    textTemp +=yoyList.get(index) + unit;
                }

                if (index <= realList.size()&&realList.size()>0) {
                    textTemp += "\n";
                    textTemp += realList.get(index) + unit;
                }
                markerView.getTvContent().setText(textTemp);
            }
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
        lineChart.notifyDataSetChanged();
    }

    /**
     * 双平滑曲线添加线下的阴影
     *
     * @param lineChartEntity
     * @param drawables
     * @param colors
     */
    private void toggleFilled(LineChartEntity lineChartEntity, Drawable[] drawables, int[] colors) {
        if (android.os.Build.VERSION.SDK_INT >= 18) {

            lineChartEntity.toggleFilled(drawables, null, true);
        } else {
            lineChartEntity.toggleFilled(null, colors, true);
        }
    }
}
