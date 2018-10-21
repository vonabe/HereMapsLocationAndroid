package dev.vonabe.here.location;

import android.graphics.PointF;

import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.MapGesture;

import java.util.List;

public class MyOnGestureListener implements MapGesture.OnGestureListener {

    private float x, y;

    @Override
    public void onPanStart() {
        System.out.println(getClass().getName()+": PanStart");
    }

    @Override
    public void onPanEnd() {
        System.out.println(getClass().getName()+": PanEnd");
    }

    @Override
    public void onMultiFingerManipulationStart() {
        System.out.println(getClass().getName()+": OnMultiFingerManipulationStart");
    }

    @Override
    public void onMultiFingerManipulationEnd() {
        System.out.println(getClass().getName()+": onMultiFingerManipulationEnd");
    }

    @Override
    public boolean onMapObjectsSelected(List<ViewObject> list) {
        System.out.println(getClass().getName()+": onMapObjectSelected");
        return false;
    }

    @Override
    public boolean onTapEvent(PointF pointF) {
        x = pointF.x;
        y = pointF.y;
        System.out.println(getClass().getName()+": TabEvent: "+x +", "+y);
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(PointF pointF) {
        x = pointF.x;
        y = pointF.y;
        System.out.println(getClass().getName()+": onDoubleTapEvent ");
        return false;
    }

    @Override
    public void onPinchLocked() {
        System.out.println(getClass().getName()+": onPinchLocked ");
    }

    @Override
    public boolean onPinchZoomEvent(float v, PointF pointF) {
        System.out.println(getClass().getName()+": onPinchZoomEvent");
        return false;
    }

    @Override
    public void onRotateLocked() {
        System.out.println(getClass().getName()+": onRotateLocked");
    }

    @Override
    public boolean onRotateEvent(float v) {
        System.out.println(getClass().getName()+": onRotateEvent");
        return false;
    }

    @Override
    public boolean onTiltEvent(float v) {
        System.out.println(getClass().getName()+": onTiltEvent");
        return false;
    }

    @Override
    public boolean onLongPressEvent(PointF pointF) {
        System.out.println(getClass().getName()+": onLongPressEvent");
        return false;
    }

    @Override
    public void onLongPressRelease() {
        System.out.println(getClass().getName()+": OnLongPressRelease");
    }

    @Override
    public boolean onTwoFingerTapEvent(PointF pointF) {
        System.out.println(getClass().getName()+": onTwoFingerTapEvent");
        return false;
    }
}
