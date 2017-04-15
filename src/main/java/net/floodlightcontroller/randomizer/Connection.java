package net.floodlightcontroller.randomizer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.randomizer.web.ConnectionSerializer;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.types.DatapathId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by geddingsbarrineau on 9/14/16.
 * <p>
 * This is a connection object for the EAGER project.
 */
@JsonSerialize(using = ConnectionSerializer.class)
public class Connection {
    private static Logger log = LoggerFactory.getLogger(Connection.class);

    private Host source;
    private Host destination;
    private DatapathId sw;
    private FlowFactory flowFactory;
    private Direction direction;

    public Connection(Host source, Host destination, Direction direction, DatapathId sw) {
        this.source = source;
        this.destination = destination;
        this.direction = direction;
        this.sw = sw;

        flowFactory = new FlowFactory(this);

        IOFSwitch ofSwitch = Randomizer.switchService.getActiveSwitch(sw);
        for (OFFlowMod flow : flowFactory.getFlowAdds()) {
            ofSwitch.write(flow);
        }

    }

    public void update() {
        IOFSwitch ofSwitch = Randomizer.switchService.getActiveSwitch(sw);
        for (OFFlowMod flow : flowFactory.getFlowAdds()) {
            ofSwitch.write(flow);
        }

    }

    public Host getSource() {
        return source;
    }

    public Host getDestination() {
        return destination;
    }

    public Direction getDirection() {

        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connection that = (Connection) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        return destination != null ? destination.equals(that.destination) : that.destination == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "source=" + source +
                ", destination=" + destination +
                ", direction=" + direction +
                '}';
    }

}
