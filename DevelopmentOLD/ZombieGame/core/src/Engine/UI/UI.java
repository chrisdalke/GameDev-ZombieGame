////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: UI
////////////////////////////////////////////////

package Engine.UI;

import Engine.System.Config.Configuration;

public class UI {

    private static UIState state;

    public static void init(Configuration config){

        state = UIState.UI_GAME;
    }



    public static void update(){

        switch (state){
            case UI_INTRO:{
                break;
            }
            case UI_MENU:{
                break;
            }
            case UI_LOAD:{
                break;
            }
            case UI_GAME:{
                break;
            }
        }
    }

    public static void dispose(){

    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////