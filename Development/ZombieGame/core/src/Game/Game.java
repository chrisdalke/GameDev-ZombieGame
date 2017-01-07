////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Game
////////////////////////////////////////////////

package Game;

import Engine.Audio.Audio;
import Engine.Input.Input;
import Engine.Renderer.Renderer;
import Engine.System.Config.Configuration;
import Engine.System.Timer.DeltaTimeManager;
import Engine.System.Utility.MethodInvoker;
import Engine.UI.Stages.Types.*;
import Engine.UI.Stages.UIStageManager;

import java.util.ArrayList;

public class Game {
    
    private static GameInstance gameInstance;
    private static MenuInstance menuInstance;
    private static boolean isPaused;
    public static DeltaTimeManager deltaTimeManager;
    private static final double TICK_RATE = 1000.0/60.0;
    static ArrayList<MethodInvoker> methodInvokers;
    
    public static void init(Configuration config){
        deltaTimeManager = new DeltaTimeManager(TICK_RATE);
        deltaTimeManager.startInterval();
        methodInvokers = new ArrayList<MethodInvoker>();
        
        //Set up the user interface stages
        UIStageManager.init();
        UIStageManager.addStage("menuStage", new MenuStage());
        UIStageManager.addStage("gameStage", new GameStage());
        UIStageManager.switchTo("gameStage");
        
        gameInstance = new GameInstance();
        gameInstance.init();
        
        
    }
    
    public static void update(){
        //Update delta timer
        deltaTimeManager.endInterval();
        deltaTimeManager.startInterval();
        
        //Update subsystems
        Audio.update();
        
        while (deltaTimeManager.consumeTick()) {
        
            //Update the command queue
            for (MethodInvoker i : methodInvokers){
                i.run();
            }
            methodInvokers.clear();
        
            if (gameInstance != null) {
                if (!isPaused) {
                    //Update the instance
                    gameInstance.update();
                
                    //Pause check
                    if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.ESCAPE)){
                        Game.setPaused(true);
                    }
                } else {
                    //Handle pause status
                    //unpause check
                    if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.ESCAPE)){
                        Game.setPaused(false);
                    }
                }
            } else {
                //Update the main menu game instance
                menuInstance.update();
            }
        }
    
    }
    
    public static void render(){
    
        //Render game stuff here
        if (gameInstance != null){
            //Render the game's view
            gameInstance.render();
        } else {
            //Render the menu instance's view
            menuInstance.render();
        }
    
        //Render Scene2d UI layers
        //This is all above the in-game UI layer
        Renderer.startUI();
        UIStageManager.render();
        Renderer.endUI();
    }
    
    public static GameInstance getGameInstance(){
        return gameInstance;
    }
    
    public static void queueMethodInvoker(MethodInvoker item){
        methodInvokers.add(item);
    }
    
    public static void setPaused(boolean isPaused){
        Game.isPaused = isPaused;
    }
    
    public static boolean getPaused(){
        return Game.isPaused;
    }
    
    public static void dispose(){
        //Dispose of any extra game resources that we won't need.
        UIStageManager.dispose();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////