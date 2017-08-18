package com.example.olay.drawtrack;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹绘制动画类
 * Created by OlAy on 2017/8/15.
 */

public class MoveHelper {

    private MapView mMapView;
    private List<Data> list;
    private List<Point> pointList = new ArrayList<>();
    private GraphicsLayer pointLayer;
    private SimpleMarkerSymbol symbol;
    private Drawable drawables;
    private PictureMarkerSymbol car;
    private GraphicsLayer carLayer;
    private Graphic carGraphic;
    private GraphicsLayer drawLayer;
    //
    private Point point;
    private GraphicsLayer resultLayer;
    private Context mainActivity;
    private MyCallOut myCallOut;

    public MoveHelper(Context mActivity, MapView mapView, List<Data> list) {
        this.mainActivity = mActivity;
        this.mMapView = mapView;
        this.list = list;
        for (Data data : list) {
            this.pointList.add(data.point);
        }
        mMapView.centerAt(wgs2(pointList.get(0)), true);
        myCallOut = new MyCallOut(mMapView, mainActivity);
        init();
        initPoint();
    }

    private int color1, color2;

    //设置icon和颜色
    public void setIconAndColor(int iconId, int color1, int color2) {
        //传入context,进行屏幕适配，否则图标会失真
        this.car = new PictureMarkerSymbol(mainActivity, mainActivity.getDrawable(iconId));
        this.color1 = color1;
        this.color2 = color2;
        drawCar();
    }

    //初始化
    private void init() {
        pointLayer = new GraphicsLayer();
        mMapView.addLayer(pointLayer);
        symbol = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        carLayer = new GraphicsLayer();
        mMapView.addLayer(carLayer);
        color1 = Color.YELLOW;
        color2 = Color.GRAY;
        drawLayer = new GraphicsLayer();
        mMapView.addLayer(drawLayer);
        resultLayer = new GraphicsLayer();
        mMapView.addLayer(resultLayer);
        point = pointList.get(0);
        ClickList.add(pointList.get(index));
    }

    private List<Point> ClickList = new ArrayList<>();

    public void setOnClickListener() {
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                Point clickPoint = mMapView.toMapPoint(v, v1);
                Point wgsPoint = (Point) GeometryEngine.project(clickPoint, spatialReference, SpatialReference.create(4326));
                for (int i = 0; i < ClickList.size(); i++) {
                    Point p = ClickList.get(i);
                    if (Math.abs(p.getX() - wgsPoint.getX()) < 0.001 && Math.abs(p.getY() - wgsPoint.getY()) < 0.001) {
                        // String timeInterval = getTimeInterval(i);
                        myCallOut.show(wgs2(ClickList.get(i)), list.get(i).data);
                        break;
                    }
                }
            }
        });
    }
