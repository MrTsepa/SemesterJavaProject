package world;

import org.jsfml.system.Vector2f;
import org.omg.CORBA.Object;

public class PlayerInfoXY extends PlayerInfo {
    Vector2f XY;

    public PlayerInfoXY(Vector2f vector2f) {
        super();
        XY = new Vector2f(vector2f.x, vector2f.y);
    }
    public PlayerInfoXY(float x, float y) {
        super();
        XY = new Vector2f(x, y);
    }
}
