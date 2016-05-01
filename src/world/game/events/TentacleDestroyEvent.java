package world.game.events;

import world.game.objects.Tentacle;

public class TentacleDestroyEvent extends Event{
    public Tentacle tentacle;
    public float cutPoint;

    @Override
    public TentacleDestroyEvent asTentacleDestroyEvent() {
        return this;
    }
}
