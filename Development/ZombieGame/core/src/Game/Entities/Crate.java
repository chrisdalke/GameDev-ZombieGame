////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: WorldTile
////////////////////////////////////////////////

package Game.Entities;

import Engine.Audio.Audio;
import Engine.Game.GameObject;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Crate extends GameObject {
    private int health = 1;

    public Crate(float x, float y) {
        super(TextureLoader.load("Assets/Textures/crate.png",1,1));
        width = 0f;
        height = 0f;
        noShadow = false;
        layer = 1;
        this.x = x;
        this.y = y;
        radius = 5;
        Game.getGameInstance().collisionManager.addObject(this);
        Game.getGameInstance().collisionManager.setKinematic(this);
    }

    @Override
    public void simulate() {
        super.simulate();

        if (width < 10.0f){
            width += 0.2f;
            height += 0.2f;
            angle++;
        }

        List<GameObject> collidingBullets = Game.getGameInstance().collisionManager.getCollidersWithType(this,Bullet.class);
        if (collidingBullets.size()> 0){
            for (GameObject collidingBullet : collidingBullets){
                collidingBullet.kill();
                health -= ((Bullet)collidingBullet).damage;
            }
        }

        //Check health
        if (health <= 0){
            //Add some XP and ammo
            for (int i = 0; i <= ThreadLocalRandom.current().nextInt(0, 4); i++){
                Game.getGameInstance().addObject(new XP(x,y));
                Game.getGameInstance().addObject(new Ammo(x,y));
            }

            kill();

        }
    }

    @Override
    public void render() {
        super.render();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////