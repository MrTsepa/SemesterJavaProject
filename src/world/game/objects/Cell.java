package world.game.objects;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import world.game.Team;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Cell implements Drawable, Serializable {

    static HashMap<Team, Color> teamColorMap = new HashMap<>();
    private int energy;
    public Set<Tentacle> tentacleSet = new HashSet<>();
    final private Vector2f position;
    public boolean isClicked = false;
    /**
     * enumeration {Neutral, Player1, Player2}
     */
    private Team team;
    static Font font = new Font();
    {
        try {
            font.loadFromFile((new File("font/ArialBlack.ttf")).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @return координаты центра клетки
     */
    public Vector2f getPosition() {
        return Vector2f.add(position, new Vector2f(getRadius(), getRadius()));
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public float getRadius() {
        return this.energy; // TODO подобрать коэффицент
    }

    public int getTentacleLimit() {
        if (energy < 10) return 1;
        if (energy < 80) return 2;
        return 3;
    }

    public Cell(Vector2f position, int energy, Team team) {
        initTeamColorMap();
        this.position = position;
        this.energy = energy;
        this.team = team;
    }

    private void initTeamColorMap() {
        teamColorMap.put(Team.Neutral, new Color(204, 204, 204)); // Grey
        teamColorMap.put(Team.Player1, Color.GREEN);
        teamColorMap.put(Team.Player2, Color.RED);
    }

    public void addTentacle(Cell targetCell) {
        if (tentacleSet.size() < getTentacleLimit()) {
            Tentacle tentacle = new Tentacle(this, targetCell);
            tentacleSet.add(tentacle);
        }
        // TODO else
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        for (Tentacle tentacle :
                tentacleSet) {
            renderTarget.draw(tentacle);
        }

        CircleShape circleShape = new CircleShape(getRadius());
        circleShape.setFillColor(teamColorMap.get(team));
        circleShape.setPosition(position);
        if (isClicked) {
            circleShape.setOutlineThickness(2);
            circleShape.setOutlineColor(Color.MAGENTA);
        }
        renderTarget.draw(circleShape);

        Text text = new Text((new Integer(energy)).toString(), font, 13);
        text.setColor(Color.BLACK);
        FloatRect textRect = text.getGlobalBounds();
        text.setPosition(Vector2f.sub(getPosition(), new Vector2f(textRect.width/2, textRect.height/2 + 3)));
        renderTarget.draw(text);
    }

}