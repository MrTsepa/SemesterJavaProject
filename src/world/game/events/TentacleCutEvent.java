package world.game.events;

public class TentacleCutEvent extends Event{
    public float cuttedPart;

    public TentacleCutEvent asTentacleCutEvent() {
        return this;
    }
}
