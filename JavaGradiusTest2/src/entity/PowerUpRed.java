package entity;


import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.PowerUpManager;
import infra.View;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class PowerUpRed extends Entity {
    
    public double vx;
    
    public PowerUpRed(View view, int x, int y) {
        super(view);
        this.x = x;
        this.y = y;
        vx = -1;
    }

    @Override
    public void start() {
        loadFrame("power_up_red_0");
        loadFrame("power_up_red_1");
        loadFrame("power_up_red_2");
    }
    
    @Override
    public void update() {
        frameIndex += 0.25;
        x += vx;
        
        getCollisionArea(0);
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP)) {
            Audio.playSound("power_up_red_pick");
            PowerUpManager.nextPowerUp();
            destroy();
        }        
        
        if (x < -16) {
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
