package client;

import java.io.IOException;
import java.util.ArrayList;

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

public class HelloJSFML {

    static RenderWindow window = new RenderWindow();
    static World world;

    static Team clientTeam;

    static class DemoRunnable implements Runnable {

        @Override
        public void run() {
            RenderWindow window = new RenderWindow();
            window.create(new VideoMode(640, 480), "Hello JSFML!");
            window.setFramerateLimit(30);

            ArrayList<Drawable> drawables = new ArrayList<>();

            Cell cell1 = new Cell(new Vector2f(30, 10), 15, Team.Player1);
            Cell cell2 = new Cell(new Vector2f(400, 200), 20, Team.Player1);
            cell1.addTentacle(cell2);
            drawables.add(cell1);
            drawables.add(cell2);

            float x = 0.0f;
            Integer trianglePosition = 0;
            boolean flag = true;

            while (window.isOpen()) {
                for (Event event : window.pollEvents()) {
                    if (event.type == Event.Type.CLOSED) {
                        window.close();
                    }
                }

                cell1.tentacleSet.iterator().next().setDistancePart(x);
                if(x < 1)
                    x += 0.006f;
                else {
                    cell1.tentacleSet.iterator().next().yellowTriangles.remove(trianglePosition);
                    if (trianglePosition == 0)
                        trianglePosition = cell1.tentacleSet.iterator().next().getTriangleCount();
                    else {
                        if (flag) {
                            trianglePosition--;
                            flag = false;
                        } else {
                            flag = true;
                        }
                        // магия
                    }
                    cell1.tentacleSet.iterator().next().yellowTriangles.add(trianglePosition);
                }
                window.clear(Color.BLACK);

                for (Drawable drawable :
                        drawables) {
                    window.draw(drawable);
                }

                window.display();
            }
        }
    }

    static class DrawRunnable implements Runnable {
        @Override
        public void run() {
            window.create(new VideoMode(640, 480), "Tentacle Wars");
            window.setFramerateLimit(30);

            Cell parentCellChosen = null;

            while (window.isOpen()) {
                Cell cellClicked = null;
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
                                cellClicked = cell;
                            }
                        }
                        for (Cell cell : world.cellArray) {
                            for (Tentacle tentacle : cell.tentacleSet) {
                                Float part = findIntersectionPart(new Geometry.Circle(mousePosition, 5),
                                        new Geometry.Line(tentacle.parentCell.getPosition(),
                                                tentacle.targetCell.getPosition()));
                                if (part != null) {
                                    tentacleClicked = tentacle;
                                    partTentacleClicked = part;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (cellClicked != null) {
                    if (parentCellChosen == null) {
                        //if (cellClicked.getTeam() == clientTeam) {
                            parentCellChosen = cellClicked;
                            parentCellChosen.isClicked = true;
                        //}
                    } else {
                        if (cellClicked == parentCellChosen) {
                            parentCellChosen.isClicked = false;
                            parentCellChosen = null;
                        } else {
                            parentCellChosen.isClicked = false;
                            world.game.events.Event event =
                                    new TentacleCreateEvent(parentCellChosen, cellClicked);
                            // TODO Dasha send event
                            // пока что будет создавтаься локально
                            parentCellChosen.tentacleSet.add(new Tentacle(parentCellChosen, cellClicked));
                            parentCellChosen = null;
                        }
                    }
                }
                if (tentacleClicked != null && cellClicked == null) {
                    if (tentacleClicked.isConfronted() == false) {
                        if (tentacleClicked.state == Tentacle.State.MovingForward) {
                            world.game.events.Event event =
                                    new TentacleDestroyEvent(tentacleClicked);
                            // TODO Dasha send event
                            // пока что будет создавтаься локально
                            tentacleClicked.state = Tentacle.State.IsDestroyed;
                        }
                        if (tentacleClicked.state == Tentacle.State.Still) {
                            world.game.events.Event event =
                                    new TentacleCutEvent(tentacleClicked, partTentacleClicked);
                            // TODO Dasha send event
                            // пока что будет создавтаься локально
                            tentacleClicked.state = Tentacle.State.IsCutted;
                            tentacleClicked.setCuttedDistancePart(partTentacleClicked);
                            tentacleClicked.setDistancePart(partTentacleClicked);
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

    static class RecvRunnable implements Runnable {

        @Override
        public void run() {
            // TODO Dasha
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        clientTeam = Team.Player1;

        Cell cell1 = new Cell(new Vector2f(100, 100), 20, Team.Player1);
        Cell cell2 = new Cell(new Vector2f(200, 100), 20, Team.Player1);
        Cell cell3 = new Cell(new Vector2f(100, 200), 20, Team.Player2);
        Cell cell4 = new Cell(new Vector2f(300, 200), 20, Team.Player2);
        Cell cell5 = new Cell(new Vector2f(150, 150), 10, Team.Neutral);
        Cell cell6 = new Cell(new Vector2f(400, 400), 20, Team.Player1);

        world = new World(cell1, cell2, cell3, cell4, cell5, cell6);

        Thread demoThread = new Thread(new DemoRunnable());
        Thread drawThread = new Thread(new DrawRunnable());
        Thread recvThread = new Thread(new RecvRunnable());
        Thread updateThread = new Thread(new UpdateRunnable(world));
        //demoThread.start();
        drawThread.start();
        updateThread.start();
        drawThread.join();
        updateThread.interrupt();
    }
}