package com.zys.loadgltf.model;

import java.net.URI;

public class GltfModelData {
    /**
     * file uri address
     */
    private URI uri;
    /**
     * 位置信息，x,y,z，长度3
     */
    private float[] position;
    /**
     * 缩放大小
     */
    private float scale;
    /**
     * 旋转参数，长度4
     * 还没弄明白 (：<)
     */
    private float[] rotations;


    public GltfModelData(URI uri, float[] position, float scale) {
        this.uri = uri;
        this.position = position;
        this.scale = scale;
    }

    public GltfModelData(URI uri, float[] position, float scale, float[] rotations) {
        this.uri = uri;
        this.position = position;
        this.scale = scale;
        this.rotations = rotations;
    }


    public URI getUri() {
        return uri;
    }


    public float[] getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public float[] getRotations() {
        return rotations;
    }
}
