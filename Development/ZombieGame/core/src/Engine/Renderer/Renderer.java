////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: Renderer
////////////////////////////////////////////////

package Engine.Renderer;

import Engine.Display.Display;
import Engine.Input.Input;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Renderer {

    //Helper class for the Game to render stuff
    private static SpriteBatch batch;
    private static StretchViewport viewport;
    public static OrthographicCamera camera;
    public static OrthographicCamera cameraUI;
    private static Stage stage;
    public static float viewWidth;
    public static float viewHeight;

    public static SpriteBatch getBatch(){
        return batch;
    }

    public static void init(Configuration config){


        batch = new SpriteBatch();


        float ratio = (float)Display.getHeight() / (float)Display.getWidth();
        viewWidth = 150;
        viewHeight = viewWidth*ratio;
        camera = new OrthographicCamera(viewWidth,viewHeight);

        cameraUI = new OrthographicCamera((float)Display.getWidth(),(float)Display.getHeight());
        cameraUI.translate((float)Display.getWidth()/2,(float)Display.getHeight()/2);
        Text.init();
        Shapes.init();


        Logger.log("Renderer successfully initialized!");
    }

    public static void displayDebug(){

    }

    public static void startWorld(){
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

    }

    public static void endWorld(){
        batch.end();
    }

    public static void startUI(){
        cameraUI.update();
        batch.setProjectionMatrix(cameraUI.combined);
        batch.begin();

    }

    public static void setClipping(int x, int y, int width, int height){
        Rectangle scissor = new Rectangle();
        Rectangle clipBounds = new Rectangle(x,y,width,height);
        ScissorStack.calculateScissors(cameraUI, 0, 0, (float)Display.getWidth(), (float)Display.getHeight(), batch.getTransformMatrix(), clipBounds, scissor);
        ScissorStack.pushScissors(scissor);
        batch.end();
    }

    public static void resetClipping(){
        ScissorStack.popScissors();

    }

    public static void endUI(){
        batch.end();
    }


    public static void setColor(Color tint){
        batch.setColor(tint);
    }
    public static void resetColor(){
        batch.end();
        batch.begin();
        batch.setColor(Color.WHITE);
    }

    static int srcFunc;
    static int dstFunc;

    public static void setAdditive(){
        batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE);
    }

    public static void setSubtractive(){

        batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ONE);
    }

    public static void setInvert(){
        batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR,GL20.GL_ONE_MINUS_SRC_COLOR);
    }


    public static void resetBlending(){
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }



    public static void draw(TextureRegion tex, float x, float y, float width, float height, float angle){
        batch.draw(tex,x,y,width/2,height/2,width,height,1.0f,1.0f,angle);
    }

    public static void draw(TextureRegion tex, float x, float y, float width, float height){
        batch.draw(tex,x,y,width,height);
    }

    public static void setCameraPos(float x, float y){
        camera.position.x = x;
        camera.position.y = y;
    }


    public static float getCameraX(){
        return camera.position.x;
    }

    public static float getCameraY(){
        return camera.position.y;
    }

    //Mouse picking code
    private static float mouseWorldX;
    private static float mouseWorldY;

    public static void calculateMouseCoords(){
        if (Input.getMouseX() < Display.getWidth() & Input.getMouseY() < Display.getHeight() & Input.getMouseX() > 0 & Input.getMouseY() > 0) {
            Vector3 result = camera.unproject(new Vector3(Input.getMouseX(), Input.getMouseY(), 0));
            mouseWorldX = result.x;
            mouseWorldY = result.y;
        }
    }


    public static float getMouseWorldX(){
        return mouseWorldX;
    }

    public static float getMouseWorldY(){
        return mouseWorldY;
    }


    public static void dispose(){
        batch.dispose();
        Text.dispose();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////