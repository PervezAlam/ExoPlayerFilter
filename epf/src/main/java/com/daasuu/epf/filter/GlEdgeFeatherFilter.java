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
            "uniform lowp float featherSize;\n" +
            "uniform lowp float imHeight;\n" +
            "uniform lowp float imWidth;\n" +
            "void main()\n" +
            "{\n" +
            "    float x = float(imWidth) * vTextureCoord.x;\n" +
            "    float y = float(imHeight) * vTextureCoord.y;\n" +
            "    float left = featherSize - x;\n" +
            "    float top = featherSize - y;\n" +
            "    float bottom = featherSize - (imHeight - y);\n" +
            "    float right = featherSize - (imWidth - x);\n" +
            "    float dist = max(max(left, right), max(top, bottom));\n" +
            "    float edgeFeather = (1.0 - dist / featherSize);\n" +
            "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
            "    if (dist > 0.0) {\n" +
            "        gl_FragColor.r *= edgeFeather;\n" +
            "        gl_FragColor.g *= edgeFeather;\n" +
            "        gl_FragColor.b *= edgeFeather;\n" +
            "    }\n" +
            "}";

    // feather height/width is normalized (0.0-1.0) on the basis of the feather size and the image's height / width
    private int featherSize = 0;
    private float imWidth = 0;
    private float imHeight = 0;

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
        //this.featherSize = featherSize;
    }

    @Override
    public void setFrameSize(int width, int height) {
        imWidth = (float) width;
        imHeight = (float) height;
    }

    @Override
    public void onDraw() {
        GLES20.glUniform1f(getHandle("featherSize"), (float)featherSize);
        GLES20.glUniform1f(getHandle("imWidth"), (float)imWidth);
        GLES20.glUniform1f(getHandle("imHeight"), (float)imHeight);
    }

}
