package infra;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Entity {
    
    public View view;
    
    public int type; // ver CollisionBuffer type
    
    public double x;
    public double y;
    
    public List<BufferedImage> frames = new ArrayList<>();
    public double frameIndex;
    
    private boolean destroyed = false;
    
    public Point collisionArea = new Point();

    public Entity(View view) {
        this.view = view;
    }
    
    public void loadFrame(String res) {
        BufferedImage sprite = Assets.getImage(res);// ImageIO.read(getClass().getResource(res));
        frames.add(sprite);
    }

    public void start() {
    }

    public void update() {
    }
    
    public void draw(Graphics2D g) {
        BufferedImage sprite = frames.get(((int) frameIndex) % frames.size());
        g.drawImage(sprite, (int) x, (int) y, null);
        if (View.DRAW_COLLISION) {
            drawCollision(g);
        }
    }
    
    public void drawCollision(Graphics2D g) {
        Point p = getCollisionArea(0);
        int colX = p.x;
        int colY = p.y;
        g.setColor(Color.RED);
        g.drawRect((int) (colX * 8 + 0), (int) (colY * 8 + 0), 8, 8);
        g.drawRect((int) (colX * 8 + 8), (int) (colY * 8 + 0), 8, 8);
    }
    
    public Point getCollisionArea(int n) {
        int colX = (int) (x + 4) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 0) >> 3; // & 0b1111_1111_1111_1000;
        colX += 1;
        colY += 1;
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }
    
    public void explode() {
        destroy();
    }
    
    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
    
}
