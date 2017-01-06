////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: IOS
////////////////////////////////////////////////

package Engine.System.Platforms.IOS;

import Engine.System.Main;
import com.badlogic.gdx.ApplicationAdapter;

public class IOS extends ApplicationAdapter {

    @Override
    public void create () {
        Main.init();
    }

    @Override
    public void render () {
        Main.render();
    }

    @Override
    public void dispose() {
        Main.dispose();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////