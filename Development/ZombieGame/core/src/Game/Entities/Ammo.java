////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Player
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

public class Ammo extends GameObject {

    private float speed = 2.5f;
    public int damage = 5;

    public Ammo(float x, float y) {
        super(TextureLoader.load("Assets/Textures/ammo.png", 1,1));
        float randSize = (float)Math.random()*0.2f;
        width = 2f + randSize;
        height = 2f + randSize;
        this.x = x;
        this.y = y;
        noShadow = false;
        angle = (float)Math.random()*360;
        //Make the Ammo go in a random direction
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

    }

    public void render(){
        super.render();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////