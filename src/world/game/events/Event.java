package world.game.events;

import java.io.Serializable;

public class Event implements Serializable {
    Type type;
    enum Type { TentacleCreate, TentacleDestroy }
    public TentacleCreateEvent asTentacleCreateEvent() throws Exception {
        throw new Exception();
    }
    public TentacleDestroyEvent asTentacleDestroyEvent() throws Exception {
        throw new Exception();
    }
}
