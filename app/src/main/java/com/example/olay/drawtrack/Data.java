package com.example.olay.drawtrack;

import android.util.Log;

import com.esri.core.geometry.Point;


/**
 * 数据类
 * Created by OlAy on 2017/8/16.
 */

public class Data {
    public String data;
    public Point point;

    public Data(String data, Point point) {
        this.data = data;
        this.point = point;
    }
}
