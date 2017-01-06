////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: GameObject
////////////////////////////////////////////////

package Engine.Game;


import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.Texture;

public abstract class GameObject {
    public float x;
    public float y;
    public float width;
    public float height;
    public float angle;
    public Texture tex;
    public int frame;
    public float vx;
    public float vy;
    public boolean deleteFlag;
    public boolean noShadow;
    public int layer;
    public float radius;

    public GameObject(Texture tex) {
        this.tex = tex;

    }

    public void kill(){
        deleteFlag = true;
    }

    public void simulate(){
        x += vx;
        y += vy;
    }

    public void render(){
        if (tex != null) {
            Renderer.draw(tex.getRegion(frame), x - (width / 2), y - (height / 2), width, height, angle);
        }
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////