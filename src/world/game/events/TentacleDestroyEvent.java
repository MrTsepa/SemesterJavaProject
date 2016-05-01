package world.game.events;

import world.game.objects.Tentacle;

import java.io.Serializable;

public class TentacleDestroyEvent extends Event implements Serializable {
    public Tentacle tentacle;
    public float cutPoint;

    @Override
    public TentacleDestroyEvent asTentacleDestroyEvent() {
        return this;
    }
}
