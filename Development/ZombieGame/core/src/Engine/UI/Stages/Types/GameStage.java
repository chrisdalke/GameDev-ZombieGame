////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: GameStage
////////////////////////////////////////////////

package Engine.UI.Stages.Types;

import Engine.Input.Input;
import Engine.UI.Stages.UIStage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class GameStage extends UIStage {
    
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public void init() {
        
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("Assets/Textures/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("Assets/Textures/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);
        
        //if (PlatformManager.getPlatform() == PlatformManager.IOS) {
            addActor(touchpad);
        //}
        
        
    }

    @Override
    public void update(){
        //Apply the block's delta to the directional input
    
        Input.directionalHoriz = touchpad.getKnobPercentX();
        Input.directionalVert = touchpad.getKnobPercentY();
    
    
    }

    @Override
    public void render(){
    
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////