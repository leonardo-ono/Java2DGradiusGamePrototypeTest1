package infra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Trail {

    private int size;
    private List<Point> points = new ArrayList<>();
    
    public Trail(int size, int x, int y) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            points.add(new Point(x, y));
        }
    }
    
    public void update(int x, int y) {
        Point head = points.remove(0);
        head.setLocation(x, y);
        points.add(head);
    }
    
    public Point get(int i) {
        return points.get(i);
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        points.forEach(point -> g.fillOval(point.x - 2, point.y - 2, 4, 4));
    }
}
