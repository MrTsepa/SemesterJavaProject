package world.game.events;

import world.game.objects.Cell;
import world.game.objects.Tentacle;

import java.io.Serializable;

public class TentacleDestroyEvent extends Event implements Serializable {

    public Tentacle tentacle;

    public TentacleDestroyEvent(Tentacle tentacle) {
        this.tentacle = tentacle;
    }

    @Override
    public TentacleDestroyEvent asTentacleDestroyEvent() {
        return this;
    }
}
