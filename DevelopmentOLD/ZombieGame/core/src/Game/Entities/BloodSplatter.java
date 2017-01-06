////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: BloodSplatter
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class BloodSplatter extends GameObject {

    int lifetime = 800;
    int lifetimeTimer = 0;
    ParticleEffect blood;

    public BloodSplatter(float x, float y){
        super(TextureLoader.load("Resources/Textures/blood.png"));
        width = 12.0f;
        height = 12.0f;
        this.x = x;
        this.y = y;
        angle = (float)Math.random()*360;
        layer = -1;
        noShadow = true;
        Game.addObject(new BloodSplatterParticle(x,y));
    }

    public void simulate(){
        width += 0.002f;
        height += 0.002f;

        lifetimeTimer++;
        if (lifetimeTimer > lifetime){
            kill();
        }
    }

    @Override
    public void render(){
        Renderer.setColor(new Color(255,255,255,(float)(lifetime - lifetimeTimer)/(float)lifetime));
        super.render();
        Renderer.resetColor();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////