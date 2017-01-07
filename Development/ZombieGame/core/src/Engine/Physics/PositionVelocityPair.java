////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: PositionVelocityPair
////////////////////////////////////////////////

package Engine.Physics;

public class PositionVelocityPair {

    public float x;
    public float y;
    public float dx;
    public float dy;

    public PositionVelocityPair(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public PositionVelocityPair(){

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////