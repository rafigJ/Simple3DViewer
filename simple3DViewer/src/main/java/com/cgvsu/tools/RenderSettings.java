package com.cgvsu.tools;

public class RenderSettings {
    public boolean texturingImageOnModel;
    public boolean turningOnTheLighting;
    public boolean showMashOnModel;
    public boolean fillWithStaticColor;

    public RenderSettings(boolean texture, boolean shadow, boolean mesh, boolean fill) {
        this.texturingImageOnModel = texture;
        this.turningOnTheLighting = shadow;
        this.showMashOnModel = mesh;
        this.fillWithStaticColor = fill;
    }
}
