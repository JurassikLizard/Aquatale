package utils;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.shape.Rectangle;

public class Utils {
    /**
     * Gets the location at the center of the given rectangle
     * @param x The location of the object on the x coordinate-plane
     * @param y The location of the object on the y coordinate-plane
     * @param recWidth The width of the rectangle
     * @param recHeight The height of the rectangle
     */
    public static Vec2 getRectangleCenterPosition(float x, float y, float recWidth, float recHeight){
        x -= recWidth / 2.0;
        y -= recHeight / 2.0;
        return new Vec2(x, y);
    }

    /**
     * Gets the location at the center of the given rectangle
     * @param x The location of the object on the x coordinate-plane
     * @param y The location of the object on the y coordinate-plane
     * @param rectangle The rectangle of whos location you want to get the center of
     */
    public static Vec2 getRectangleCenterPosition(float x, float y, Rectangle rectangle){
        x -= rectangle.getWidth() / 2.0;
        y -= rectangle.getHeight() / 2.0;
        return new Vec2(x, y);
    }

    /**
     * Gets the coordinates of the center of the screen
     */
    public static Vec2 getCenterScreen(){
        double x = FXGL.getGameScene().getAppWidth() / 2.0;
        double y = FXGL.getGameScene().getAppHeight() / 2.0;
        return new Vec2(x, y);
    }
}
