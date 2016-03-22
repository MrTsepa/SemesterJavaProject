package client;

import java.io.IOException;
import java.net.*;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.*;
import org.jsfml.window.VideoMode;

import world.*;

public class HelloJSFML {
    SocketConnection client1 = new SocketConnection();
        
    public static void main(String args[]) throws IOException {
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(640, 480), "Hello JSFML!");
        window.setFramerateLimit(30);
        SocketConnection socketConnection = new SocketConnection();

        //LocalSocketWrapper socketConnection = new LocalSocketWrapper();

        CircleShape[] circleShapes = new CircleShape[2];
        circleShapes[0] = new CircleShape(50);
        circleShapes[1] = new CircleShape(50);

        circleShapes[0].setFillColor(Color.RED);
        circleShapes[1].setFillColor(Color.GREEN);

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

            window.clear(Color.BLACK);
            for (CircleShape shape: circleShapes) {
                window.draw(shape);
            }

            window.display();
        }
   }
}

