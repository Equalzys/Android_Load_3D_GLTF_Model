package com.zys.loadgltf.utils;

import de.javagl.jgltf.model.GltfModel;

public interface LoadingCallback {
    void onStart();

    void onProgress();

    void onLoad(GltfModel model);

    void onEnd();
}
