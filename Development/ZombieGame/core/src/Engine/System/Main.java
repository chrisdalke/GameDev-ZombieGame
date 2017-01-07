////////////////////////////////////////////////
// ZombieGame
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Main
////////////////////////////////////////////////

package Engine.System;

import Engine.Audio.Audio;
import Engine.Display.Display;
import Engine.Input.Input;
import Engine.Renderer.Renderer;
import Engine.System.Config.ConfigManager;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import Engine.System.Platforms.PlatformManager;
import Engine.System.Timer.FPSTimer;
import Engine.UI.UI;
import Game.Game;

public class Main {

    ////////////////////////////////////////////////
    // Initialization
    ////////////////////////////////////////////////

    public static void init() {
        ConfigManager configManager = new ConfigManager();
        Configuration config = configManager.getConfig();
        
        if (PlatformManager.getPlatform() == PlatformManager.IOS){
            //Scale to native resolution
            config.fullscreen = true;
            config.fullscreen_window = true;
        } else {
            //Obey file config
            config.width = 800;
            config.height = 600;
            configManager.save();
        }


        try {
            //Init all of our game's systems
            System.init(config);
            Display.init(config);
            Input.init(config);
            Renderer.init(config);
            Audio.init(config);
            Game.init(config);
            UI.init(config);

        } catch (Exception e){
            Logger.logError("Error while Initializing");
            e.printStackTrace();
        }
        Logger.log("Init completed.");

    }

    ////////////////////////////////////////////////
    // Update
    ////////////////////////////////////////////////

    public static void render(){
        FPSTimer.updateFrameTime();

        //update the input manager
        System.update();
        Input.update();

        Game.update(); //Handle game logic update

        Display.startRender();
        Game.render(); //Handle game rendering
        Display.finishRender();

    }


    ////////////////////////////////////////////////
    // Terminate
    ////////////////////////////////////////////////

    public static void dispose(){

        //Destroy all the game resources before we close the game
        Logger.logError("Disposing...");

        Game.dispose();
        Audio.dispose();
        Renderer.dispose();
        Input.dispose();
        UI.dispose();
        Display.dispose();
        System.dispose();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////