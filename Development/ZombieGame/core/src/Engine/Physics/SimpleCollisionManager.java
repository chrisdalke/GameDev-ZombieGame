////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: SimpleCollisionManager
////////////////////////////////////////////////

package Engine.Physics;

import Engine.Game.GameObject;
import Engine.Renderer.Shapes;
import Game.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;

import java.util.*;

//Simple collision manager with spherical collisions
public class SimpleCollisionManager implements CollisionManager {

    private ArrayList<GameObject> colliders;
    private ArrayList<GameObject> kinematics;
    private ArrayList<GameObject> statics;

    private HashMap<GameObject,PositionVelocityPair> states;

    public SimpleCollisionManager() {
        colliders = new ArrayList<GameObject>();
        kinematics = new ArrayList<GameObject>();
        statics = new ArrayList<GameObject>();
        states = new HashMap<GameObject, PositionVelocityPair>();
    }

    @Override
    public void setKinematic(GameObject obj) {
        kinematics.add(obj);
    }
    @Override
    public void setStatic(GameObject obj) {
        statics.add(obj);
    }

    @Override
    public void addObject(GameObject obj) {
        colliders.add(obj);
        //states.put(obj,new PositionVelocityPair(obj.x,obj.y,0,0));
    }

    @Override
    public void removeObject(GameObject obj) {
        colliders.remove(obj);
    }

    @Override
    public void start(){
        //Record positions for all the colliders
        for (GameObject collider : colliders){
            states.put(collider,new PositionVelocityPair(collider.x,collider.y,0,0));
        }
    }

    @Override
    public void finish() {

        //Purge all objects not in gameObjects or newObjects
        Iterator<GameObject> iterator = colliders.iterator();
        while (iterator.hasNext()) {
            GameObject currentObject = iterator.next();
            if (!Game.getGameInstance().gameObjects.contains(currentObject) && !Game.getGameInstance().gameObjects.contains(currentObject)) {
                iterator.remove();
            }
        }

        //Update all the collider positions and velocities
        for (GameObject collider : states.keySet()){
            PositionVelocityPair pv = states.get(collider);
            pv.dx = collider.x - pv.x;
            pv.dy = collider.y - pv.y;
        }

        //Using our list of colliders, update all of their positions
        //with simple sliding spherical collision.
        //Ignore kinematic objects
        //BAD ALGORITHM NEEDS FIX THIS IS O(N^2) AT LEAST

        int numPasses = 1;
        for (int pass = 0; pass < numPasses; pass++) {
            for (GameObject activeCollider : colliders) {
                if (!kinematics.contains(activeCollider)) {

                    Sphere collider1 = new Sphere(new Vector3(activeCollider.x, activeCollider.y, 0), activeCollider.radius);
                    for (GameObject passiveCollider : colliders) {
                        if (passiveCollider != activeCollider) {

                            if (!kinematics.contains(passiveCollider)) {
                                Sphere collider2 = new Sphere(new Vector3(passiveCollider.x, passiveCollider.y, 0), passiveCollider.radius);

                                if (collider1.overlaps(collider2)) {
                                    //Perform collision correction
                                    //Reverse the movement of both items
                                    PositionVelocityPair pv1 = states.get(activeCollider);
                                    PositionVelocityPair pv2 = states.get(passiveCollider);
                                    //If one of the pv pairs is null that means this object has just spawned
                                    //ignore that collision
                                    if (pv1 != null && pv2 != null) {

                                        //Resolve the collision
                                        Vector2 position1 = new Vector2(pv1.x, pv1.y);
                                        Vector2 position2 = new Vector2(pv2.x, pv2.y);
                                        Vector2 vel1 = new Vector2(activeCollider.vx, activeCollider.vy);
                                        Vector2 vel2 = new Vector2(passiveCollider.vx, passiveCollider.vy);

                                        // get the mtd
                                        Vector2 delta = (position1.sub(position2));
                                        float d = delta.len();
                                        // minimum translation distance to push balls apart after intersecting
                                        Vector2 mtd = delta.scl((activeCollider.radius + passiveCollider.radius - d) / d);

                                        //Check that both are not static
                                        if (!statics.contains(activeCollider) || !statics.contains(passiveCollider)) {
                                            // resolve intersection --
                                            // inverse mass quantities
                                            float im1 = 1;
                                            float im2 = 1;

                                            // push-pull them apart based off their mass
                                            position1 = position1.add(mtd.scl(im1 / (im1 + im2)));
                                            position2 = position2.sub(mtd.scl(im2 / (im1 + im2)));

                                            // impact speed
                                            Vector2 v = (vel1).sub(vel2);
                                            float vn = v.dot(mtd.nor());

                                            // sphere intersecting but moving away from each other already
                                            if (vn > 0.0f) return;

                                            float restitution = 1.0f;

                                            // collision impulse
                                            float i = (-(1.0f + restitution) * vn) / (im1 + im2);
                                            Vector2 impulse = mtd.scl(i);

                                            // change in momentum
                                            vel1 = vel1.add(impulse.scl(im1));
                                            vel2 = vel2.sub(impulse.scl(im2));

                                            if (!statics.contains(activeCollider)) {
                                                activeCollider.vx = vel1.x;
                                                activeCollider.vy = vel1.y;
                                                activeCollider.x = pv1.x + vel1.x;
                                                activeCollider.y = pv1.y + vel1.y;
                                            }

                                            if (!statics.contains(passiveCollider)) {
                                                passiveCollider.vx = vel2.x;
                                                passiveCollider.vy = vel2.y;
                                                passiveCollider.x = pv2.x + vel2.x;
                                                passiveCollider.y = pv2.y + vel2.y;
                                            }
                                        }


                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

    }

    @Override
    public List<GameObject> getColliders(GameObject obj) {

        ArrayList<GameObject> tempColliders = new ArrayList<GameObject>();

        //Get all the things that overlap with a given gameobject
        for (GameObject collider : colliders){
            if (collider != obj) {
                Sphere collider1 = new Sphere(new Vector3(collider.x, collider.y, 0), collider.radius);
                Sphere collider2 = new Sphere(new Vector3(obj.x, obj.y, 0), obj.radius);
                if (collider1.overlaps(collider2)) {
                    tempColliders.add(collider);
                }
            }
        }

        return tempColliders;
    }

    @Override
    public boolean collidesWithType(GameObject obj, Class type) {
        //returns true if gameobject overlaps with a type of object
        return getCollidersWithType(obj,type).size() > 0;
    }

    @Override
    public boolean collidesWithAnything(GameObject obj) {
        return getColliders(obj).size() > 0;
    }

    @Override
    public List<GameObject> getCollidersWithType(GameObject obj, Class type) {
        List<GameObject> tempColliders = getColliders(obj);
        List<GameObject> collidersOfType = new ArrayList<GameObject>();
        for (GameObject tempCollider : tempColliders){
            if (type.isAssignableFrom(tempCollider.getClass())){
                collidersOfType.add(tempCollider);
            }
        }

        return collidersOfType;
    }

    public void drawDebug(){
        //Draw the collision shapes and velocities for objects
        for (GameObject collider : colliders){
            if (collidesWithAnything(collider)){
                Shapes.setColor(255,0,0,255);
            } else {
                Shapes.setColor(0,0,255,255);
            }
            Shapes.drawSphere(collider.x,collider.y,collider.radius);
        }
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////