package world.game.events;

import world.World;
import world.game.objects.Tentacle;

import java.io.Serializable;

public class TentacleDestroyEvent extends Event implements Serializable {

    public int parentCellIndex, targetCellIndex;

    public TentacleDestroyEvent(int parentCellIndex, int targetCellIndex) {
        this.parentCellIndex = parentCellIndex;
        this.targetCellIndex = targetCellIndex;
    }

    @Override
    public TentacleDestroyEvent asTentacleDestroyEvent() {
        return this;
    }

    @Override
    public void handle(World world) {
        world.cellArray[parentCellIndex].
                getTentacle(world.cellArray[targetCellIndex]).setState(Tentacle.State.IsDestroyed);
    }
}
