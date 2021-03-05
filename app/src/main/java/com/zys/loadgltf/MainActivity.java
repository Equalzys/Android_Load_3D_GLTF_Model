package com.zys.loadgltf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.task.DownloadTask;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zys.loadgltf.model.GltfModelData;
import com.zys.loadgltf.utils.DownLoadFileUtil;
import com.zys.loadgltf.utils.FileUtil;
import com.zys.loadgltf.views.ModelSurfaceView;

import java.io.File;
import java.net.URI;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ModelSurfaceView mSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 已知问题：触摸滑动时，绘制闪烁，猜测是矩阵变换问题，因对矩阵不了解，无处下手
         * 触摸事件在ModelSurfaceView->>onTouchEvent
         * 矩阵变换在ModelRenderer->>updateViewMatrix
         *
         */

        setContentView(mSurfaceView = new ModelSurfaceView(this));

        //第一次打开时请求权限和下载model
//        permissions();


        //下载后的文件地址
        String p = "/storage/emulated/0/Android/data/com.zys.loadgltf/files/Download/fjxz.gltf";
        String p1 = "/storage/emulated/0/Android/data/com.zys.loadgltf/files/Download/AnimatedMorphCube.gltf";

        File f = new File(p);
        File f1 = new File(p1);
        URI uri = FileUtil.file2URI(f);
        URI uri1 = FileUtil.file2URI(f1);

        GltfModelData modelData = new GltfModelData(uri,
                new float[]{30f, 30f, 30f},
                2f);
        GltfModelData modelData0_1 = new GltfModelData(uri,
                new float[]{-10f, -5f, -10f},
                2f,
                new float[]{-1f, 0f, 0f, 0f});

        GltfModelData modelData0_2 = new GltfModelData(uri,
                new float[]{5f, 5f, 10f},
                1f,
                new float[]{1f, 0f, 0f, 0f});

        GltfModelData modelData0_3 = new GltfModelData(uri,
                new float[]{-20f, -20f, -20f},
                1.5f,
                new float[]{0f, 1f, 0f, 0f});

        GltfModelData modelData0_4 = new GltfModelData(uri,
                new float[]{20f, 20f, 20f},
                1.5f,
                new float[]{0f, -1f, 0f, 0f});


        GltfModelData modelData0_5 = new GltfModelData(uri,
                new float[]{-10f, -10f, -10f},
                1.5f,
                new float[]{0f, 0f, -1f, 0f});

        GltfModelData modelData0_6 = new GltfModelData(uri,
                new float[]{10f, 10f, 10f},
                2f,
                new float[]{0f, 0f, 1f, 0f});

        GltfModelData modelData0_7 = new GltfModelData(uri,
                new float[]{-10f, -10f, -10f},
                1.5f,
                new float[]{0f, 0f, 0f, -20f});

        GltfModelData modelData0_8 = new GltfModelData(uri,
                new float[]{10f, 10f, 10f},
                2f,
                new float[]{0f, 0f, 0f, 20f});

        GltfModelData modelData0_9 = new GltfModelData(uri,
                new float[]{10f, 10f, 10f},
                2f,
                new float[]{1f, 1f, 1f, 0f});

        GltfModelData modelData0_10 = new GltfModelData(uri,
                new float[]{-10f, -10f, -10f},
                2f,
                new float[]{1f, 1f, 1f, 1f});
        GltfModelData modelData0_11 = new GltfModelData(uri,
                new float[]{0f, 10f, 10f},
                2f,
                new float[]{10f, 1f, 1f, 1f});

        GltfModelData modelData1 = new GltfModelData(uri1,
                new float[]{0, 0, 0},
                50f);

        mSurfaceView.loadGLTFData(modelData);
//        mSurfaceView.loadGLTFData(modelData0_1);
//        mSurfaceView.loadGLTFData(modelData0_2);
        mSurfaceView.loadGLTFData(modelData0_3);
//        mSurfaceView.loadGLTFData(modelData0_4);
//        mSurfaceView.loadGLTFData(modelData0_5);
//        mSurfaceView.loadGLTFData(modelData0_6);
//        mSurfaceView.loadGLTFData(modelData0_7);
//        mSurfaceView.loadGLTFData(modelData0_8);
        mSurfaceView.loadGLTFData(modelData0_9);
        mSurfaceView.loadGLTFData(modelData0_10);
        mSurfaceView.loadGLTFData(modelData0_11);
        mSurfaceView.loadGLTFData(modelData1);

    }


    private void permissions(){
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                        String message = "需要您同意以下权限才能正常使用";
                        scope.showRequestReasonDialog(deniedList, message, "确定", "取消");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            downLoadGltf();
                        } else {
                            Toast.makeText(MainActivity.this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    /**
     * model 文件太大，每次用网络流加载不友好
     * bin文件为gltf的详细参数文件，加载时自动读取gltf配置里的bin文件，和gltf存放在同一文件夹下
     * 第一次下载手机后，之后用到直接读取
     */
    private void downLoadGltf() {
        DownLoadFileUtil.downLoad(this, "http://www.aizys.com/files/fjxz.gltf", "fjxz.gltf");
        DownLoadFileUtil.downLoad(this, "http://www.aizys.com/files/fjxz.bin", "fjxz.bin");
        DownLoadFileUtil.downLoad(this, "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/AnimatedMorphCube/glTF/AnimatedMorphCube.gltf", "AnimatedMorphCube.gltf");
        DownLoadFileUtil.downLoad(this, "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/AnimatedMorphCube/glTF/AnimatedMorphCube.bin", "AnimatedMorphCube.bin");

    }


    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
//        if(task.getKey().eques(url)){
////		....
////            可以通过url判断是否是指定任务的回调
//        }
        int p = task.getPercent();    //任务进度百分比
        Log.e("----", "------progress=" + p);
//        String speed = task.getConvertSpeed();	//转换单位后的下载速度，单位转换需要在配置文件中打开
//        long speed1 = task.getSpeed(); //原始byte长度速度
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.e("-----", "------taskComplete--url=" + task.getKey());
        Log.e("-----", "------taskComplete--path=" + task.getFilePath());
    }

}