//计算时间间隔
//    private String getTimeInterval(int index) {
//        String startTime = list.get(index).data;
//        String endTime;
//        //开始遍历
//        Stack<Data> stack = new Stack<>();
//        stack.add(list.get(index));
//        for (int i = index; i < list.size() - 1; i++) {
//            if (Math.abs(pointList.get(i).getX() - pointList.get(i + 1).getX()) < 0.0001
//                    && Math.abs(pointList.get(i).getY() - pointList.get(i + 1).getY()) < 0.0001) {
//                //前后两个点相同
//                stack.add(list.get(i + 1));
//            } else {
//                //前后2个点不同
//                break;
//            }
//        }
//        if (stack.size() > 1) {
//            endTime = stack.pop().data;
//            stack.removeAllElements();
//            return startTime + "---" + endTime;
//        } else {
//            return startTime;
//        }
//    }

    //画跟随物
    private void drawCar() {
        carLayer.removeAll();
        carGraphic = new Graphic(wgs2(pointList.get(index)), car);
        carLayer.addGraphic(carGraphic);
    }

    //初始化点
    private void initPoint() {
        drawPoint(index);
    }

    //画点
    public void drawPoint(int index) {
        Point wgs2 = wgs2(pointList.get(index));
        Graphic graphic = new Graphic(wgs2, symbol);
        pointLayer.addGraphic(graphic);
    }

    //变化速度
    private double speedX = 1, speedY = 1;
    //第几段的第几次动画
    private double time = 1;
    private boolean isFellow = false;

    //提供给外部的借口，在整个动画完成后调用
    public interface onDraw {
        void onFinish();
    }

    private onDraw onDraw;

    public void setOnDraw(onDraw onDraw) {
        this.onDraw = onDraw;
    }

    //是否跟随模式，默认为否
    public void setFellow(boolean fellow) {
        isFellow = fellow;
    }

    private int Time = 0;
    //每一段动画完成的时间
    private double a = 0;

    //整个动画完成的时间
    public void setTime(int time) {
        this.Time = time;
        a = (Time / pointList.size()) * 10;
    }

    //画动画的线，在每一段完成后，移除
    public void drawLines(int index) {
        carLayer.removeAll();
        speedX = (pointList.get(index + 1).getX() - pointList.get(index).getX()) / a;
        speedY = (pointList.get(index + 1).getY() - pointList.get(index).getY()) / a;
        Log.e("TAG", speedX + "," + speedY);
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(color1, 5, SimpleLineSymbol.STYLE.SOLID);
        point = new Point(pointList.get(index).getX(), pointList.get(index).getY());
        Log.e("TAG", "drawLines: " + point.toString());
        point.setX(point.getX() + speedX * time);
        point.setY(point.getY() + speedY * time);
        if (isFellow) {
            mMapView.centerAt(wgs2(point), true);
        }
        carGraphic = new Graphic(wgs2(point), car);
        Polyline polyline = new Polyline();
        polyline.startPath(wgs2(pointList.get(index)));
        Log.e("TAG1", pointList.get(index).toString());
        polyline.lineTo(wgs2(point));
        Log.e("Po", "drawLines: " + point.toString());
        Graphic graphic = new Graphic(polyline, lineSymbol);
        drawLayer.addGraphic(graphic);
        carLayer.addGraphic(carGraphic);
    }

    private boolean isPause = false;

    //开始动画
    public void start() {
        isPause = false;
        draw();
    }

    //暂停
    public void pause() {
        isPause = true;
    }

    //第几段动画
    private int index = 0;
    private double precision = 0.0001;

    //默认0.001
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    //开始绘制
    private void draw() {
        if (!isPause) {
            if (index >= pointList.size() - 1)
                return;
            if (Math.abs(pointList.get(index).getX() - pointList.get(index + 1).getX()) < precision
                    && Math.abs(pointList.get(index).getY() - pointList.get(index + 1).getY()) < precision) {
                //前一个点和后一个点没有变化，原地跳动
                index++;
                myCallOut.show(wgs2(pointList.get(index)), list.get(index).data);
                time = 0;
                ClickList.add(pointList.get(index));
                drawLines(index);
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                if (Math.abs(point.getX() - pointList.get(index + 1).getX()) < 0.000001
                        && Math.abs(point.getY() - pointList.get(index + 1).getY()) < 0.000001) {
                    //上一段动画完成
                    showResult(pointList.get(index), pointList.get(index + 1));
                    index++;
                    myCallOut.show(wgs2(pointList.get(index)), list.get(index).data);
                    time = 0;
                    ClickList.add(pointList.get(index));
                    setOnClickListener();
                    drawPoint(index);
                    if (index == pointList.size() - 1) {
                        //路程结束
                        Toast.makeText(mainActivity, "路程结束", Toast.LENGTH_SHORT).show();
                        onDraw.onFinish();
                        return;
                    }
                }
                drawLines(index);
                handler.sendEmptyMessageDelayed(0, 100);
            }
        }
    }

    //在每一段动画完成后，消除之前的线，绘制已经完成的线
    private void showResult(Point start, Point end) {
        drawLayer.removeAll();
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(color2, 5, SimpleLineSymbol.STYLE.SOLID);
        Polyline polyline = new Polyline();
        polyline.startPath(wgs2(start));
        polyline.lineTo(wgs2(end));
        Graphic graphic = new Graphic(polyline, lineSymbol);
        resultLayer.addGraphic(graphic);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            time++;
            draw();
        }
    };

    //坐标转换
    public Point wgs2(Point point) {
        Point po = (Point) GeometryEngine.project(point, SpatialReference.create(4326), spatialReference);
        return po;
    }

    private SpatialReference spatialReference = SpatialReference.create(3857);

    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }
}
