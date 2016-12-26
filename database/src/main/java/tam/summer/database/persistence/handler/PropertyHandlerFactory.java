package tam.summer.database.persistence.handler;

import tam.summer.database.persistence.handler.impls.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class PropertyHandlerFactory {
    private final static List<PropertyHandler> handlers = new ArrayList<>();

    static {
        handlers.add(new StringColumnHandler());
        handlers.add(new DatePropertyHandler());
        handlers.add(new BooleanPropertyHandler());
        handlers.add(new BigDecimalPropertyHandler());
        handlers.add(new IntegerColumnHandler());
        handlers.add(new LongColumnHandler());
        handlers.add(new ShortColumnHandler());
        handlers.add(new DoubleColumnHandler());
        handlers.add(new FloatColumnHandler());
        handlers.add(new SqlDatePropertyHandler());
        handlers.add(new TimePropertyHandler());
        handlers.add(new TimestampColumnHandler());
        handlers.add(new BytePropertyHandler());
        handlers.add(new SQLXMLColumnHandler());
        handlers.add(new ObjectPropertyHandler());
    }

    public final static List<PropertyHandler> getHandlers() {
        return handlers;
    }
}
