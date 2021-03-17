package com.daasuu.epf.filter;

import android.opengl.GLES20;

/**
 * Created by Pervez Alam on 17 Mar 2021.
 */

public class GlEdgeFeatherFilter extends GlFilter {

    private static final String EDGE_FEATHER_FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform lowp sampler2D sTexture;\n" +
            "uniform lowp float featherHeight;\n" +
            "uniform lowp float featherWidth;\n" +
            "uniform lowp float imHeight;\n" +
            "uniform lowp float imWidth;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
            "    float left = featherWidth - vTextureCoord.x;\n" +
            "    float top = featherHeight - vTextureCoord.y;\n" +
            "    float bottom = featherHeight - (1.0 - vTextureCoord.y);\n" +
            "    float right = featherWidth - (1.0 - vTextureCoord.x);\n" +
            "    float lr = max(left, right);\n" +
            "    float tb = max(top, bottom);\n" +
            "    float dist = max(lr, tb);\n" +
            "    float featherSize = lr > tb ? featherWidth : featherHeight;\n" +
            "    float edgeFeather = (1.0 - dist / featherSize);\n" +
            "    if (dist > 0.0) {\n" +
            "        gl_FragColor.r *= edgeFeather;\n" +
            "        gl_FragColor.g *= edgeFeather;\n" +
            "        gl_FragColor.b *= edgeFeather;\n" +
            "    }\n" +
            "}";

    // feather height/width is normalized (0.0-1.0) on the basis of the feather size and the image's height / width
    private int featherSize = 0;
    private float featherWidth;
    private float featherHeight;
    private float imWidth = 0;
    private float imHeight = 0;

    private void updateFeatherSize() {
        this.featherHeight = (float) featherSize / (float) imHeight;
        this.featherWidth = (float) featherSize / (float) imWidth;
    }

    public GlEdgeFeatherFilter() {
        super(DEFAULT_VERTEX_SHADER, EDGE_FEATHER_FRAGMENT_SHADER);
    }

    public float getFeatherSize() {
        return featherSize;
    }

    /**
     * Edge feather amount (in pixel) to be cropped
     * @param featherSize value in pixel
     */
    public void setFeatherSize(int featherSize) {
        this.featherSize = featherSize;
        updateFeatherSize();
    }

    @Override
    public void setFrameSize(int width, int height) {
        imWidth = (float) width;
        imHeight = (float) height;
        updateFeatherSize();
    }

    @Override
    public void onDraw() {
        GLES20.glUniform1f(getHandle("featherHeight"), featherHeight);
        GLES20.glUniform1f(getHandle("featherWidth"), featherWidth);
    }

}
