package com.glace.client;

public enum SpriteParts {

    HEAD(2, 5, 23, 16),
    TORSO(6, 21, 11, 4),
    LEFT_ARM(3, 21, 3, 5),
    RIGHT_ARM(17, 21, 7, 5),
    LEFT_LEG(6, 25, 6, 3),
    RIGHT_LEG(12, 25, 7, 3);

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    SpriteParts(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

}
