////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: GameInstance
////////////////////////////////////////////////

package Game;

import Engine.Audio.Audio;
import Engine.Display.Display;
import Engine.Display.Recording.Recording;
import Engine.Game.GameObject;
import Engine.Input.Input;
import Engine.Physics.CollisionManager;
import Engine.Physics.SimpleCollisionManager;
import Engine.Renderer.Renderer;
import Engine.Renderer.Shapes;
import Engine.Renderer.Text;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Config.Configuration;
import Engine.System.Timer.DeltaTimeManager;
import Engine.System.Timer.FPSTimer;
import Game.Entities.*;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static Engine.Display.Display.hideCursor;

public class GameInstance {
   
   Texture floorTex;
   Texture shadowTex;
   Texture crosshairTex;
   Texture vignetteTex;
   
   Texture uiAmmoTex;
   
   public float screenShakeIntensity;
   
   public float screenDamageIntensity;
   
   public AbstractPlayer player;
   public CollisionManager collisionManager;
   public ZombieManager waveManager;
   
   public ArrayList<GameObject> gameObjects;
   public ArrayList<GameObject> newObjects;
   public DeltaTimeManager deltaTimeManager;
   
   int wave;
   int delayToNextWave = 10;
   long waveEndTime;
   long waveNextCounter;
   boolean nextWaveTrigger;
   boolean waveIsDone;
   
   public int worldSizeX;
   public int worldSizeY;
   
   public float camX;
   public float camY;
   
   public void addObject(GameObject obj) {
      newObjects.add(obj);
   }
   
   public void reset() {
      dispose();
      init();
   }
   
   public void init() {
      Audio.stopAll();
      
      collisionManager = new SimpleCollisionManager();
      
      gameObjects = new ArrayList<GameObject>();
      newObjects = new ArrayList<GameObject>();
      
      worldSizeX = 200;
      worldSizeY = 200;
      //Add tile entities for the floor of the world... each tile is 10 wide
      for (int x = -worldSizeX; x < worldSizeX; x += 20) {
         for (int y = -worldSizeY; y < worldSizeY; y += 20) {
            GameObject tile = new WorldTile(x, y, "Assets/Textures/Sand.png");
            addObject(tile);
         }
      }
      
      //Add tile entities for the random props in the world
      for (int i = 0; i < worldSizeX / 4; i++) {
         int x = ThreadLocalRandom.current().nextInt(-worldSizeX, worldSizeX);
         int y = ThreadLocalRandom.current().nextInt(-worldSizeY, worldSizeY);
         GameObject tile = new Crate(x, y);
         tile.x = x;
         tile.y = y;
         tile.angle = (float) Math.random() * 360;
         addObject(tile);
      }
      
      Player playerObj = new Player();
      TankPlayer tankObj = new TankPlayer();
      tankObj.x = 30;
      player = playerObj;
      gameObjects.add(playerObj);
      gameObjects.add(tankObj);
      
      gameObjects.add(new Turret());
      
      wave = 1;
      waveManager = new ZombieManager();
      gameObjects.add(waveManager);
      waveManager.setNumGroupsToSpawn(1);
      
      //gameObjects.add(new EdgeSmoke());
      floorTex = TextureLoader.load("Assets/Textures/Sand.png");
      shadowTex = TextureLoader.load("Assets/Textures/shadow.png");
      crosshairTex = TextureLoader.load("Assets/Textures/crosshair.png");
      vignetteTex = TextureLoader.load("Assets/Textures/vignette.png");
      uiAmmoTex = TextureLoader.load("Assets/Textures/ui/BlockUI/EnergyBar.png");
      //Renderer.setCameraPos(90,50);

        /*
        Game.BloodSplatter testBloodSplatter = new Game.BloodSplatter();
        testBloodSplatter.x = 5;
        testBloodSplatter.y = 5;
        gameObjects.add(testBloodSplatter);
        */
      
      
      Renderer.camera.zoom = 1.0f;
      
      Display.hideCursor();
   }
   
