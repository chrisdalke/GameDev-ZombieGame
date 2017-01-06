////////////////////////////////////////////////
// ZombieGame
// Chris Dalke
////////////////////////////////////////////////
// Module: MathUtils
////////////////////////////////////////////////

package Engine.System;

public class MathUtils {
    public MathUtils() {
    }

    public static float LerpDegrees(float start, float end, float amount)
    {
        float difference = java.lang.Math.abs(end - start);
        if (difference > 180)
        {
            // We need to add on to one of the values.
            if (end > start)
            {
                // We'll add it on to start...
                start += 360;
            }
            else
            {
                // Add it on to end.
                end += 360;
            }
        }

        // Interpolate it.
        float value = (start + ((end - start) * amount));

        // Wrap it..
        float rangeZero = 360;

        if (value >= 0 && value <= 360)
            return value;

        return (value % rangeZero);
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////