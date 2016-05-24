package world.game.events;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import world.World;

import java.io.Serializable;

// TODO Dasha
// Я думаю тебе нужно будет переделать Event под себя
// для того чтобы его можно было пересылать
// Дело в том что сейчас все классы Event содержат ссылки на тентакли или клетки
// а передавать ссылки смысла нет
// Напрмер можно присвоить всем клеткам номера и пересылать их

// Очень важно чтобы эта функциональность была зашита внутрь класса
// То есть чтобы конструктор остался таким же и не надо было переписывать Main
// где он вызывается

abstract public class Event implements Serializable {
    Type type;
    enum Type { TentacleCreate, TentacleDestroy }
    public TentacleCreateEvent asTentacleCreateEvent() throws Exception {
        throw new Exception();
    }
    public TentacleDestroyEvent asTentacleDestroyEvent() throws Exception {
        throw new Exception();
    }
    public TentacleCutEvent asTentacleCutEvent() throws Exception {
        throw new Exception();
    }

    abstract public void handle(World world);
}
