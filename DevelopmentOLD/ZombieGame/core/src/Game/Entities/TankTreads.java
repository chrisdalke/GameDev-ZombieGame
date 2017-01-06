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
import com.badlogic.gdx.graphics.Color;

public class TankTreads extends GameObject {

    int lifetime = 800;
    int lifetimeTimer = 0;

    public TankTreads(float x, float y,float angle){
        super(TextureLoader.load("Resources/Textures/treads.png"));
        width = 2.8f;
        height = 2.8f;
        this.x = x;
        this.y = y;
        layer = -1;
        this.angle = angle;
        noShadow = true;
    }

    public void simulate(){
        lifetimeTimer++;
        if (lifetimeTimer > lifetime){
            kill();
        }
    }

    @Override
    public void render(){
        Renderer.setColor(new Color(255,255,255,((float)(lifetime - lifetimeTimer)/(float)lifetime)*0.8f));
        super.render();
        Renderer.resetColor();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////