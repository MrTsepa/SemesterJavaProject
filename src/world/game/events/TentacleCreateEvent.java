package world.game.events;

import world.World;
import world.game.objects.Cell;

import java.io.Serializable;

public class TentacleCreateEvent extends Event implements Serializable {
    public int parentCellIndex;
    public int targetCellIndex;

    public TentacleCreateEvent(int parentCellIndex, int targetCellIndex) {
        type = Type.TentacleCreate;
        this.parentCellIndex = parentCellIndex;
        this.targetCellIndex = targetCellIndex;
    }

    @Override
    public TentacleCreateEvent asTentacleCreateEvent() {
        return this;
    }

    @Override
    public void handle(World world) {
        world.cellArray[parentCellIndex].addTentacle(world.cellArray[targetCellIndex]);
    }
}
