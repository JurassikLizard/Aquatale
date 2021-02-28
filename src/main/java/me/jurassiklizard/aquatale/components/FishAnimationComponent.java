package me.jurassiklizard.aquatale.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import me.jurassiklizard.aquatale.Aquatale;
import me.jurassiklizard.aquatale.enums.FishType;

import java.util.Random;

public class FishAnimationComponent extends Component {
    private int speed = 50;

    private AnimatedTexture texture;
    private AnimationChannel animSwim;
    public FishType type;
    private boolean swimsRight;

    public FishAnimationComponent(boolean swimsRight){
        int random = new Random().nextInt(4);
        if(random == 1){
            speed = 70;
            animSwim = new AnimationChannel(FXGL.image("fish.png"), 4, 32, 32, Duration.seconds(1), 0, 3);
            type = FishType.REGULAR;
        }
        else if(random == 2){
            speed = 65;
            animSwim = new AnimationChannel(FXGL.image("fish-big.png"), 4, 54, 49, Duration.seconds(1), 0, 3);
            type = FishType.BIG;
        }
        else{
            speed = 80;
            animSwim = new AnimationChannel(FXGL.image("fish-dart.png"), 4, 39, 20, Duration.seconds(1), 0, 3);
            type = FishType.DART;
        }

        this.swimsRight = swimsRight;
        texture = new AnimatedTexture(animSwim);
    }

    @Override
    public void onAdded() {
        if(type == FishType.BIG) entity.getTransformComponent().setScaleOrigin(new Point2D(27, 24.5));
        if(type == FishType.REGULAR) entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        if(type == FishType.DART) entity.getTransformComponent().setScaleOrigin(new Point2D(19.5, 10));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if(swimsRight) {
            getEntity().setScaleX(1);
            entity.translateX(speed * tpf);
            if(entity.getX() > FXGL.getAppWidth() + 10){
                entity.removeFromWorld();
                Aquatale.instance.fish.remove(entity);
            }
        }
        if(!swimsRight) {
            getEntity().setScaleX(-1);
            entity.translateX(speed * -tpf);
            if(entity.getX() < -10){
                entity.removeFromWorld();
                Aquatale.instance.fish.remove(entity);
            }
        }

        if(texture.getAnimationChannel() != animSwim){
            texture.loopAnimationChannel(animSwim);
        }
    }
}
