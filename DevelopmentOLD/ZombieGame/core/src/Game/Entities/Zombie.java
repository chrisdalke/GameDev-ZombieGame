////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Zombie
////////////////////////////////////////////////

package Game.Entities;


import Engine.Audio.Audio;
import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Zombie extends GameObject {
    private int health = 20;

    private float speed = 0.25f;

    private Texture[] moveFrames;
    private Texture[] idleFrames;
    private Texture[] attackFrames;
    private int frame;
    private int frameTimer;
    private int frameDelay = 1;
    private float kvx;
    private float kvy;

    private int attackTimer = 0;
    private int attackCooldown = 20;
    private int damage = 5;

    private GameObject target;

    public Zombie() {
        super(null);
        moveFrames = new Texture[17];
        idleFrames = new Texture[17];
        attackFrames = new Texture[9];
        for (int i =0; i <= 16; i++){
            moveFrames[i]= TextureLoader.load("Resources/Textures/zombie/skeleton-move_"+i+".png", 1,1);
        }
        for (int i =0; i <= 16; i++){
            idleFrames[i]= TextureLoader.load("Resources/Textures/zombie/skeleton-idle_"+i+".png", 1,1);
        }

        for (int i =0; i <= 8; i++){
            attackFrames[i]= TextureLoader.load("Resources/Textures/zombie/skeleton-attack_"+i+".png", 1,1);
        }


        width = 10f;
        height = 10f;
        x = 0;
        y = 0;
        layer = 1;
        radius = 3;
        Game.collisionManager.addObject(this);

        for (int i = 1; i < 19; i++){
            Audio.loadSound("zombie"+i,"Resources/Audio/zombies/new/zombie-"+i+".wav");
        }

    }

    public void simulate(){
        //Find whatever player or turret is the nearest object
        //And aim towards / try to attack it

        //Look for player or turrets and figure out which is nearest
        target = Game.player;
        float dist = (float)Math.hypot(x-target.x,y-target.y);

        for (GameObject tempTarget : Game.gameObjects){
            if (tempTarget instanceof Turret){
                float tempDist = (float)Math.hypot(x-tempTarget.x,y-tempTarget.y);
                if (tempDist < dist){
                        dist = tempDist;
                        target = tempTarget;
                }
            }
        }


        //Point at the center of target
        float mx = target.x;
        float my = target.y;
        float centerx = x;
        float centery = y;
        angle = 90-(float)Math.toDegrees(Math.atan2(mx-centerx,my-centery));

        if (Math.abs(mx-centerx) > 0.1f || Math.abs(mx-centerx) > 0.1f){

            //Set velocity based on our direction
            vx = (float)Math.cos(Math.toRadians(angle))*speed;
            vy = (float)Math.sin(Math.toRadians(angle))*speed;
        }




        List<GameObject> collidingBullets = Game.collisionManager.getCollidersWithType(this,Bullet.class);
        if (collidingBullets.size()> 0){
            for (GameObject collidingBullet : collidingBullets){
                collidingBullet.kill();
                health -= ((Bullet)collidingBullet).damage;

                //Do Knockback from bullet

                kvx += collidingBullet.vx * ((Bullet)collidingBullet).knockback;
                kvy += collidingBullet.vy * ((Bullet)collidingBullet).knockback;

            }
        }
        vx += kvx;
        vy += kvy;
        kvx *= 0.9f;
        kvy *= 0.9f;


        attackTimer++;
        if (attackTimer > attackCooldown){
            attackTimer=0;
            //Check collide with target and attack it

            List<GameObject> collidingPlayer = Game.collisionManager.getCollidersWithType(this,AbstractPlayer.class);
            if (collidingPlayer.size()> 0 && collidingPlayer.contains(target)){
                ((AbstractPlayer)target).hurt(damage);
            }


            List<GameObject> collidingTurret = Game.collisionManager.getCollidersWithType(this,Turret.class);
            if (collidingTurret.size()> 0 && collidingTurret.contains(target)){
                ((Turret)target).hurt(damage);
            }



        }

        //Check health
        if (health <= 0){
            BloodSplatter bloodSplatter = new BloodSplatter(x,y);
            Game.addObject(bloodSplatter);

            //Add some XP
            for (int i = 0; i <= ThreadLocalRandom.current().nextInt(0, 4); i++){
                Game.addObject(new XP(x,y));
            }

            //Add some ammo?
            if (ThreadLocalRandom.current().nextInt(0, 6) == 0){
                Game.addObject(new Ammo(x,y));
            }

            kill();
            //Play a random sound
            Audio.playSound("zombie"+ThreadLocalRandom.current().nextInt(1, 19));

        }

        //Handle animate
        frameTimer++;
        if (frameTimer > frameDelay){
            frameTimer = 0;
            frame++;
        }

        super.simulate();
    }

    public void render(){
        //TODO Interpolate between frames

        if (vx == 0 && vy == 0){
            //Idle
            Renderer.draw(moveFrames[frame % 17].getRegion(), x - (width / 2), y - (height / 2), width, height, angle);
        } else {
            //Move
            Renderer.draw(moveFrames[frame % 17].getRegion(), x - (width / 2), y - (height / 2), width, height, angle);
        }

        //Display health
        //Text.draw(x,y,Integer.toString(health));

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////