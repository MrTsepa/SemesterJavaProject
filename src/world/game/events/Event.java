package world.game.events;

public class Event {
    Type type;
    enum Type { TentacleCreate, TentacleDestroy }
    public TentacleCreateEvent asTentacleCreateEvent() throws Exception {
        throw new Exception();
    }
    public TentacleDestroyEvent asTentacleDestroyEvent() throws Exception {
        throw new Exception();
    }
}
