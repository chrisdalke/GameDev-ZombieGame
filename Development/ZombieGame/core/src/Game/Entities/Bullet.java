////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Player
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Textures.Texture;

public class Bullet extends GameObject {

    public float speed = 2.5f;
    public int damage = 5;
    public float knockback = 0.1f;
    public int age = 0;
    public int ageLimit = 200;

    public Bullet(Texture tex) {
        super(tex);
    }

    public void simulate(){
        vx = (float)Math.cos(Math.toRadians(angle))*speed;
        vy = (float)Math.sin(Math.toRadians(angle))*speed;

        super.simulate();

        age++;
        if (age > ageLimit){
            kill();
        }
        /*
        //Kill if outside the screen
        if (x < -Renderer.viewWidth/2 || x > Renderer.viewWidth/2 || y < -Renderer.viewHeight/2 || y > Renderer.viewHeight/2){
            kill();
        }
        */
    }

    public void render(){
        super.render();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////