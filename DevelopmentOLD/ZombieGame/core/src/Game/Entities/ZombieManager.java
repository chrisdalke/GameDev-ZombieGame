////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: ZombieManager
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.GameObject;
import Engine.Renderer.Renderer;
import Game.Game;

import java.util.concurrent.ThreadLocalRandom;

public class ZombieManager extends GameObject {

    private int zombieSpawnCounter;
    private int zombieSpawnRate = 100;

    private int numGroupsToSpawn = 1;
    private int groupSizeOffset = 0;
    public void setNumGroupsToSpawn(int numGroups){
        numGroupsToSpawn = numGroups;
    }
    public int getNumGroupsToSpawn(){
        return numGroupsToSpawn;
    }

    public ZombieManager() {
        super(null);
    }

    public void setGroupSizeOffset(int sizeOffset){
        groupSizeOffset = sizeOffset;
    }

    @Override
    public void simulate() {

        //Maintain a counter, and every
        zombieSpawnCounter++;

        if (zombieSpawnCounter > zombieSpawnRate){
            zombieSpawnCounter = 0;

            if (numGroupsToSpawn > 0) {
                numGroupsToSpawn--;

                //choose a random "pack size"
                int packSize = ThreadLocalRandom.current().nextInt(1, 5) + groupSizeOffset; //1 to 4
                for (int i = 0; i < packSize; i++) {
                    GameObject zombie = new Zombie();
                    //Position zombie on edge of screen, then add
                    int pos = ThreadLocalRandom.current().nextInt(1, 5); //1 to 4

                    switch (pos) {
                        case 1:
                            //top of screen
                            zombie.y = Game.camX + Renderer.viewHeight / 2;
                            zombie.x = Game.camX + (float) Math.random() * Renderer.viewWidth - Renderer.viewWidth / 2;
                            break;
                        case 2:
                            //bottom of screen
                            zombie.y = Game.camX + -Renderer.viewHeight / 2;
                            zombie.x = Game.camX + (float) Math.random() * Renderer.viewWidth - Renderer.viewWidth / 2;
                            break;
                        case 3:
                            //left
                            zombie.x = Game.camX + -Renderer.viewWidth / 2;
                            zombie.y = Game.camX + (float) Math.random() * Renderer.viewHeight - Renderer.viewHeight / 2;
                            break;
                        case 4:
                            //right
                            zombie.x = Game.camX + Renderer.viewWidth / 2;
                            zombie.y = Game.camX + (float) Math.random() * Renderer.viewHeight - Renderer.viewHeight / 2;
                            break;
                    }
                    Game.addObject(zombie);

                }
            }
        }


    }

    @Override
    public void render() {

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////