   public void update() {
      //HERE: Game update code
      //Check simulation loop and use up the built up delta time
   
   
      //If we are recording, we will simply render each delta tick
   
      
   
      Renderer.calculateMouseCoords();
   
      //Do screen shake
      //camX = Game.player.x;
      //camY = Game.player.y;
      //Renderer.setCameraPos(camX+ (float)(((Math.random()*100)-50)/50.0f)*screenShakeIntensity,camY + (float)(((Math.random()*100)-50)/50.0f)*screenShakeIntensity);
      screenShakeIntensity *= 0.6f;
   
      Iterator<GameObject> iterator = gameObjects.iterator();
      while (iterator.hasNext()) {
         GameObject currentObject = iterator.next();
         if (currentObject.deleteFlag) {
            iterator.remove();
         }
      }
   
      collisionManager.start();
      for (GameObject currentObject : gameObjects) {
         currentObject.simulate();
      }
      //Apply collisions
      collisionManager.finish();
   
      //Add any of the new objects to the list
      gameObjects.addAll(newObjects);
      newObjects.clear();
   
   
      //Do wave timer
      if ((waveManager.getNumGroupsToSpawn() == 0) && (getNumZombies() == 0)) {
         //Wave is over
         long timeMillis = System.currentTimeMillis();
         long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
      
         if (nextWaveTrigger == false) {
            nextWaveTrigger = true;
            waveEndTime = timeSeconds;
            waveIsDone = true;
         
         
            //Randomly generate between 0 and 4 crates
            if (ThreadLocalRandom.current().nextInt(1, 4) == 1) {
               for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 4); i++) {
               
                  int x = ThreadLocalRandom.current().nextInt((int) -Renderer.viewWidth, (int) Renderer.viewWidth);
                  int y = ThreadLocalRandom.current().nextInt((int) -Renderer.viewHeight, (int) Renderer.viewHeight);
                  GameObject tile = new Crate(x, y);
                  tile.x = x;
                  tile.y = y;
                  tile.angle = (float) Math.random() * 360;
                  addObject(tile);
               }
            }
         }
      
