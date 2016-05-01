package world.game.events;

import world.game.objects.Cell;

import java.io.Serializable;

public class TentacleCreateEvent extends Event implements Serializable {
    public Cell parentCell;
    public Cell targetCell;

    public TentacleCreateEvent(Cell parentCell, Cell targetCell) {
        type = Type.TentacleCreate;
        this.parentCell = parentCell;
        this.targetCell = targetCell;
    }

    @Override
    public TentacleCreateEvent asTentacleCreateEvent() {
        return this;
    }
}
