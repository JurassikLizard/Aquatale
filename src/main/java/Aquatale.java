import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.Utils;

public class Aquatale extends GameApplication {
    private Entity player;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Aquatale");
        gameSettings.setVersion("0.1");
        gameSettings.setIntroEnabled(false);
    }

    @Override
    protected void initGame() {
        Rectangle rectangle = new Rectangle(25, 25, Color.BLUE);
        Vec2 center = Utils.getCenterScreen();
        player = FXGL.entityBuilder()
                .at(Utils.getRectangleCenterPosition(center.x, center.y, rectangle))
                .view(rectangle)
                .buildAndAttach();
        FXGL.getGameScene().setCursor(Cursor.DEFAULT);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Entity getPlayer() {
        return player;
    }

    public Aquatale setPlayer(Entity player) {
        this.player = player;
        return this;
    }
}
