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

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        for (Cell cell :
                cellArray) {
            renderTarget.draw(cell);
        }
    }
}
