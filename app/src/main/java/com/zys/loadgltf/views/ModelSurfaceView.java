package com.zys.loadgltf.views;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.zys.loadgltf.model.GltfModelData;
import com.zys.loadgltf.utils.LoaderTask;
import com.zys.loadgltf.utils.LoadingCallback;


import de.javagl.jgltf.model.GltfModel;


public class ModelSurfaceView extends GLSurfaceView {
    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_ROTATE = 1;
    private static final int TOUCH_ZOOM = 2;

    @NonNull
    private ModelRenderer renderer;

    private float previousX;
    private float previousY;

    private PointF pinchStartPoint = new PointF();
    private float pinchStartDistance = 0.0f;
    private int touchMode = TOUCH_NONE;
    Context context;

    boolean isLoadMoreModel=true;


    public ModelSurfaceView(Context context) {
        super(context);
        this.context = context;
        setEGLContextClientVersion(2);

        renderer = new ModelRenderer();
        setRenderer(renderer);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public boolean isLoadMoreModel() {
        return isLoadMoreModel;
    }

    public void setLoadMoreModel(boolean loadMoreModel) {
        isLoadMoreModel = loadMoreModel;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                previousX = event.getX();
                previousY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {

                    if (touchMode != TOUCH_ROTATE) {
                        previousX = event.getX();
                        previousY = event.getY();
                    }
                    touchMode = TOUCH_ROTATE;
                    float x = event.getX();
                    float y = event.getY();
                    float dx = x - previousX;
                    float dy = y - previousY;
                    previousX = x;
                    previousY = y;
                    if (Math.abs(dx) != 0 || Math.abs(dy) != 0) {
                        renderer.rotate(pxToDp(dy), pxToDp(dx));
                    }
                } else if (event.getPointerCount() == 2) {

                    if (touchMode != TOUCH_ZOOM) {
                        pinchStartDistance = getPinchDistance(event);
                        getPinchCenterPoint(event, pinchStartPoint);
                        previousX = pinchStartPoint.x;
                        previousY = pinchStartPoint.y;
                        touchMode = TOUCH_ZOOM;
                    } else {
                        PointF pt = new PointF();
                        getPinchCenterPoint(event, pt);
                        float dx = pt.x - previousX;
                        float dy = pt.y - previousY;
                        previousX = pt.x;
                        previousY = pt.y;
                        float pinchScale = getPinchDistance(event) / pinchStartDistance;
                        pinchStartDistance = getPinchDistance(event);
//                        Log.e("-----", "-----dx=" + dx + ",dy=" + dy + ",pinchScale=" + pinchScale);
                        if (pinchScale != 1 || dx != 0 || dy != 0) {
                            renderer.translate(pxToDp(dx), pxToDp(dy), pinchScale);
                        }

                    }

                }
                requestRender();
                break;

            case MotionEvent.ACTION_UP:
                pinchStartPoint.x = 0.0f;
                pinchStartPoint.y = 0.0f;
                touchMode = TOUCH_NONE;
                break;
        }
        return true;
    }

    private float getPinchDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void getPinchCenterPoint(MotionEvent event, PointF pt) {
        pt.x = (event.getX(0) + event.getX(1)) * 0.5f;
        pt.y = (event.getY(0) + event.getY(1)) * 0.5f;
    }

    public float pxToDp(float px) {
        return px / getDensityScalar();
    }

    private float getDensityScalar() {
        return context.getResources().getDisplayMetrics().density;
    }

    public void loadModel(GltfModel model) {

        queueEvent(() -> {
            if (isLoadMoreModel){
                renderer.addMoreGLTFModel(model);
            }else {
                renderer.addSingleGLTFModel(model);
            }
        });

    }

    public void loadGLTFData(GltfModelData modelData) {
        if (modelData==null){
            Toast.makeText(context,"GltfModelData不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        LoaderTask mTask = new LoaderTask(this, modelData,null);
        mTask.execute();
    }

    public void loadGLTFData(GltfModelData modelData,LoadingCallback callback) {
        if (modelData==null){
            Toast.makeText(context,"GltfModelData不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        LoaderTask mTask = new LoaderTask(this, modelData, callback);
        mTask.execute();
    }
}
