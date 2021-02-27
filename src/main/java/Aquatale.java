import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Cursor;

public class Aquatale extends GameApplication {
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
        FXGL.getGameScene().setCursor(Cursor.DEFAULT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
