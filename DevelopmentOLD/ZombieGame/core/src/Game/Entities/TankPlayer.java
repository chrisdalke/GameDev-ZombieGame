////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Player
////////////////////////////////////////////////

package Game.Entities;

import Engine.Audio.Audio;
import Engine.Game.GameObject;
import Engine.Input.Input;
import Engine.Renderer.Renderer;
import Engine.Renderer.Text;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;
import com.badlogic.gdx.math.MathUtils;

public class TankPlayer extends AbstractPlayer {

    private float speed = 0.4f;
    private float decay = 0.94f;

    int shootCooldownNumFrames = 3;
    int shootCooldownCounter = 0;
    public float turretAngle = 0;
    public float currentSpeed = 0;

    int tankTreadTimer = 0;

    Texture tankTexture;

    public TankPlayer() {
        tankTexture = TextureLoader.load("Resources/Textures/tanks/KV-2_strip2.png", 2,1);
        width = 13.2f * 1.5f;
        height = 25.4f * 1.5f;
        radius = 13;
        //Game.collisionManager.addObject(this);
        Game.collisionManager.setKinematic(this);


        Audio.loadSound("engine","Resources/Audio/machines/aircompressor.wav");

        Audio.loadSound("turret","Resources/Audio/machines/electric.wav");
    }

    public void simulate(){

        //We are inactive if the current player isn't us
        //In inactive mode, if the player presses E within the radius, we take over as player!
        if (Game.player != this){
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.E)){
                score = Game.player.score;
                health = Game.player.health;
                ammo = Game.player.ammo;
                Game.player.kill();
                Game.player = this;
                Audio.loopSound("engine");
            }

        } else {

            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.E)){

                Player playerObj = new Player();
                playerObj.x = x;
                playerObj.y = y+15;
                playerObj.health = this.health;
                playerObj.ammo = this.ammo;
                playerObj.score = this.score;
                Game.addObject(playerObj);
                Game.player = playerObj;
                Audio.stopSound("engine");
            }

            int state = 0;

            if (Input.getKey(com.badlogic.gdx.Input.Keys.W)) {
                //move forwards
                currentSpeed = Math.min(currentSpeed + 0.04f, speed);
                state = 1;
            }
            if (Input.getKey(com.badlogic.gdx.Input.Keys.S)) {
                //move backwards
                currentSpeed = Math.max(currentSpeed - 0.04f, -speed);
                state = 1;
            }
            if (Input.getKey(com.badlogic.gdx.Input.Keys.A)) {
                angle++;
            }
            if (Input.getKey(com.badlogic.gdx.Input.Keys.D)) {
                angle--;
            }

            if (state == 1){
                tankTreadTimer++;
                if (tankTreadTimer > 5){
                    //Place tank tread objects
                    GameObject leftTread = new TankTreads(x+((float)Math.cos(Math.toRadians(angle+90))*7),y+((float)Math.sin(Math.toRadians(angle+90))*7),angle);
                    GameObject rightTread = new TankTreads(x+((float)Math.cos(Math.toRadians(angle-90))*7),y+((float)Math.sin(Math.toRadians(angle-90))*7),angle);
                    Game.addObject(leftTread);
                    Game.addObject(rightTread);
                    tankTreadTimer = 0;
                }
            }

            //Point turret at the mouse
            float mx = Renderer.getMouseWorldX();
            float my = Renderer.getMouseWorldY();
            float centerx = x;
            float centery = y;
            float targetTurretAngle = 180 - (float) Math.toDegrees(Math.atan2(mx - centerx, my - centery));
            turretAngle= MathUtils.lerpAngleDeg(turretAngle,targetTurretAngle,0.9f);
            if (Math.abs(targetTurretAngle - turretAngle)> 1.0f){
                //Audio.loopSound("turret");
            } else {
                Audio.stopSound("turret");
            }

            //Handle weapon
            shootCooldownCounter++;
            if (shootCooldownCounter > shootCooldownNumFrames & Input.getMouseClicked() == 1) {
                shootCooldownCounter = 0;
                Game.screenShakeIntensity = 0.5f;
                float bulletX = x +((float)Math.cos(Math.toRadians(turretAngle+90))*-20);
                float bulletY = y +((float)Math.sin(Math.toRadians(turretAngle+90))*-20);
                Game.addObject(new RifleBullet(bulletX, bulletY, turretAngle-90-10));
                Game.addObject(new RifleBullet(bulletX, bulletY, turretAngle-90-5));
                Game.addObject(new RifleBullet(bulletX, bulletY, turretAngle-90));
                Game.addObject(new RifleBullet(bulletX, bulletY, turretAngle-90+5));
                Game.addObject(new RifleBullet(bulletX, bulletY, turretAngle-90+10));
                Audio.playSound("bullet1");
            }

        }

        vx = (float) Math.cos(Math.toRadians(angle)) * currentSpeed;
        vy = (float) Math.sin(Math.toRadians(angle)) * currentSpeed;

        super.simulate();
        currentSpeed *= decay;

        //x = Math.min(Renderer.viewWidth / 2, Math.max(x, -Renderer.viewWidth / 2));
        //y = Math.min(Renderer.viewHeight / 2, Math.max(y, -Renderer.viewHeight / 2));


        //Check health
        if (health < 0) {
            //It's game over!!
            kill();
        }

    }

    public void render() {
        Renderer.draw(tankTexture.getRegion(0), x - (width / 2), y - (height / 2), width, height, angle + 90);
        Renderer.draw(tankTexture.getRegion(1), x - (width / 2), y - (height / 2), width, height, turretAngle);

        if (Game.player != this) {
            double plrDistance = Math.hypot(x - Game.player.x, y - Game.player.y);
            if (plrDistance < 10.0f) {
                Text.centerDraw(x, y, "Press E to enter Tank");
            }
        }
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////