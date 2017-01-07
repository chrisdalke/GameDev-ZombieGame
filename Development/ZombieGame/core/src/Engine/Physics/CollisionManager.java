package Engine.Physics;

import Engine.Game.GameObject;

import java.util.List;

public interface CollisionManager {

    void addObject(GameObject obj);
    void setKinematic(GameObject obj);
    void setStatic(GameObject obj);
    void removeObject(GameObject obj);
    List<GameObject> getColliders(GameObject obj);
    boolean collidesWithType(GameObject obj, Class type);
    boolean collidesWithAnything(GameObject obj);
    List<GameObject> getCollidersWithType(GameObject obj, Class type);
    void start();
    void finish();
    void drawDebug();
}

