////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Sandbags
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Textures.TextureLoader;
import Game.Game;

public class Sandbags extends GameObject {
    public Sandbags() {
        super(TextureLoader.load("Resources/Textures/sandbags.png",1,1));
        width = 30f;
        height = 15f;
        noShadow = true;
        layer = 1;
        radius = 15f;
        Game.collisionManager.addObject(this);
        Game.collisionManager.setStatic(this);
    }

    @Override
    public void simulate() {
        super.simulate();
    }

    @Override
    public void render() {
        super.render();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////