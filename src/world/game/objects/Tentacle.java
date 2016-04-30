package world.game.objects;

import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

public class Tentacle implements Drawable {

    static final float triangleHeight = 10;

    private Cell parentCell;
    private Cell targetCell;

    /**
     * число в инетрвале [0, 1] отражающее какую часть
     * отрезка между клетками прошла тентакля
     */
    float distancePart;


    public float getDistancePart() {
        return distancePart;
    }

    public void setDistancePart(float distancePart) {
        this.distancePart = distancePart;
    }

    public Tentacle(Cell parentCell, Cell targetCell) {
        this.parentCell = parentCell;
        this.targetCell = targetCell;
        distancePart = 0;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        Vector2f distanceVector = Vector2f.sub(targetCell.getPosition(), parentCell.getPosition());
        Vector2f normalizedDistanceVector = normalize(distanceVector);
        Vector2f normalizedOrthogonalVector = normalize(new Vector2f(1, -1*distanceVector.x/distanceVector.y));
        Vector2f parentBorderPosition = Vector2f.add(parentCell.getPosition(),
                Vector2f.mul(normalizedDistanceVector, parentCell.getRadius()));
        Vector2f targetBorderPosition = Vector2f.sub(targetCell.getPosition(),
                Vector2f.mul(normalizedDistanceVector, targetCell.getRadius()));
        distanceVector = Vector2f.sub(targetBorderPosition, parentBorderPosition);
        Vector2f headPosition = Vector2f.add(parentBorderPosition, Vector2f.mul(distanceVector, distancePart));
        while (true) {
            ConvexShape polygon = new ConvexShape(headPosition,
                    Vector2f.add(Vector2f.sub(headPosition, Vector2f.mul(normalizedDistanceVector, triangleHeight)),
                            Vector2f.mul(normalizedOrthogonalVector, triangleHeight/2)),
                    Vector2f.sub(Vector2f.sub(headPosition, Vector2f.mul(normalizedDistanceVector, triangleHeight)),
                            Vector2f.mul(normalizedOrthogonalVector, triangleHeight/2))
            );
            polygon.setFillColor(Cell.teamColorMap.get(parentCell.getTeam()));
            renderTarget.draw(polygon);
            headPosition = Vector2f.sub(headPosition, Vector2f.mul(normalizedDistanceVector, triangleHeight));
            if (Float.compare(length(Vector2f.sub(headPosition, parentCell.getPosition())), triangleHeight) < 0) {
                break;
            }
        }
    }

    private Vector2f normalize(Vector2f vector) {
        return Vector2f.div(vector, length(vector));
    }

    private float length(Vector2f vector) {
        return (float) Math.sqrt(vector.x*vector.x + vector.y*vector.y);
    }
}
