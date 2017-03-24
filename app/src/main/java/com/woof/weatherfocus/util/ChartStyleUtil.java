package com.woof.weatherfocus.util;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.ValueShape;

/**
 * Created by Woof on 3/11/2017.
 */

public class ChartStyleUtil {


    public Line getChartStyle(int color, Line line) {
        /**
         * 设置线图的样式
         */
        line.setColor(color);
        line.setShape(ValueShape.CIRCLE); // 设置对应点形状
        line.setCubic(true); // 设置linechart形状，曲线或者折现
        line.setFilled(true); // 是否填充曲线下部区域
        line.setHasLabels(true); // 对曲线坐标是否加上备注
        line.setHasLines(true); // 是否用曲线进行表示，否则用点进行表示
        line.setHasPoints(true); // 是否圆点进行显示
        line.setHasLabelsOnlyForSelected(false); // 设置节点点击的效果
        return line;
    }

    public LineChartData getChartData(List<Line> lines) {

        LineChartData data = new LineChartData();
        data.setLines(lines);
        return data;
    }

    public void setAxis(Axis axisX, Axis axisY, List<AxisValue> axisValue, LineChartData data) {
        axisX.setHasTiltedLabels(false); //坐标轴标题显示斜体或者正常体
        axisX.setTextColor(Color.BLACK);
        axisX.setTextSize(6); // 设置字体大小
        axisX.setLineColor(Color.parseColor("#ff9800"));
        axisX.setTextColor(Color.parseColor("#ff9800"));
        axisX.setMaxLabelChars(7);
        axisX.setValues(axisValue);
        axisX.setHasLines(false); // 设置分割线

        axisY.setName("气温");
        axisY.setTextSize(10);
        axisY.setHasLines(false);
        axisY.setTextColor(Color.parseColor("#ff9800"));
        axisY.setLineColor(Color.parseColor("#ff9800"));
//        axisY.setMaxLabelChars(16);
//        List<AxisValue> values = new ArrayList<>();
//        for (int i = -20; i < 50; i += 4) {
//            AxisValue value = new AxisValue(i);
//            String label = "";
//            value.setLabel(label);
//            values.add(value);
//        }
//        axisY.setValues(values);

        data.setAxisXBottom(axisX); // 将x轴设置在底部
        data.setAxisYLeft(axisY); // y轴设定在左侧


    }

}
