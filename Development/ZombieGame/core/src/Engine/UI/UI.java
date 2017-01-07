////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: UI
////////////////////////////////////////////////

package Engine.UI;

import Engine.System.Config.Configuration;

public class UI {
    public static void init(Configuration config){

    }

    public static void dispose(){

    }

    //Static functions for some important UI elements

    //Creates a modal dialog asking the user if they want to quit the game
    public static void createQuitMenu() {
        /*
        UIModalWindow.createYesNoDialog(UIStageManager.getCurrentStage(), "Quit Orbital Space", "Are you sure you would like to quit?", new Runnable() {
            @Override
            public void run() {
                Game.Game.commandExecute("quit");
            }
        }, 1);
        */
    }

   // private static UIPauseWindow pauseOverlay;
/*
    public static void createPauseMenu() {
        if (pauseOverlay == null) {
            pauseOverlay = new UIPauseWindow();
            UIStageManager.getCurrentStage().addChild(pauseOverlay);
        }
    }

    public static boolean hasPauseMenu(){
        return pauseOverlay != null;
    }

    public static void closePauseMenu(){
        if (pauseOverlay != null) {
            pauseOverlay.getActor().remove();
            pauseOverlay = null;
        }
    }
    */
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////