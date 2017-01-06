////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: AbstractPlayer
////////////////////////////////////////////////

package Game.Entities;

import Engine.Audio.Audio;
import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import Game.Game;

import java.util.List;

public class AbstractPlayer extends GameObject {
    public int score = 0;
    public int health = 100;
    public int damageFrame =0;
    public int ammo = 100;

    public AbstractPlayer() {
        super(null);
        layer = 1;
        Game.collisionManager.addObject(this);
    }

    @Override
    public void simulate() {
        super.simulate();

        //Handle XP
        List<GameObject> collidingXP = Game.collisionManager.getCollidersWithType(this,XP.class);
        if (collidingXP.size()> 0){
            for (GameObject aCollidingXP : collidingXP){
                aCollidingXP.kill();
                score +=10;
                //if (!Audio.soundPlaying("coin")) {
                Audio.playSound("coin");
                //}
            }
        }

        //Handle ammo
        List<GameObject> collidingAmmo = Game.collisionManager.getCollidersWithType(this,Ammo.class);
        if (collidingAmmo.size()> 0){
            for (GameObject aCollidingAmmo : collidingAmmo){
                aCollidingAmmo.kill();
                ammo +=10;
                ammo = Math.min(ammo,100);
                //if (!Audio.soundPlaying("coin")) {
                Audio.playSound("coin");
                //}
            }
        }
        //Check health
        if (health < 0){
            //It's game over!!
            kill();
        }

        //World boundaries

        x = Math.min(Renderer.viewWidth/2,Math.max(x,-Renderer.viewWidth/2));
        y = Math.min(Renderer.viewHeight/2,Math.max(y,-Renderer.viewHeight/2));

    }

    @Override
    public void render() {
        super.render();
    }


    public void hurt(int damage){
        health -= damage;
        damageFrame = 1;
    }
}


////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////