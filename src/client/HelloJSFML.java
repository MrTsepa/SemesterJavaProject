package client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.*;
import org.jsfml.window.VideoMode;

import world.*;
import world.game.Team;
import world.game.objects.Cell;

public class HelloJSFML {
    SocketConnection client1 = new SocketConnection();
        
    public static void main(String args[]) throws IOException {
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(640, 480), "Hello JSFML!");
        window.setFramerateLimit(30);

        ArrayList<Drawable> drawables = new ArrayList<>();

        Cell cell1 = new Cell(new Vector2f(30, 10), 15, Team.Player1);
        Cell cell2 = new Cell(new Vector2f(400, 200), 20, Team.Player1);
        cell1.addTentacle(cell2);
        cell1.tentacleSet.iterator().next().setDistancePart((float) 0);
        drawables.add(cell1);
        drawables.add(cell2);

        float x = 0.0f;
        while (window.isOpen()) {
            for (Event event : window.pollEvents()) {
                if (event.type == Event.Type.CLOSED) {
                    window.close();
                }
                if (event.type == Event.Type.KEY_PRESSED) {
                    if (event.asKeyEvent().key == Keyboard.Key.LEFT) {
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.RIGHT) {
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.UP) {
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.DOWN) {
                    }
                }
            }

            cell1.tentacleSet.iterator().next().setDistancePart(x);
            if(x < 1)
                x += 0.006f;
            window.clear(Color.BLACK);

            for (Drawable drawable :
                    drawables) {
                window.draw(drawable);
            }

            window.display();
        }
   }
}

