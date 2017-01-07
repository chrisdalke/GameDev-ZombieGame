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
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

public class Player extends AbstractPlayer {

    private float speed = 0.5f;
    private float decay = 0.7f;
    int state = 0;
    int shootCooldownNumFrames = 15;
    int shootCooldownCounter = 0;
    int weapon = 0;

    Texture playerTex;

    public Player() {
        playerTex = TextureLoader.load("Assets/Textures/player/shotgun/idle/survivor-idle_shotgun_0.png", 1,1);

        width = 10f;
        height = 10f;
        radius = 4;

        Audio.loadSound("bullet1","Assets/Audio/shots/pistol.wav");
        Audio.loadSound("coin","Assets/Audio/inventory/caching.wav");
    }

    public void simulate(){
        state = 0;
        if (forward){
            vy += speed * Input.directionalVert;
            state = 1;
        }
        if (backward){
            vy -= speed * -Input.directionalVert;
            state = 1;
        }
        if (left){
            vx -= speed * -Input.directionalHoriz;
            state = 1;
        }
        if (right){
            vx += speed* Input.directionalHoriz;
            state = 1;
        }
        if (Input.getKey(com.badlogic.gdx.Input.Keys.K)){
            kill();
        }
        if (Input.getKey(com.badlogic.gdx.Input.Keys.NUM_1)){
            weapon = 0;
        }
        if (Input.getKey(com.badlogic.gdx.Input.Keys.NUM_2)){
            //weapon = 1;
        }
        if (Input.getKey(com.badlogic.gdx.Input.Keys.NUM_3)){
            //weapon = 2;
        }

        if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.SPACE)){
            if (score >= 1000){
                score -= 1000;
                Audio.playSound("coin");
                GameObject turret = new Turret();
                turret.x = Renderer.getMouseWorldX();
                turret.y = Renderer.getMouseWorldY();
                Game.getGameInstance().addObject(turret);
            }
        }

        vy = Math.min(speed,Math.max(vy,-speed));
        vx = Math.min(speed,Math.max(vx,-speed));

        //Point at the mouse
        float mx = Renderer.getMouseWorldX();
        float my = Renderer.getMouseWorldY();
        float centerx = x;
        float centery = y;
        angle = 90-(float)Math.toDegrees(Math.atan2(mx-centerx,my-centery));

        super.simulate();
        vy *= decay;
        vx *= decay;


        //Handle shooting
        if (weapon == 0) {
            shootCooldownCounter++;
            if (shootCooldownCounter > shootCooldownNumFrames & Input.getMouseClicked() == 1 & ammo > 0) {
                ammo--;
                shootCooldownCounter = 0;
                Game.getGameInstance().screenShakeIntensity = 0.5f;
                Game.getGameInstance().addObject(new RifleBullet(x, y, angle));
                //Game.addObject(new RifleBullet(x, y, angle + 5));
                //Game.addObject(new RifleBullet(x, y, angle - 5));
                Audio.playSound("bullet1");
            }
            if (Input.getMouseClicked() == 0){
                //shootCooldownCounter
            }
        } else if (weapon == 1){
            shootCooldownCounter++;
            if (shootCooldownCounter > 2 & Input.getMouseClicked() == 1) {
                shootCooldownCounter = 0;
                for (int i = 0; i < 6; i++) {
                    Game.getGameInstance().addObject(new FlamethrowerBullet(x, y, angle + ((float) Math.random() * 10.0f - 5.0f)));
                }
            }
        }
    }

    public void render(){
        Renderer.draw(playerTex.getRegion(frame), x - (width / 2), y - (height / 2), width, height, angle);

    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////