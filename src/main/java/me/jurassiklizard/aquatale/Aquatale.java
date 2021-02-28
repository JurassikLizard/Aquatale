package me.jurassiklizard.aquatale;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import me.jurassiklizard.aquatale.enums.MoveDirection;
import me.jurassiklizard.aquatale.utils.BoundingBox;
import me.jurassiklizard.aquatale.utils.Utils;

import java.util.HashMap;

public class Aquatale extends GameApplication {
    private Entity player;
    private Entity light;
    private HashMap<Entity, BoundingBox> entityViews = new HashMap<>();
    public static Aquatale instance;
    private int totalBlackScreenWidth;
    private int totalBlackScreenHeight;
    private int multiplier = 3;

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
        totalBlackScreenWidth = FXGL.getAppWidth() * multiplier;
        totalBlackScreenHeight = FXGL.getAppHeight() * multiplier;

        Rectangle rectangle = new Rectangle(25, 25, Color.BLUE);
        Vec2 center = Utils.getCenterScreen();
        Rectangle r = new Rectangle(totalBlackScreenWidth, totalBlackScreenHeight);
        Circle mouseLight = new Circle(center.x * multiplier,center.y * multiplier,100, Color.BLACK);
        Circle playerLight = new Circle(multiplier*center.x,multiplier*center.y,100, Color.BLACK);
        Shape flashlight = Shape.subtract(r, Shape.union(mouseLight, playerLight));

        light = FXGL.entityBuilder()
                .at(Utils.getRectangleCornerPosition(center.x, center.y, r))
                .view(flashlight)
                .buildAndAttach();
        player = FXGL.entityBuilder()
                .at(Utils.getRectangleCornerPosition(center.x, center.y, rectangle))
                .view(rectangle)
                .buildAndAttach();

        FXGL.getGameScene().setCursor(Cursor.DEFAULT);
        FXGL.getGameScene().setBackgroundRepeat("background.png");
        entityViews.put(player, new BoundingBox(rectangle.getWidth(), rectangle.getHeight()));
        entityViews.put(light, new BoundingBox(r.getWidth(), r.getHeight()));

        // Sound
        Music music = FXGL.getAssetLoader().loadMusic("piano_bass.mp3");
        FXGL.getAudioPlayer().loopMusic(music);
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
            Input input = FXGL.getInput();
        });
    }

    @Override
    protected void onUpdate(double tpf){
        light.removeFromWorld();
        entityViews.remove(light);

        Input input = FXGL.getInput();
        Point2D mousePos = input.getMousePositionWorld();
        Point2D playerPos = player.getPosition();

        Rectangle r = new Rectangle(FXGL.getAppWidth()*multiplier, FXGL.getAppHeight()*multiplier);
        //Circle mouseLight = new Circle(mousePos.getX() + (FXGL.getAppWidth() * ((multiplier - 1) / 2.0)), mousePos.getY() + (FXGL.getAppHeight() * ((multiplier - 1) / 2.0)),100, Color.BLACK);
        //Circle playerLight = new Circle(playerPos.getX() + (FXGL.getAppWidth() * ((multiplier - 1) / 2.0)), playerPos.getY() + (FXGL.getAppHeight() * ((multiplier - 1) / 2.0)),100, Color.BLACK);
        Circle mouseLight = new Circle(mousePos.getX() + FXGL.getAppWidth(), mousePos.getY() + FXGL.getAppHeight(),100, Color.BLACK);
        Circle playerLight = new Circle(playerPos.getX() + FXGL.getAppWidth(), playerPos.getY() + FXGL.getAppHeight(),100, Color.BLACK);
        Shape r2 = Shape.subtract(r, mouseLight);
        Shape r3 = Shape.subtract(r2, playerLight);

        light = FXGL.entityBuilder()
                .at(Utils.getRectangleCornerPosition(Utils.getCenterScreen().x, Utils.getCenterScreen().y, r))
                .view(r3)
                .buildAndAttach();
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
