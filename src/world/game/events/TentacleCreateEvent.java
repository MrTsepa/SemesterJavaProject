package world.game.events;

import world.game.objects.Cell;

public class TentacleCreateEvent extends Event{
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
