package world;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import world.game.objects.Cell;

import java.io.Serializable;

public class World implements Drawable, Serializable {
    final public Cell[] cellArray;
    public int playerNumber;

    public World(Cell... cells) {
        cellArray = cells;
    }

    public void updateWorld(World world) {
        if (cellArray.length != world.cellArray.length) {
            System.out.println("World of invalid size recieved");
        }
        for (int i = 0; i < cellArray.length; i++) {
            cellArray[i] = world.cellArray[i];
        }
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        for (Cell cell :
                cellArray) {
            renderTarget.draw(cell);
        }
    }
}
