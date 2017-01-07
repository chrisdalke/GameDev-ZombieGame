////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Player
////////////////////////////////////////////////

package Game.Entities;

import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

public class FlamethrowerBullet extends Bullet {


    public FlamethrowerBullet(float x, float y, float angle) {
        super(TextureLoader.load("Assets/Textures/fire4.png", 1,1));
        speed = 0.6f + (float)Math.random()*0.3f;
        damage = 1;
        knockback = 0.0f;
        ageLimit = 40;

        width = 0.1f;
        height = 0.1f;
        layer = 4;
        this.x = x;
        this.y = y;
        this.angle = angle;
        noShadow = true;
        Game.getGameInstance().collisionManager.addObject(this);
        Game.getGameInstance().collisionManager.setKinematic(this);
    }

    public void simulate(){
        vx = (float)Math.cos(Math.toRadians(angle))*speed;
        vy = (float)Math.sin(Math.toRadians(angle))*speed;

        super.simulate();

        width += 0.2f;
        height += 0.2f;


    }

    public void render(){
        //Renderer.setColor(new Color(244, 158, 66,1.0f));
        Renderer.setAdditive();
        super.render();
        Renderer.resetBlending();
        //Renderer.resetColor();

    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////