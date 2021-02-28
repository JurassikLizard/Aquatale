package me.jurassiklizard.aquatale.comonents;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationComponent extends Component {
    private Vec2 speed = new Vec2(0, 0);

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animSwim;

    public AnimationComponent(){
        animIdle = new AnimationChannel(FXGL.image("player-idle.png"), 6, 80, 80, Duration.seconds(1), 0, 5);
        animSwim = new AnimationChannel(FXGL.image("player-swiming.png"), 7, 80, 80, Duration.seconds(1), 0, 6);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(40, 40));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speed.x * tpf);
        entity.translateY(speed.y * tpf);

        if (speed.x != 0 || speed.y != 0) {
            if(speed.x == 0 && speed.y != 0){
                if(texture.getAnimationChannel() == animSwim){
                    texture.loopAnimationChannel(animIdle);
                }
            }
            else{
                if (texture.getAnimationChannel() == animIdle) {
                    texture.loopAnimationChannel(animSwim);
                }
            }
        }
        else{
            if(texture.getAnimationChannel() == animSwim) texture.loopAnimationChannel(animIdle);
        }

        speed.x = 0;
        speed.y = 0;
    }

    public void moveRight() {
        speed.x = 50;

        getEntity().setScaleX(1);
    }

    public void moveLeft() {
        speed.x = -50;

        getEntity().setScaleX(-1);
    }

    public void moveUp(){
        speed.y = -50;
    }

    public void moveDown(){
        speed.y = 50;
    }
}
