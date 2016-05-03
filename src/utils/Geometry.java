package utils;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class Geometry {
    public static class Circle {
        public Circle(Vector2i position, int radius) {
            this.position = position;
            this.radius = radius;
        }

        Vector2i position;
        int radius;
    }

    public static class Line {
        public Line(Vector2f startPosition, Vector2f endPosition) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        public Vector2f getVector() {
            return Vector2f.sub(endPosition, startPosition);
        }

        public Vector2f getNormalizedVector() {
            return normalize(getVector());
        }

        public float getLength() {
            return length(getVector());
        }

        Vector2f startPosition;
        Vector2f endPosition;
    }

    /**
     * @return Часть отрезка - число [0, 1] обозначает точку пересечения
     * Null в случае отсутствия пересечения
     */
    public static Float findIntersectionPart(Circle circle, Line line) {
        Vector2f point = line.startPosition;
        for (float part = 0; part < 1f; part += 0.05f) {
            point = Vector2f.add(point, Vector2f.mul(line.getNormalizedVector(),
                    line.getLength()*0.05f));
            if (length(point, new Vector2f(circle.position)) < circle.radius) {
                return part;
            }
        }
        return null;
    }

    public static Vector2f normalize(Vector2f vector) {
        return Vector2f.div(vector, length(vector));
    }

    public static float length(Vector2f vector) {
        return (float) Math.sqrt(vector.x*vector.x + vector.y*vector.y);
    }

    public static float length(Vector2f start, Vector2f end) {
        return (new Line(start, end)).getLength();
    }

    public static float length(Line line) {
        return length(Vector2f.sub(line.endPosition, line.startPosition));
    }
}
