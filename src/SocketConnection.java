import org.jsfml.system.Vector2f;

class SocketConnection implements ExchangeXY {

    private static final int playerCount = 2;

    /**
     * @return массив с координатами всех игроков включая самого себя
     */
    @Override
    public Vector2f[] getRemoteXYArray() {
        return new Vector2f[0];
    }

    /**
     * желательно не брать свои координаты с сервера а хранить здесь
     * @return координаты самого себя
     */
    @Override
    public Vector2f getSelfXY() {
        return null;
    }

    @Override
    public void setSelfXY(Vector2f XY) {
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }
}
