package world;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import world.game.objects.Cell;

import java.io.Serializable;

import static utils.Geometry.length;

public class World implements Drawable, Serializable {
    final public Cell[] cellArray;
    public int playerNumber;

    public World(Cell... cells) {
        cellArray = cells;
    }

    public void update(World world) {
        if (cellArray.length != world.cellArray.length) {
            System.out.println("World of invalid size recieved");
        }
        for (int i = 0; i < cellArray.length; i++) {
            boolean temp = cellArray[i].isClicked;
            cellArray[i] = world.cellArray[i];
            cellArray[i].isClicked = temp;
        }
    }

    public int getCellNumber(Cell cell) {
        for (int i = 0; i < cellArray.length; i++) {
            if (length(Vector2f.sub(cell.getPosition(), cellArray[i].getPosition())) < 0.1)
                return i;
        }
        return -1;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        for (Cell cell :
                cellArray) {
            renderTarget.draw(cell);
        }
    }
}
