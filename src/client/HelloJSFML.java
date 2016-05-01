package client;

import java.io.IOException;
import java.util.ArrayList;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;

import org.jsfml.window.event.Event;
import world.UpdateRunnable;
import world.World;
import world.game.Team;
import world.game.objects.Cell;

public class HelloJSFML {

    static World world;

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
            RenderWindow window = new RenderWindow();
            window.create(new VideoMode(640, 480), "Tentacle Wars");
            window.setFramerateLimit(30);
            while (window.isOpen()) {
                for (Event event : window.pollEvents()) {
                    if (event.type == Event.Type.CLOSED) {
                        window.close();
                    }
                }

                window.clear(Color.BLACK);
                window.draw(world);
                window.display();
            }
        }
    }

    static class RecvRunnable implements Runnable {

        @Override
        public void run() {
            // TODO
        }
    }

    static class EventHandleRunnable implements Runnable {

        @Override
        public void run() {
            // TODO
        }
    }

    public static void main(String args[]) throws IOException {
        Thread demoThread = new Thread(new DemoRunnable());
        Thread drawThread = new Thread(new DrawRunnable());
        Thread recvThread = new Thread(new RecvRunnable());
        Thread eventHandleThread = new Thread(new EventHandleRunnable());
        Thread updateThread = new Thread(new UpdateRunnable());
        demoThread.start();
    }
}