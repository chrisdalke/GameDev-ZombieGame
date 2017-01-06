////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: BloodSplatter
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class BloodSplatterParticle extends GameObject {

    ParticleEffect blood;

    public BloodSplatterParticle(float x, float y){
        super(null);
        this.x = x;
        this.y = y;
        layer = 2;
        blood = new ParticleEffect();
        blood.load(Gdx.files.internal("Resources/Particles/blood1.p"),Gdx.files.internal("Resources/Textures"));
        blood.start();
    }

    public void simulate(){

        blood.setPosition(x,y);
        blood.update(0.01f);

        if (blood.isComplete()){
            kill();
        }
    }

    @Override
    public void render(){
        blood.draw(Renderer.getBatch());
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////