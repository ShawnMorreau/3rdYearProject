package View;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Interface used to generalize topology shapes: nodes and connections, used
 * for certain purposes such as allowing the user to select only one node or one
 * connection at a time.
 * 
 * @author Aaron Bungay
 */
public interface TopologyShape {

	void draw(Graphics2D g2D, TopologyShape selected);
	boolean contains(Point2D p);

}
