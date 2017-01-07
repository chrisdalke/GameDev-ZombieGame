////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: WorldTile
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Textures.TextureLoader;

public class WorldTile extends GameObject {
    public WorldTile(float x, float y,String tex) {
        super(TextureLoader.load(tex,1,1));
        width = 20f;
        height = 20f;
        noShadow = true;
        layer = -10;
        this.x = x;
        this.y = y;
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