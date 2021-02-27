package me.jurassiklizard.aquatale.utils;

public class BoundingBox {
    private double height;
    private double width;

    public BoundingBox(double width, double height){
        this.height = height;
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
