import org.jsfml.system.Vector2f;

interface ExchangeXY {
    Vector2f[] getRemoteXYArray();
    Vector2f getSelfXY();
    void setSelfXY(Vector2f XY);
    int getPlayerCount();
}
