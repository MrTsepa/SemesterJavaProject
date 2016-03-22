package client_socket;

import java.io.IOException;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.*;
import org.jsfml.window.VideoMode;

public class HelloJSFML {
    socketConnection client1 = new socketConnection();
        
    public static void main(String args[]) throws IOException {
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(640, 480), "Hello JSFML!");
        window.setFramerateLimit(30);
        socketConnection.Connect();

        //LocalSocketWrapper socketConnection = new LocalSocketWrapper();

        CircleShape[] circleShapes = new CircleShape[2];
        circleShapes[0] = new CircleShape(50);
        circleShapes[1] = new CircleShape(50);
        //Vector2f[] XYArray;
          Vector2f selfXY = new Vector2f(World.getSelfXY().x, World.getSelfXY().y);
        

        circleShapes[0].setFillColor(Color.RED);
        circleShapes[1].setFillColor(Color.GREEN);

        while (window.isOpen()) {
            for (Event event : window.pollEvents()) {
                if (event.type == Event.Type.CLOSED) {
                    window.close();
                }
                if (event.type == Event.Type.KEY_PRESSED) {
                    if (event.asKeyEvent().key == Keyboard.Key.LEFT) {
                       World.setSelfXY(new Vector2f(selfXY.x - 10, selfXY.y));
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.RIGHT) {
                         World.setSelfXY(new Vector2f(selfXY.x + 10, selfXY.y));
                         System.out.println("hello!");
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.UP) {
                         World.setSelfXY(new Vector2f(selfXY.x, selfXY.y - 10));
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.DOWN) {
                         World.setSelfXY(new Vector2f(selfXY.x, selfXY.y + 10));
                    }
                     selfXY = World.getSelfXY();
                }
            }

            Vector2f[] XYArray = new Vector2f[2];
            XYArray = World.getRemoteXYArray();
            for (int i = 0; i < circleShapes.length; i++) {
                circleShapes[i].setPosition(XYArray[i]);
            }

            window.clear(Color.BLACK);
            for (CircleShape shape: circleShapes) {
                window.draw(shape);
            }

            window.display();
        }
   }
}

