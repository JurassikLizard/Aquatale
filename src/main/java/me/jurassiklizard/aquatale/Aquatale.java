package me.jurassiklizard.aquatale;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import me.jurassiklizard.aquatale.enums.MoveDirection;
import me.jurassiklizard.aquatale.utils.BoundingBox;
import me.jurassiklizard.aquatale.utils.Utils;

import java.util.HashMap;

public class Aquatale extends GameApplication {
    private Entity player;
    private Entity light;
    private HashMap<Entity, BoundingBox> entityViews = new HashMap<>();
    public static Aquatale instance;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(960);
        gameSettings.setHeight(540);
        gameSettings.setTitle("Aquatale");
        gameSettings.setVersion("0.1");
        gameSettings.setIntroEnabled(false);
    }
    

    @Override
    protected void initGame() {
        instance = this;
        Rectangle rectangle = new Rectangle(25, 25, Color.BLUE);
        Rectangle r = new Rectangle(100, 100, Color.BLACK);
        Vec2 center = Utils.getCenterScreen();
        player = FXGL.entityBuilder()
                .at(Utils.getRectangleCenterPosition(center.x, center.y, rectangle))
                .view(rectangle)
                .buildAndAttach();
        light = FXGL.entityBuilder()
                .at(Utils.getRectangleCenterPosition(center.x, center.y, r))
                .view(r)
                .buildAndAttach();

        FXGL.getGameScene().setCursor(Cursor.DEFAULT);
        FXGL.getGameScene().setBackgroundRepeat("background.png");
        entityViews.put(player, new BoundingBox(rectangle.getWidth(), rectangle.getHeight()));
        entityViews.put(light, new BoundingBox(r.getWidth(), r.getHeight()));
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            Utils.move(player, MoveDirection.RIGHT); // move right 5 pixels
        });

        FXGL.onKey(KeyCode.A, () -> {
            Utils.move(player, MoveDirection.LEFT); // move left 5 pixels
        });

        FXGL.onKey(KeyCode.W, () -> {
            Utils.move(player, MoveDirection.UP); // move up 5 pixels
        });

        FXGL.onKey(KeyCode.S, () -> {
            Utils.move(player, MoveDirection.DOWN); // move down 5 pixels
        });
    }

    @Override
    protected void onUpdate(double tpf){
        Input input = FXGL.getInput();
        Point2D pos = input.getMousePositionWorld();
        Vec2 centerPos = Utils.getRectangleCenterPosition(pos.getX(), pos.getY(), entityViews.get(light));
        light.setPosition(centerPos);
    }

    public static void main(String[] args) { launch(args); }

    public Entity getPlayer() {
        return player;
    }

    public Aquatale setPlayer(Entity player) {
        this.player = player;
        return this;
    }

    public Entity getLight() {
        return light;
    }

    public Aquatale setLight(Entity light) {
        this.light = light;
        return this;
    }

    public HashMap<Entity, BoundingBox> getEntityViews() {
        return entityViews;
    }

    public Aquatale setEntityViews(HashMap<Entity, BoundingBox> entityViews) {
        this.entityViews = entityViews;
        return this;
    }

    public static Aquatale getInstance(){ return instance; }
}