         waveNextCounter = delayToNextWave - (timeSeconds - waveEndTime);
         if (waveNextCounter <= 0) {
            wave++;
            waveManager.setNumGroupsToSpawn(wave);
            waveManager.setGroupSizeOffset(wave);
            waveIsDone = false;
         }
      
      } else {
         //Wave is not over
         nextWaveTrigger = false;
         waveIsDone = false;
      }
      if (player.damageFrame == 1)

      {
         player.damageFrame = 0;
         screenDamageIntensity = 100;
         addObject(new BloodSplatterParticle(player.x, player.y));
      }
   
      screenDamageIntensity--;
      if (screenDamageIntensity < 0)

      {
         screenDamageIntensity = 0;
      }
   
   }
   
   
   public void render(){
      renderWorld();
      renderUI();
   }
   
   public void renderWorld(){
      Renderer.startWorld();
      Renderer.resetColor();

        /*
        //Draw the game floor
        //Repeat this over the entire scene
        int size = 16;
        for (int x = -(int)Renderer.viewWidth/2 -1; x < (int)Renderer.viewWidth/2 +1; x += size){
            for (int y = -(int)Renderer.viewHeight/2 -1; y < (int)Renderer.viewHeight/2 +1; y+= size){
                Renderer.draw(floorTex.getRegion(),x,y,size,size);
            }
        }
*/
      
      //Sort by the 'layer' property
      Collections.sort(gameObjects, new Comparator<GameObject>() {
         @Override
         public int compare(GameObject o1, GameObject o2) {
            return Integer.compare(o1.layer, o2.layer);
         }
      });
      
      
      //Draw objects that are below shadow layer
      for (GameObject currentObject : gameObjects){
         if (currentObject.layer <= -10) {
            currentObject.render();
         }
      }
      
      //Renderer.setSubtractive();
      //Draw shadows for all objects before drawing the objects
      //Shadows need to render after the floor objects which are on layer -10 and below
      float shadowSize = 0.75f;
      for (GameObject currentObject : gameObjects){
         if (!currentObject.noShadow) {
            Renderer.draw(shadowTex.getRegion(), currentObject.x - (currentObject.width * shadowSize) / 2, currentObject.y - (currentObject.height * shadowSize) / 2, currentObject.width * shadowSize, currentObject.height * shadowSize,currentObject.angle);
         }
      }
      //Renderer.resetBlending();
      
      
      for (GameObject currentObject : gameObjects){
         if (currentObject.layer > -10) {
            currentObject.render();
         }
      }
      
      
      Renderer.endWorld();
      
      //Do any shape renderer stuff here
      
      Shapes.beginOutline(Renderer.camera);
      //collisionManager.drawDebug();
      Shapes.end();
   }
   
   public void renderUI() {
      
      
      Text.setFont(2);
      
      String waveText = "";
      if (waveIsDone) {
         waveText = "Next Wave in " + waveNextCounter + "...";
      } else {
         waveText = "Wave " + wave;
      }
      
      //Do world UI
      Shapes.begin(Renderer.cameraUI);
      
      
      //Handle game over
      if (!gameObjects.contains(player)){
         Shapes.setColor(0,0,0,200);
         Shapes.drawBox(0, (float)Display.getHeight()/2 - 50,(float)Display.getWidth(),100);
      } else {
         Shapes.setColor(0, 0, 0, 160);
         Shapes.drawBox(0, (float) Display.getHeight() - 40, Text.getTextWidth(waveText) + 40, 40);
         
         Shapes.drawBox((float) Display.getWidth() - 400, 0, 400, 160);
         
         Shapes.drawBox((float) Display.getWidth() - 290, 10, 280, 40);
         Shapes.drawBox((float) Display.getWidth() - 290, 10 + 50, 280, 40);
         Shapes.drawBox((float) Display.getWidth() - 290, 10 + 50 + 50, 280, 40);
         
         Shapes.setColor(255, 200, 100, 255);
         Shapes.drawBox((float) Display.getWidth() - 290, 10 + 50, 280 * (player.ammo / 100.0f), 40);
         Shapes.setColor(100, 255, 100, 255);
         Shapes.drawBox((float) Display.getWidth() - 290, 10, 280 * (player.health / 100.0f), 40);
      }
      
      Shapes.end();
      
      Renderer.startUI();
      
      
      if (!gameObjects.contains(player)){
         Text.setFont(3);
         Text.centerDraw((float)Display.getWidth()/2,(float)Display.getHeight()/2,"Game Over");
         Text.setFont(1);
      } else {
         if (waveIsDone) {
            Text.draw(10, (float) Display.getHeight() - 10, waveText);
         } else {
            String text = "Wave " + wave;
            Text.draw(10, (float) Display.getHeight() - 10, waveText);
         }
         
         Text.setFont(2);
         
         int yy = 20;
         
         
         if (player.health > 15) {
            Text.setColor(Color.BLACK);
            Text.draw((float) Display.getWidth() - 280 + 280 * (player.health / 100.0f) - (Text.getTextWidth("" + player.health) + 20), 10 + 50 - 20, "" + player.health);
         } else {
            Text.setColor(Color.WHITE);
            Text.draw((float) Display.getWidth() - 280 + 280 * (player.health / 100.0f) + (Text.getTextWidth("" + player.health) - 10), 10 + 50 - 20, "" + player.health);
         }
         if (player.ammo > 15) {
            Text.setColor(Color.BLACK);
            Text.draw((float) Display.getWidth() - 280 + 280 * (player.ammo / 100.0f) - (Text.getTextWidth("" + player.ammo) + 20), 10 + 50 + 50 - 20, "" + player.ammo);
         } else {
            Text.setColor(Color.WHITE);
            Text.draw((float) Display.getWidth() - 280 + 280 * (player.ammo / 100.0f) + (Text.getTextWidth("" + player.ammo) - 10), 10 + 50 + 50 - 20, "" + player.ammo);
         }
         
         Text.setColor(Color.WHITE);
         Text.draw((float) Display.getWidth() - 380, 10 + 50 - 20, "Health");
         Text.draw((float) Display.getWidth() - 370, 10 + 50 + 50 - 20, "Ammo");
         Text.draw((float) Display.getWidth() - 370, 10 + 50 + 50 + 50 - 20, "Cash");
         
         Text.draw((float) Display.getWidth() - 280, 10 + 50 + 50 + 50 - 20, "$" + player.score);
      }
      Renderer.endUI();
      
      //Do Menu UI
      
      //Render UI stuff last!!
      Renderer.startUI();

        /*
        if (screenDamageIntensity > 0) {
            Renderer.setAdditive();
            Renderer.setColor(new Color(255, 0, 0, (screenDamageIntensity/255.0f)));
            Renderer.draw(vignetteTex.getRegion(), 0, 0, (float) Display.getWidth(), (float) Display.getHeight());
            Renderer.resetColor();
            Renderer.resetBlending();
        }
        */
      
      
      Renderer.setAdditive();
      Renderer.setColor(new Color(255*Math.min(screenShakeIntensity,1.0f),255*Math.min(screenShakeIntensity,1.0f),255*Math.min(screenShakeIntensity,1.0f),1.0f));
      //Renderer.draw(vignetteTex.getRegion(),0,0,(float)Display.getWidth(),(float)Display.getHeight());
      Renderer.resetBlending();
      Renderer.resetColor();
      
      
         
         //Render debug information
         renderDebug();
         
         float crosshairSize = 20;
         Renderer.setInvert();
         Renderer.draw(crosshairTex.getRegion(),Input.getMouseX() - crosshairSize/2,(float)Display.getHeight() - Input.getMouseY() - crosshairSize/2,crosshairSize,crosshairSize);
         Renderer.resetBlending();
        
      
      Renderer.endUI();
      
   }
   
   public void renderDebug(){
      //Render debug stuff
      int y = (int) Display.getHeight();
      Renderer.setColor(Color.BLACK);
      Text.draw(0, y, "Zombie Game"); y=y-20;
      Text.draw(0, y, "Current Frame: "+ Display.frameNumber); y=y-20;
      Text.draw(0, y, "Frame Time: "+ FPSTimer.getFrameTime()); y=y-20;
      Text.draw(0, y, "FPS: "+ FPSTimer.getFPS()); y=y-20;
      Text.draw(0, y, "Tick Length: "+ Game.deltaTimeManager.getLastInterval()); y=y-20;
      Text.draw(0, y, "Entities: "+gameObjects.size()); y=y-20;
      Text.draw(0, y, "X Pad: "+Input.directionalHoriz); y=y-20;
      Text.draw(0, y, "Y Pad: "+Input.directionalVert); y=y-20;
      int n = 0;

        /*
        for (GameObject currentObject : gameObjects){
            Text.draw(0, y, currentObject.getClass().getSimpleName()+") "+"x: "+currentObject.x+"y: "+currentObject.y+"w: "+currentObject.width+"h:"+currentObject.height); y=y-20;
        }
        */
      
      Renderer.resetColor();
      Renderer.displayDebug();
   }
   
   public int getNumZombies(){
      int numZombies = 0;
      for (GameObject obj : gameObjects){
         if (obj instanceof Zombie){
            numZombies++;
         }
      }
      return numZombies;
   }
   
   public void dispose(){
      
      gameObjects = null;
      newObjects = null;
      deltaTimeManager = null;
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////