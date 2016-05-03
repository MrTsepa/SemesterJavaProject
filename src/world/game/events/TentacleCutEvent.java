package world.game.events;

import world.game.objects.Tentacle;

public class TentacleCutEvent extends Event{
    public float cuttedPart;
    public Tentacle tentacle;

    public TentacleCutEvent(Tentacle tentacle, float cuttedPart) {
        this.cuttedPart = cuttedPart;
        this.tentacle = tentacle;
    }

    public TentacleCutEvent asTentacleCutEvent() {
        return this;
    }
}
