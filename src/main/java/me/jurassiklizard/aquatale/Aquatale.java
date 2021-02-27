package me.jurassiklizard.aquatale;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class Aquatale extends GameApplication {
    private Entity player;
    private Entity light;
    private HashMap<Entity, Node> entityViews = new HashMap<>();
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
        entityViews.put(player, rectangle);
        entityViews.put(light, r);

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

        FXGL.onKey(KeyCode.SPACE, () -> {
            Input input = FXGL.getInput();
            Point2D pos =  input.getMousePositionWorld();
            light.setPosition(pos); // Update mouse cursor position for flashlight
        });
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

    public HashMap<Entity, Node> getEntityViews() {
        return entityViews;
    }

    public Aquatale setEntityViews(HashMap<Entity, Node> entityViews) {
        this.entityViews = entityViews;
        return this;
    }

    public static Aquatale getInstance(){ return instance; }
}
