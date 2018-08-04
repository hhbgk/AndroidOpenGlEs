package com.hhbgk.example.opengles;

public class CodecWrapper {
    private String tag = getClass().getSimpleName();
    static {
        System.loadLibrary("glrender");
    }
    public static native void init();

    public static native int decode(byte[] array, int width, int height);

    public static native void changed(int width, int height);

    public static native void render();

    public static native void destroy();
}
