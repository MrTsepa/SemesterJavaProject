package world.game.events;

import world.World;
import world.game.objects.Tentacle;

public class TentacleCutEvent extends Event{
    public float cuttedPart;
    public int parentCellIndex, targetCellIndex;

    public TentacleCutEvent(int parentCellIndex, int targetCellIndex, float cuttedPart) {
        this.cuttedPart = cuttedPart;
        this.parentCellIndex = parentCellIndex;
        this.targetCellIndex = targetCellIndex;
    }

    public TentacleCutEvent asTentacleCutEvent() {
        return this;
    }

    @Override
    public void handle(World world) {
        Tentacle tentacle = world.cellArray[parentCellIndex].
                getTentacle(world.cellArray[targetCellIndex]);
        tentacle.setState(Tentacle.State.IsCutted);
        tentacle.setCuttedDistancePart(cuttedPart);
        tentacle.setDistancePart(cuttedPart);
    }
}
