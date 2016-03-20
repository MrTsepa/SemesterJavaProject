import org.jsfml.system.Vector2f;

/**
 * Этот класс нужен для проверки работы интерфейса
 * Он рисует второй кружок сдвинутым на (200, 200) относительно первого
 * Created by stas on 20.03.16.
 */
class LocalSocketWrapper implements ExchangeXY {

    private Vector2f[] XYArray = new Vector2f[2];

    LocalSocketWrapper() {
        XYArray[0] = new Vector2f(100, 100);
        XYArray[1] = new Vector2f(300, 300);
    }

    @Override
    public Vector2f[] getRemoteXYArray() {
        return XYArray;
    }

    @Override
    public Vector2f getSelfXY() {
        return XYArray[0];
    }

    @Override
    public void setSelfXY(Vector2f XY) {
        XYArray[0] = XY;
        XYArray[1] = new Vector2f(XY.x + 200, XY.y + 200);
    }

    @Override
    public int getPlayerCount() {
        return 2;
    }
}
