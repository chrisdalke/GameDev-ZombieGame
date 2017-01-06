////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: DebugTile
////////////////////////////////////////////////

package Game.Entities;
import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class EdgeSmoke extends GameObject {

    ParticleEffect effectX1;
    ParticleEffect effectX2;
    ParticleEffect effectY1;
    ParticleEffect effectY2;

    public EdgeSmoke() {
        super(null);

        //Set up particle system and load
        TextureAtlas particleAtlas;
        effectX1 = new ParticleEffect();
        effectX1.load(Gdx.files.internal("Resources/Particles/smoke_edge_y.p"),Gdx.files.internal("Resources/Textures"));
        effectX1.start();
        effectX2 = new ParticleEffect();
        effectX2.load(Gdx.files.internal("Resources/Particles/smoke_edge_y.p"),Gdx.files.internal("Resources/Textures"));
        effectX2.start();
        effectY1 = new ParticleEffect();
        effectY1.load(Gdx.files.internal("Resources/Particles/smoke_edge_x.p"),Gdx.files.internal("Resources/Textures"));
        effectY1.start();
        effectY2 = new ParticleEffect();
        effectY2.load(Gdx.files.internal("Resources/Particles/smoke_edge_x.p"),Gdx.files.internal("Resources/Textures"));
        effectY2.start();

        layer = 2;
    }

    @Override
    public void simulate() {
        super.simulate();
        effectX1.setPosition(-Renderer.viewWidth/2,-Renderer.viewHeight/2);
        effectX2.setPosition(Renderer.viewWidth/2,-Renderer.viewHeight/2);
        effectY1.setPosition(-Renderer.viewWidth/2,-Renderer.viewHeight/2);
        effectY2.setPosition(-Renderer.viewWidth/2,Renderer.viewHeight/2);
        effectX1.update(0.01f);
        effectX2.update(0.01f);
        effectY1.update(0.01f);
        effectY2.update(0.01f);

    }

    @Override
    public void render() {
        effectX1.draw(Renderer.getBatch());
        effectX2.draw(Renderer.getBatch());
        effectY1.draw(Renderer.getBatch());
        effectY2.draw(Renderer.getBatch());
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////