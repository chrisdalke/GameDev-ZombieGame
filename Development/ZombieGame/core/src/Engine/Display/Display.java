////////////////////////////////////////////////
// ZombieGame
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Display
////////////////////////////////////////////////

package Engine.Display;

import Engine.Display.Recording.Recording;
import Engine.Input.Input;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;

public class Display {

    private static Configuration config;
    public static long frameNumber;

    public static Configuration getConfig(){
        return config;
    }

    public static double getHeight(){return config.height;}
    public static double getWidth(){return config.width;}

    public static void init(Configuration config){
        Display.config = config;
        config.fullscreen = false;

        if (config.fullscreen){
            Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
            //if (config.fullscreen_window) {
                config.width = mode.width;
                config.height = mode.height;
            //} else {
            //    mode.width = (int)config.width;
            //    mode.height = (int)config.height;
            //
            //    mode = new LwjglApplicationConfigurationDisplayMode(mode);
            //}

            Gdx.graphics.setFullscreenMode(mode);

        } else {
            Gdx.graphics.setWindowedMode((int) config.width, (int) config.height);
        }
        config.width = Gdx.graphics.getWidth();
        config.height = Gdx.graphics.getHeight();

        Gdx.graphics.setVSync(config.vsync);
        Gdx.graphics.setTitle("ZombieGame");

        frameNumber = 0;
    }

    public static void startRender(){
        //Clear the background
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    public static void finishRender(){

        //take a screenshot?
        if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.P)){
            Screenshot.saveScreenshot();
            Logger.log("Took a screenshot!");
        }

        //Handle video capture
        if (Recording.isRecording()){
            Recording.recordFrame();
        }

        if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.GRAVE)){
            if (Recording.isRecording()){
                Recording.stopRecording();
            } else {
                Recording.startRecording();
            }
        }

        frameNumber++;
    }

    public static void dispose(){

    }

    public static void hideCursor(){
        Pixmap pm = new Pixmap(Gdx.files.internal("Assets/Textures/pixel_blank.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();
    }

    public static void showCursor(){
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////