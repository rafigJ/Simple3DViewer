package com.cgvsu.tools;

public class TextureSettings {
    public boolean texture;
    public boolean shadow;
    public boolean mesh;
    public boolean fill;

    public TextureSettings(boolean texture, boolean shadow, boolean mesh, boolean fill) {
        this.texture = texture;
        this.shadow = shadow;
        this.mesh = mesh;
        this.fill = fill;
    }
}
