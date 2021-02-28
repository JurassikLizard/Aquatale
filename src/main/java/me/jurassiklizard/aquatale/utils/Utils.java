package me.jurassiklizard.aquatale.utils;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import me.jurassiklizard.aquatale.Aquatale;
import me.jurassiklizard.aquatale.components.FishAnimationComponent;
import me.jurassiklizard.aquatale.components.PlayerAnimationComponent;
import me.jurassiklizard.aquatale.enums.MoveDirection;

import java.util.Random;

public class Utils {
    private static int moveDistance = 5;
    private static Aquatale main = Aquatale.getInstance();
    private static double fishCounter = 0.0;

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
     * Gets the location at the center of the given rectangle
     * @param x The location of the object on the x coordinate-plane
     * @param y The location of the object on the y coordinate-plane
     */
    public static Vec2 getRectangleCornerPosition(double x, double y, double x1, double x2){
        x -= x1 / 2.0;
        y -= x2 / 2.0;
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
        if(direction == MoveDirection.LEFT && !isOutsideScreenLeft(entity)){
            main.getPlayer().getComponent(PlayerAnimationComponent.class).moveLeft();
        }
        if(direction == MoveDirection.UP && !isOutsideScreenUp(entity)) {
            main.getPlayer().getComponent(PlayerAnimationComponent.class).moveUp();
        }
        if(direction == MoveDirection.RIGHT && !isOutsideScreenRight(entity)){
            main.getPlayer().getComponent(PlayerAnimationComponent.class).moveRight();
        }
        if(direction == MoveDirection.DOWN && !isOutsideScreenDown(entity)){
            main.getPlayer().getComponent(PlayerAnimationComponent.class).moveDown();
        }
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

    public static void spawnFish(double tpf){
        if(fishCounter > 5.0){
            int random = new Random().nextInt(3);
            if(random != 1){
                fishCounter += 2 * tpf;
                return;
            }

            int ySpawn = FXGL.random(0, FXGL.getAppHeight());
            int xSpawnOffset = 10;
            boolean swimsRight = new Random().nextBoolean();
            int xSpawn = 0;
            if(swimsRight) xSpawn = -1 * xSpawnOffset;
            if(!swimsRight) xSpawn = FXGL.getAppWidth() + xSpawnOffset;
            Vec2 vec2Corner = Utils.getRectangleCornerPosition(xSpawn, ySpawn, 54, 49);

            Entity fish = FXGL.entityBuilder()
                    .at(new Vec2(xSpawn, ySpawn))
                    .with(new CollidableComponent(true))
                    .with(new FishAnimationComponent(swimsRight))
                    .viewWithBBox(new Rectangle(54, 49, Color.TRANSPARENT))
                    .buildAndAttach();

            main.fish.add(fish);
            fishCounter = 0.0;
            return;
        }

        fishCounter += 2 * tpf;
    }

    public static void updateFlashLights(){
        main.light.removeFromWorld();
        main.entityViews.remove(main.light);

        Input input = FXGL.getInput();
        Point2D mousePos = input.getMousePositionWorld();
        Vec2 vec2PlayerPos = Utils.getRectangleCenterPosition(main.player.getX(), main.player.getY(), main.entityViews.get(main.player));
        Point2D playerPos = new Point2D(vec2PlayerPos.x, vec2PlayerPos.y);

        Rectangle r = new Rectangle(FXGL.getAppWidth() * main.multiplier, FXGL.getAppHeight() * main.multiplier);
        Circle mouseLight = new Circle(mousePos.getX(), mousePos.getY(),100, Color.BLACK);
        Circle playerLight = new Circle(playerPos.getX(), playerPos.getY(),100, Color.BLACK);
        Shape flashlight = Shape.subtract(r, Shape.union(mouseLight, playerLight));

        flashlight.setStroke(Color.YELLOW);
//        Glow glow = new Glow();
//        glow.setLevel(50);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(5);
        flashlight.setEffect(blur);

        main.light = FXGL.entityBuilder()
                .at(new Vec2(0, 0))
                .view(flashlight)
                .buildAndAttach();
    }
}
