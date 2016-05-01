package world;

import world.game.objects.Cell;

import java.io.Serializable;

public class World implements Serializable {
    final public Cell[] cellArray;

    public World(Cell... cells) {
        cellArray = cells;
    }
}
