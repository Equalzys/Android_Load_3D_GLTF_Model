package com.zys.loadgltf.views;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import androidx.core.view.KeyEventDispatcher;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.viewer.ExternalCamera;
import de.javagl.jgltf.viewer.GltfViewer;
import de.javagl.jgltf.viewer.gles.GlViewerGles;

import static android.opengl.GLES10.GL_LIGHTING;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;

public class ModelRenderer implements GLSurfaceView.Renderer, ExternalCamera {
    private GltfViewer<KeyEventDispatcher.Component> gltfViewerOBJ; //main viewer object
    private static final float MODEL_BOUND_SIZE = 50f;
    private static final float Z_NEAR = 2f;
    private static final float Z_FAR = MODEL_BOUND_SIZE * 10;
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private float rotateAngleX;
    private float rotateAngleY;
    private float translateX;
    private float translateY;
    private float translateZ;
    private boolean modelReady = false;


    public void translate(float dx, float dy, float dz) {
        Log.e("------", "------translate-----");
        final float translateScaleFactor = MODEL_BOUND_SIZE / 200f;
        translateX += dx * translateScaleFactor;
        translateY += dy * translateScaleFactor;
        if (dz != 0f) {
            translateZ /= dz;
        }
        updateViewMatrix();


    }

    public void rotate(float aX, float aY) {
        Log.e("-----", "-----rotate");

        final float rotateScaleFactor = 0.5f;
        rotateAngleX -= aX * rotateScaleFactor;
        rotateAngleY += aY * rotateScaleFactor;

        updateViewMatrix();
    }

    private void updateViewMatrix() {
        Log.e("-----", "-----updateViewMatrix");

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, translateZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.translateM(viewMatrix, 0, -translateX, -translateY, 0f);
        Matrix.rotateM(viewMatrix, 0, rotateAngleX, 1f, 0f, 0f);
        Matrix.rotateM(viewMatrix, 0, rotateAngleY, 0f, 1f, 0f);
    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (modelReady) {
            gltfViewerOBJ.triggerRendering();
        }
    }


    private int width;
    private int height;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.e("-----", "-----onSurfaceCreated");

        glClearColor(0.2f, 0.2f, 0.2f, 1f);
        glEnable(GLES20.GL_CULL_FACE);
        glEnable(GLES20.GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GLES20.GL_BLEND);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.e("-----", "-----onSurfaceChanged");

        this.width = width;
        this.height = height;

        GLES20.glViewport(0, 0, width, height);


        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, Z_NEAR, Z_FAR);

        rotateAngleX = 0;
        rotateAngleY = 0;
        translateX = 0f;
        translateY = 0f;
        translateZ = -MODEL_BOUND_SIZE * 1.5f;
        updateViewMatrix();


    }


    public void addGLTFModels(List<GltfModel> gms) {

        try {
            if (gms != null) {

                if (gltfViewerOBJ == null) {
                    gltfViewerOBJ = new GlViewerGles(this.width, this.height);
                    gltfViewerOBJ.setExternalCamera(this);
                }
                for (int i = 0; i < gms.size(); i++) {
                    gltfViewerOBJ.addGltfModel(gms.get(i));
                    //gltfViewerOBJ.setAnimationsRunning(false);
                    modelReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.e("------", "------gms.size=" + gms.size());
        Log.e("------", "------addGLTFModels-----");

    }

    public void addMoreGLTFModel(GltfModel gm) {

        try {
            if (gm != null) {
                if (gltfViewerOBJ == null) {
                    gltfViewerOBJ = new GlViewerGles(this.width, this.height);
                    gltfViewerOBJ.setExternalCamera(this);
                }
                gltfViewerOBJ.addGltfModel(gm);
                //gltfViewerOBJ.setAnimationsRunning(false);

                modelReady = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("------", "------addGLTFModel-----");
    }

    public void addSingleGLTFModel(GltfModel gm) {

        try {
            if (gm != null) {
                gltfViewerOBJ = new GlViewerGles(this.width, this.height);
                gltfViewerOBJ.setExternalCamera(this);
                gltfViewerOBJ.addGltfModel(gm);
                //gltfViewerOBJ.setAnimationsRunning(false);
                modelReady = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("------", "------addSingleGLTFModel-----");
    }

    public void stopAnim() {
        if (gltfViewerOBJ != null) {
            gltfViewerOBJ.setAnimationsRunning(false);
        }
    }

    public void startAnim() {
        if (gltfViewerOBJ != null) {
            gltfViewerOBJ.setAnimationsRunning(true);
        }
    }


    @Override
    public float[] getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

}