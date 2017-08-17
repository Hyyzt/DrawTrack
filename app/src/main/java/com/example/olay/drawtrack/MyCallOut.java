package com.example.olay.drawtrack;

import android.content.Context;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

/**
 * 时间气泡
 * Created by Luke on 2017/8/16.
 */

public class MyCallOut {

    private MapView mMapView;
    private Context mContext;
    private Callout mCallOut;

    public MyCallOut(MapView mapView, Context context) {
        mMapView = mapView;
        mContext = context;
        initCallOut();
    }

    public void initCallOut() {
        mCallOut = mMapView.getCallout();
        // 设置尺寸
        mCallOut.setMaxWidth(600);
        mCallOut.setMaxHeight(300);
        //设设置样式
        CalloutStyle calloutStyle = new CalloutStyle();
        calloutStyle.setAnchor(Callout.ANCHOR_POSITION_LOWER_MIDDLE);
        mCallOut.setStyle(calloutStyle);
    }

    public void show(Point point, String text) {
        TextView tv = new TextView(mContext);
        tv.setText(text);
        mCallOut.setContent(tv);
        mCallOut.show(point);
    }
}
