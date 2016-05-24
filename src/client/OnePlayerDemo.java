package client;

import java.io.IOException;

import org.jsfml.graphics.*;


import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import utils.Geometry;
import world.UpdateRunnable;
import world.World;
import world.game.Team;
import world.game.objects.Cell;
import world.game.events.*;
import world.game.objects.Tentacle;
import static utils.Geometry.findIntersectionPart;

public class OnePlayerDemo {

    static RenderWindow window = new RenderWindow();
    static World world;

    static Team clientTeam;

    static class DrawRunnable implements Runnable {


        @Override
        public void run() {
            window.create(new VideoMode(640, 480), "Tentacle Wars");
            window.setFramerateLimit(30);

            int parentCellChosenIndex = -1;

            while (window.isOpen()) {
                int cellClickedIndex = -1;
                Tentacle tentacleClicked = null;
                Float partTentacleClicked = null;

                for (Event event : window.pollEvents()) {
                    if (event.type == Event.Type.CLOSED) {
                        window.close();
                    }
                    if (event.type == Event.Type.MOUSE_BUTTON_PRESSED) {
                        Vector2i mousePosition = event.asMouseEvent().position;
                        for (Cell cell : world.cellArray) {
                            if (length(Vector2f.sub(cell.getPosition(), new Vector2f(mousePosition)))
                                    < cell.getRadius()) {
                                cellClickedIndex = world.getCellNumber(cell);
                            }
                        }
                        for (Cell cell : world.cellArray) {
                            for (Tentacle tentacle : cell.tentacleSet) {
                                Float part = findIntersectionPart(new Geometry.Circle(mousePosition, 5),
                                        new Geometry.Line(
                                                Vector2f.add(tentacle.parentCell.getPosition(),
                                                        Vector2f.mul(tentacle.getNormalizedDistanceVector(),
                                                                tentacle.parentCell.getRadius())),
                                                Vector2f.sub(tentacle.targetCell.getPosition(),
                                                        Vector2f.mul(tentacle.getNormalizedDistanceVector(),
                                                                tentacle.targetCell.getRadius()))));
                                if (part != null && part < tentacle.getDistancePart()) {
                                    tentacleClicked = tentacle;
                                    partTentacleClicked = part;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (cellClickedIndex != -1) {
                    if (parentCellChosenIndex == -1) {
                        //if (cellClicked.getTeam() == clientTeam) {
                            parentCellChosenIndex = cellClickedIndex;
                            world.cellArray[parentCellChosenIndex].isClicked = true;
                        //}
                    } else {
                        if (cellClickedIndex ==
                                parentCellChosenIndex) {
                            world.cellArray[parentCellChosenIndex].isClicked = false;
                            parentCellChosenIndex = -1;
                        } else {
                            world.game.events.Event event =
                                    new TentacleCreateEvent(parentCellChosenIndex,
                                            cellClickedIndex);
                            world.cellArray[parentCellChosenIndex].isClicked = false;
                            parentCellChosenIndex = -1;
                            event.handle(world);
                        }
                    }
                }
                if (tentacleClicked != null && cellClickedIndex == -1) {
                    if (tentacleClicked.isConfronted() == false) {
                        if (tentacleClicked.getState() == Tentacle.State.MovingForward) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(
                                                world.getCellNumber(tentacleClicked.parentCell),
                                                world.getCellNumber(tentacleClicked.targetCell));
                                event.handle(world);
                            }
                        }
                        if (tentacleClicked.getState() == Tentacle.State.Still) {
                            world.game.events.Event event =
                                    new TentacleCutEvent(
                                            world.getCellNumber(tentacleClicked.parentCell),
                                            world.getCellNumber(tentacleClicked.targetCell),
                                            partTentacleClicked);
                            event.handle(world);
                        }
                    }
                    if (tentacleClicked.isConfronted() == true) {
                        if (tentacleClicked.getState() == Tentacle.State.MovingForward) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(
                                                world.getCellNumber(tentacleClicked.parentCell),
                                                world.getCellNumber(tentacleClicked.targetCell));
                                event.handle(world);
                            }
                        } // немного дублированного кода
                        if (tentacleClicked.getState() == Tentacle.State.IsCollided) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(
                                            world.getCellNumber(tentacleClicked.parentCell),
                                            world.getCellNumber(tentacleClicked.targetCell));
                                event.handle(world);
                            }
                        }
                        if (tentacleClicked.getState() == Tentacle.State.Still) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(
                                                world.getCellNumber(tentacleClicked.parentCell),
                                                world.getCellNumber(tentacleClicked.targetCell));
                                event.handle(world);
                            }

                        }
                    }
                }

                window.clear(Color.BLACK);
                window.draw(world);
                window.display();

            }
        }

        private float length(Vector2f vector) {
            return (float) Math.sqrt(vector.x*vector.x + vector.y*vector.y);
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
        clientTeam = Team.Player1;
        Cell cell1 = new Cell(new Vector2f(100, 100), 20, Team.Player1);
        Cell cell2 = new Cell(new Vector2f(200, 100), 20, Team.Player1);
        Cell cell3 = new Cell(new Vector2f(100, 200), 20, Team.Player2);
        Cell cell4 = new Cell(new Vector2f(300, 200), 20, Team.Player2);
        Cell cell5 = new Cell(new Vector2f(150, 150), 10, Team.Neutral);
        Cell cell6 = new Cell(new Vector2f(400, 400), 20, Team.Player1);

        world = new world.World(cell1, cell2, cell3, cell4, cell5, cell6);

        Thread drawThread = new Thread(new DrawRunnable());
        Thread updateThread = new Thread(new UpdateRunnable(world));

        drawThread.start();
        updateThread.start();

        drawThread.join();
        updateThread.interrupt();
    }
}

