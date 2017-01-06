////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Turret
////////////////////////////////////////////////

package Game.Entities;

import Engine.Audio.Audio;
import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

public class Turret extends GameObject {

    public int health = 100;

    public float turretAngle;

    Texture turretBaseTex;
    Texture turretGunTex;
    Texture turretRangeIndicator;

    int shootCooldownNumFrames = 4;
    int shootCooldownCounter = 0;
    float searchDist = 50;


    public Turret() {
        super(null);
        turretBaseTex = TextureLoader.load("Resources/Textures/turret_base.png",1,1);
        turretGunTex = TextureLoader.load("Resources/Textures/turret_gun.png",1,1);
        turretRangeIndicator = TextureLoader.load("Resources/Textures/blue-circle.png",1,1);
        noShadow = false;
        width = 10f;
        height = 10f;
        layer = 3;
        radius = 5;
        Game.collisionManager.addObject(this);
        Game.collisionManager.setKinematic(this);
    }

    @Override
    public void simulate() {
        super.simulate();

        //Look for zombies within the turret range and fire at the nearest zombie
        GameObject closestZombie = null;
        float closestZombieDist = 9999;
        for (GameObject zombie : Game.gameObjects){
            if (zombie instanceof Zombie){
                float dist = (float)Math.hypot(x-zombie.x,y-zombie.y);
                if (dist <= searchDist/2){
                    if (dist < closestZombieDist){
                        closestZombie = zombie;
                        closestZombieDist = dist;
                    }
                }
            }
        }

        if (closestZombie != null){
            //Shoot at the closest zombie
            float mx = closestZombie.x;
            float my = closestZombie.y;
            float centerx = x;
            float centery = y;
            turretAngle = 90-(float)Math.toDegrees(Math.atan2(mx-centerx,my-centery));

            shootCooldownCounter++;
            if (shootCooldownCounter > shootCooldownNumFrames) {
                shootCooldownCounter = 0;
                Game.addObject(new RifleBullet(x, y, turretAngle));
                Audio.playSound("bullet1");
            }
        }

        //Check health
        if (health < 0){
            kill();
        }

    }

    @Override
    public void render() {
        //Renderer.setColor(new Color(255,255,255,0.2f));
        //Renderer.draw(turretRangeIndicator.getRegion(),x-searchDist/2,y-searchDist/2,searchDist,searchDist);
        //Renderer.setColor(Color.WHITE);
        Renderer.draw(turretBaseTex.getRegion(), x - (width / 2), y - (height / 2), width, height, angle);
        Renderer.draw(turretGunTex.getRegion(), x - (width / 2), y - (height / 2), width, height, turretAngle);

    }



    public void hurt(int damage){
        health -= damage;

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////