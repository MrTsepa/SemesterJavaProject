package world;

import world.game.objects.Cell;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerInfoCells extends PlayerInfo {
    public ArrayList<Cell> cellArrayList = new ArrayList<>();

    public PlayerInfoCells(Cell... cellArrayList) {
        Collections.addAll(this.cellArrayList, cellArrayList);
    }
}
