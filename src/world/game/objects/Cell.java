package world.game.objects;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import world.game.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Cell implements Drawable {

    static HashMap<Team, Color> teamColorMap = new HashMap<>();
    private int energy;
    public Set<Tentacle> tentacleSet = new HashSet<>();
    private Vector2f position;
    /**
     * enumeration {Neutral, Player1, Player2}
     */
    private Team team;

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
        tentacleSet.forEach(renderTarget::draw);

        CircleShape circleShape = new CircleShape(getRadius());
        circleShape.setFillColor(teamColorMap.get(team));
        circleShape.setPosition(position);
        renderTarget.draw(circleShape);
        // TODO text
    }

    public float getRadius() {
        return this.energy; // TODO подобрать коэффицент
    }

    public int getTentacleLimit() {
        if (energy < 10) return 1;
        if (energy < 80) return 2;
        return 3;
    }
}