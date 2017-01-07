////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Player
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;
import com.badlogic.gdx.graphics.Color;

public class XP extends GameObject {

    int lifetime = 1600;
    int lifetimeTimer = 0;
    private float speed = 2.5f;
    public int damage = 5;

    public XP(float x, float y) {
        super(TextureLoader.load("Assets/Textures/exp.png", 1,1));
        float randSize = (float)Math.random()*0.2f;
        width = 2f + randSize;
        height = 1f + randSize;
        this.x = x;
        this.y = y;
        noShadow = true;
        angle = (float)Math.random()*360;
        //Make the XP go in a random direction
        this.vx = (float)Math.random()*1.0f - 0.5f;
        this.vy = (float)Math.random()*1.0f - 0.5f;
        Game.getGameInstance().collisionManager.addObject(this);
        Game.getGameInstance().collisionManager.setKinematic(this);

    }

    public void simulate(){

        super.simulate();

        //Find distance to player
        //If distance is within range, exponentially move closer to player
        double plrDistance = Math.hypot(x-Game.getGameInstance().player.x,y-Game.getGameInstance().player.y);
        float range = 8;
        if (plrDistance < range){

            //Point at the center of player
            float mx = Game.getGameInstance().player.x;
            float my = Game.getGameInstance().player.y;
            float centerx = x;
            float centery = y;
            float fangle = 90-(float)Math.toDegrees(Math.atan2(mx-centerx,my-centery));

            //Set velocity based on our direction and distance
            float speed = (float)((range-plrDistance)/range)*2.0f+0.01f;
            vx = (float)Math.cos(Math.toRadians(fangle))*speed;
            vy = (float)Math.sin(Math.toRadians(fangle))*speed;
            angle+=10;
        } else {
            vx *= 0.9f;
            vy *= 0.9f;
        }


        lifetimeTimer++;
        if (lifetimeTimer > lifetime){
            kill();
        }

    }

    public void render(){
        Renderer.setColor(new Color(255,255,255,(float)(lifetime - lifetimeTimer)/(float)lifetime));
        super.render();
        Renderer.resetColor();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////