package me.jurassiklizard.aquatale;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import me.jurassiklizard.aquatale.components.PlayerAnimationComponent;
import me.jurassiklizard.aquatale.enums.MoveDirection;
import me.jurassiklizard.aquatale.utils.BoundingBox;
import me.jurassiklizard.aquatale.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Aquatale extends GameApplication {
    public Entity player;
    public Entity light;
    public HashMap<Entity, BoundingBox> entityViews = new HashMap<>();
    public ArrayList<Entity> fish = new ArrayList<>();
    public static Aquatale instance;
    public int multiplier = 3;
    private boolean collided = false;
    public int fishAmount = 0;
    public Text fishText;

    public enum EntityType {
        PLAYER, ENEMY, FISH
    }
    public static class AquataleFactory implements EntityFactory {
        @Spawns("player")
        public Entity newPlayer(SpawnData data) {
            var rectangle = new Rectangle(70, 70, Color.TRANSPARENT);

            return entityBuilder()
                    .type(EntityType.PLAYER)
                    .from(data)
                    .viewWithBBox(rectangle)
                    .collidable()
                    .with(new PlayerAnimationComponent())
                    .build();
        }
        @Spawns("enemy")
        public Entity newEnemy(SpawnData data) {
            var view = new Rectangle(80, 20, Color.RED);
            view.setStroke(Color.GRAY);
            view.setStrokeWidth(0.5);

            animationBuilder()
                    .interpolator(Interpolators.SMOOTH.EASE_OUT())
                    .duration(Duration.seconds(0.5))
                    .repeatInfinitely()
                    .animate(view.fillProperty())
                    .from(Color.RED)
                    .to(Color.DARKRED)
                    .buildAndPlay();

            return entityBuilder()
                    .type(EntityType.ENEMY)
                    .from(data)
                    .viewWithBBox(view)
                    .collidable()
                    .with(new ProjectileComponent(new Point2D(-1, 0), FXGLMath.random(50, 100)))
                    .build();
        }
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(960);
        gameSettings.setHeight(540);
        gameSettings.setTitle("Aquatale");
        gameSettings.setVersion("1.0.0-Release");
        gameSettings.setAppIcon("icon.png");
        gameSettings.setIntroEnabled(false);
        //gameSettings.setProfilingEnabled(true);
        gameSettings.setCredits(List.of("UnsocialSamurott", "JurassikLizard", "https://github.com/JurassikLizard/Aquatale"));
    }


    @Override
    protected void initGame() {
        instance = this;

        getGameWorld().addEntityFactory(new AquataleFactory());
        Rectangle rectangle = new Rectangle(25, 25, Color.BLUE);
        Vec2 center = Utils.getCenterScreen();
        Rectangle r = new Rectangle(FXGL.getAppWidth() * multiplier, FXGL.getAppHeight() * multiplier);
        Circle mouseLight = new Circle(center.x * multiplier,center.y * multiplier,100, Color.BLACK);
        Circle playerLight = new Circle(multiplier * center.x,multiplier * center.y,100, Color.BLACK);
        Shape flashlight = Shape.subtract(r, Shape.union(mouseLight, playerLight));

        light = FXGL.entityBuilder()
                .at(new Vec2(0, 0))
                .view(flashlight)
                .buildAndAttach();
        player = spawn("player", center.x, center.y);

        FXGL.getGameScene().setCursor(Cursor.DEFAULT);
        FXGL.getGameScene().setBackgroundRepeat("background.png");
        entityViews.put(player, new BoundingBox(80, 80));
        entityViews.put(light, new BoundingBox(r.getWidth(), r.getHeight()));

        // Sound
        Music music = FXGL.getAssetLoader().loadMusic("piano_bass.mp3");
        FXGL.getAudioPlayer().loopMusic(music);

        // UI
        String fishString = fishAmount + " fish in your net";
        Text testPosTest = FXGL.addText(fishString, -100, -100);
        Bounds bounds = testPosTest.getLayoutBounds();
        Vec2 textLocation = Utils.getRectangleCornerPosition(FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() - 10, bounds.getWidth(), bounds.getHeight());
        testPosTest.setText("");
        fishText = FXGL.addText(fishString, textLocation.x, textLocation.y);
        fishText.setTextOrigin(VPos.BOTTOM);
        fishText.setFill(Color.WHITE);
        fishText.setTextAlignment(TextAlignment.CENTER);
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.PLAYER, EntityType.ENEMY, (player, enemy) -> {
            enemy.removeFromWorld();
            fish.remove(enemy);
            collided = true;
            fishAmount = 0;
        });
        onCollisionBegin(EntityType.PLAYER, EntityType.FISH, (player, fish) -> {
            fish.removeFromWorld(); // Remove fish object that collided with player
            this.fish.remove(fish);
            fishAmount++;
        });
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            if(player.getRightX() < FXGL.getAppWidth())
            {
                Utils.move(player, MoveDirection.RIGHT); // move right 5 pixels
            }
        });

        FXGL.onKey(KeyCode.A, () -> {
            if(player.getX() > 0)
            {
                Utils.move(player, MoveDirection.LEFT); // move left 5 pixels
            }
        });

        FXGL.onKey(KeyCode.W, () -> {
            if(player.getY() > 0)
            {
                Utils.move(player, MoveDirection.UP); // move up 5 pixels
            }
        });

        FXGL.onKey(KeyCode.S, () -> {
            if(player.getBottomY() < FXGL.getAppHeight())
            {
                Utils.move(player, MoveDirection.DOWN); // move down 5 pixels
            }
        });

        FXGL.onKey(KeyCode.RIGHT, () -> {
            if(player.getRightX() < FXGL.getAppWidth())
            {
                Utils.move(player, MoveDirection.RIGHT); // move right 5 pixels
            }
        });

        FXGL.onKey(KeyCode.LEFT, () -> {
            if(player.getX() > 0)
            {
                Utils.move(player, MoveDirection.LEFT); // move left 5 pixels
            }
        });

        FXGL.onKey(KeyCode.UP, () -> {
            if(player.getY() > 0)
            {
                Utils.move(player, MoveDirection.UP); // move up 5 pixels
            }
        });

        FXGL.onKey(KeyCode.DOWN, () -> {
            if(player.getBottomY() < FXGL.getAppHeight())
            {
                Utils.move(player, MoveDirection.DOWN); // move down 5 pixels
            }
        });
    }

    @Override
    protected void onUpdate(double tpf){
        Utils.spawnFish(tpf);
        Utils.updateFlashLights();
        fishText.setText(fishAmount + " fish in your net!");
        if(collided)
        {
            collided = false;
            showMessage("Game Over!", () -> getGameController().startNewGame());
        }
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
