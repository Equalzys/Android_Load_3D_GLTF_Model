package com.zys.loadgltf.utils;

import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.util.Log;


import androidx.annotation.NonNull;

import com.zys.loadgltf.model.GltfModelData;
import com.zys.loadgltf.views.ModelSurfaceView;

import java.io.IOException;
import java.net.URI;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.io.GltfModelReader;

public class LoaderTask extends AsyncTask<String, String, GltfModel> {

    GLSurfaceView mSurfaceView;
    GltfModelData modelData;
    LoadingCallback callback;

    public LoaderTask(GLSurfaceView mySurfaceView, GltfModelData modelData, LoadingCallback callback) {
        this.mSurfaceView = mySurfaceView;
        this.modelData = modelData;
        this.callback = callback;
    }


    @Override
    protected GltfModel doInBackground(String... strings) {
        Log.e("----", "1-----doInBackground");
        if (callback != null) {
            callback.onStart();
        }

        GltfModelReader r = new GltfModelReader();
        GltfModel gltfModel = null;
        try {
            gltfModel = r.read(modelData.getUri());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return gltfModel;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (callback != null) {
            callback.onProgress();
        }
    }

    // 方法4：onPostExecute（）
    // 作用：接收线程任务执行结果、将执行结果显示到UI组件
    // 注：必须复写，从而自定义UI操作
    @Override
    protected void onPostExecute(GltfModel result) {
//        Log.e("----", "-----onPostExecute");

        if (result != null) {
            Log.e("----", "2-----onPostExecute");
            NodeModel nodeModel = result.getSceneModels().get(0).getNodeModels().get(0);
            if (nodeModel != null) {
                nodeModel.setScale(new float[]{modelData.getScale(), modelData.getScale(), modelData.getScale()});
                nodeModel.setTranslation(modelData.getPosition());
                //rotations[0]:绕X轴旋转
                //rotations[1]:绕Y轴旋转
                //rotations[2]:绕Z轴旋转
                //rotations[3]:绕w轴旋转
                if (modelData.getRotations() != null) {
                    nodeModel.setRotation(modelData.getRotations());
                }
            }

            if (callback != null) {
                callback.onLoad(result);
            }
            if (mSurfaceView instanceof ModelSurfaceView) {
                ((ModelSurfaceView) mSurfaceView).loadModel(result);
            }


        } else {
            Log.e("----", "3-----onPostExecute");
        }

        if (callback != null) {
            callback.onEnd();
        }


    }


}
