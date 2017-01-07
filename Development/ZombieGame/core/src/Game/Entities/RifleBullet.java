////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: RifleBullet
////////////////////////////////////////////////

package Game.Entities;

import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

public class RifleBullet extends Bullet {

    public RifleBullet(float x, float y, float angle) {
        super(TextureLoader.load("Assets/Textures/flash01.png", 1,1));
        width = 10f;
        height = 4f;
        this.x = x;
        this.y = y;
        this.angle = angle;
        noShadow = true;
        layer = 4;
        Game.getGameInstance().collisionManager.addObject(this);
        Game.getGameInstance().collisionManager.setKinematic(this);
    }

    public void simulate(){
        super.simulate();

    }

    public void render(){
        Renderer.setAdditive();
        super.render();
        Renderer.resetBlending();

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////