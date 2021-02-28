package me.jurassiklizard.aquatale.utils;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.shape.Rectangle;
import me.jurassiklizard.aquatale.Aquatale;
import me.jurassiklizard.aquatale.enums.MoveDirection;

public class Utils {
    private static int moveDistance = 5;
    private static Aquatale main = Aquatale.getInstance();

    /**
     * Gets the location at the center of the given rectangle
     * @param x The location of the object on the x coordinate-plane
     * @param y The location of the object on the y coordinate-plane
     * @param box The bounding box of the entity
     */
    public static Vec2 getRectangleCenterPosition(double x, double y, BoundingBox box){
        x += box.getWidth() / 2.0;
        y += box.getHeight() / 2.0;
        return new Vec2(x, y);
    }

    public static Vec2 getRectangleCenterPosition(double x, double y, double recWidth, double recHeight){
        x += recWidth / 2.0;
        y += recHeight / 2.0;
        return new Vec2(x, y);
    }

    /**
     * Gets the location at the center of the given rectangle
     * @param x The location of the object on the x coordinate-plane
     * @param y The location of the object on the y coordinate-plane
     * @param rectangle The rectangle of whos location you want to get the center of
     */
    public static Vec2 getRectangleCenterPosition(double x, double y, Rectangle rectangle){
        x += rectangle.getHeight() / 2.0;
        y += rectangle.getHeight() / 2.0;
        return new Vec2(x, y);
    }

    /**
     * Gets the location at the center of the given rectangle
     * @param x The location of the object on the x coordinate-plane
     * @param y The location of the object on the y coordinate-plane
     * @param rectangle The rectangle of whos location you want to get the corner of
     */
    public static Vec2 getRectangleCornerPosition(double x, double y, Rectangle rectangle){
        x -= rectangle.getHeight() / 2.0;
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

    public static void move(Entity entity, MoveDirection direction){
        if(direction == MoveDirection.LEFT && !isOutsideScreenLeft(entity)) entity.translateX(-5);
        if(direction == MoveDirection.UP && !isOutsideScreenUp(entity)) entity.translateY(-5);;
        if(direction == MoveDirection.RIGHT && !isOutsideScreenRight(entity)) entity.translateX(5);
        if(direction == MoveDirection.DOWN && !isOutsideScreenDown(entity)) entity.translateY(5);
    }

    public static boolean isInsideScreen(Entity entity){
        boolean left = entity.getX() < 0 || entity.getX() - moveDistance < 0;
        boolean up = entity.getY() < 0 || entity.getY() - moveDistance < 0;
        boolean right = entity.getX() > FXGL.getGameScene().getAppWidth() || entity.getX() + moveDistance > FXGL.getGameScene().getAppWidth();
        boolean down = entity.getY() > FXGL.getGameScene().getAppHeight() || entity.getY() + moveDistance > FXGL.getGameScene().getAppHeight();

        if(left || up || right || down) return false;
        return true;
    }

    public static boolean isOutsideScreenLeft(Entity entity){
        if(entity.getX() < 0 || entity.getX() - moveDistance < 0)
            return true;
        return false;
    }

    public static boolean isOutsideScreenUp(Entity entity){
        if(entity.getY() < 0 || entity.getY() - moveDistance < 0)
            return true;
        return false;
    }

    public static boolean isOutsideScreenRight(Entity entity){
        BoundingBox box = main.getEntityViews().get(entity);
        double width = box.getWidth();
        double x = entity.getX() + width;
        if(x > FXGL.getGameScene().getAppWidth() || x + moveDistance > FXGL.getGameScene().getAppWidth())
            return true;
        return false;
    }

    public static boolean isOutsideScreenDown(Entity entity){
        BoundingBox box = main.getEntityViews().get(entity);
        double height = box.getHeight();
        double y = entity.getY() + height;
        if(y > FXGL.getGameScene().getAppHeight() || y + moveDistance > FXGL.getGameScene().getAppHeight())
            return true;
        return false;
    }
}
