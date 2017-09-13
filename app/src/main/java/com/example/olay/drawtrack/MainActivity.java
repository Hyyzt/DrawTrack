package com.example.olay.drawtrack;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private MapView mMapView;
    private String chinaMapURL = "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer";
    private Button btn, btn1;
    private MoveHelper moveHelper;
    private List<Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.MapView);
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer(chinaMapURL);
        mMapView.addLayer(layer);
        getList();
        //缩放至中心点
        Envelope envelope = new Envelope(list.get(0).point, 1000, 1000);
        mMapView.setExtent(envelope);
        //初始化MoveHelper时的参数 Context,MapView,List<Bean>
        moveHelper = new MoveHelper(this, mMapView, list)
                .SpatialReference(SpatialReference.create(3857))
                .IconAndColor(R.drawable.point, Color.YELLOW, Color.GRAY)
                .Precision(0.0001)
                .Fellow(false)
                .Time(15)
                .OnDraw(new MoveHelper.onDraw() {
                    @Override
                    public void onFinish() {

                    }
                });
        //开始动画，在开始动画前，必须进行必要参数的设置
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveHelper.start();
            }
        });
        //暂停动画
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveHelper.pause();
            }
        });
    }

    public void getList() {
        list = new ArrayList<>();
        list.add(new Data("2017-08-17 13:56:00", new Point(116.37489, 40.06644)));
        list.add(new Data("2017-08-17 14:06:00", new Point(116.371086, 40.066822)));
        list.add(new Data("2017-08-17 14:16:00", new Point(116.371607, 40.07193)));
        list.add(new Data("2017-08-17 14:26:00", new Point(116.371607, 40.07193)));//116.363828,40.071433
        list.add(new Data("2017-08-17 14:36:00", new Point(116.371607, 40.07193)));//116.365085, 40.068313
        list.add(new Data("2017-08-17 14:46:00", new Point(116.363828, 40.071433)));
        list.add(new Data("2017-08-17 14:56:00", new Point(116.365085, 40.068313)));
        list.add(new Data("2017-08-17 15:06:00", new Point(116.354072, 40.066132)));
        list.add(new Data("2017-08-17 16:16:00", new Point(116.354647, 40.061618)));
        list.add(new Data("2017-08-17 16:26:00", new Point(116.354647, 40.061618)));
        list.add(new Data("2017-08-17 16:36:00", new Point(116.354647, 40.061618)));
        list.add(new Data("2017-08-17 16:46:00", new Point(116.360522, 40.046182)));
        list.add(new Data("2017-08-17 16:56:00", new Point(116.35127, 40.04563)));
        list.add(new Data("2017-08-17 17:16:00", new Point(116.346221, 40.043821)));
        list.add(new Data("2017-08-17 17:26:00", new Point(116.337525, 40.041625)));
        list.add(new Data("2017-08-17 17:36:00", new Point(116.33201, 40.041363)));
        list.add(new Data("2017-08-17 17:46:00", new Point(116.325255, 40.043489)));
        list.add(new Data("2017-08-17 17:56:00", new Point(116.325255, 40.043489)));
        list.add(new Data("2017-08-17 18:16:00", new Point(116.325255, 40.043489)));
//        list.add(new Data("2017-08-17 18:26:00", new Point(116.325255, 40.043489)));
//        list.add(new Data("2017-08-17 18:36:00", new Point(116.290544, 40.040134)));
//        list.add(new Data("2017-08-17 18:46:00", new Point(116.286951, 40.039968)));
//        list.add(new Data("2017-08-17 18:56:00", new Point(116.279028, 40.038697)));
//        list.add(new Data("2017-08-17 19:16:00", new Point(116.271626, 40.03722)));
//        list.add(new Data("2017-08-17 19:26:00", new Point(116.272237, 40.030659)));
//        list.add(new Data("2017-08-17 19:36:00", new Point(116.27344, 40.024706)));
//        list.add(new Data("2017-08-17 19:46:00", new Point(116.273584, 40.017882)));
//        list.add(new Data("2017-08-17 19:56:00", new Point(116.276477, 40.013724)));
//        list.add(new Data("2017-08-17 20:06:00", new Point(116.276477, 40.013724)));
//        list.add(new Data("2017-08-17 20:16:00", new Point(116.272057, 40.004509)));
//        list.add(new Data("2017-08-17 20:26:00", new Point(116.269721, 40.002091)));
    }
}
