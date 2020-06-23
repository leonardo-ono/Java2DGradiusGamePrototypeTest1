package entity;


import infra.Entity;
import infra.View;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class EnemyExplosion extends Entity {
    
    public EnemyExplosion(View view, int x, int y) {
        super(view);
        this.x = x;
        this.y = y;
    }

    @Override
    public void start() {
        loadFrame("explosion_0");
        loadFrame("explosion_1");
        loadFrame("explosion_2");
    }
    
    @Override
    public void update() {
        frameIndex += 0.5;
        
        if (frameIndex >= 3) {
            destroy();
        }
    }


    @Override
    public void drawCollision(Graphics2D g) {
        //Point p = getCollisionArea(0);
        //int colX = p.x;
        //int colY = p.y;
        //g.setColor(Color.ORANGE);
        //g.drawRect((int) (colX * 8), (int) (colY * 8), 8, 8);
    }
    
    @Override
    public Point getCollisionArea(int n) {
        int colX = (int) (x + 2) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 2) >> 3; // & 0b1111_1111_1111_1000;
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }
    
}
