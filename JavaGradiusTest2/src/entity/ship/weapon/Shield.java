package entity.ship.weapon;


import entity.Ship;
import infra.CollisionBuffer;
import infra.Entity;
import infra.Terrain;
import infra.View;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class Shield extends Entity {
    
    public boolean free = false;
    public double vx;
    public Ship ship;
    public Terrain terrain;
    
    public boolean activated = false;
    public int life = 3;
    
    public Shield(View view, Terrain terrain, Ship ship) {
        super(view);
        this.terrain = terrain;
        this.ship = ship;
    }

    @Override
    public void start() {
        loadFrame("weapon_shield_blue_0");
        loadFrame("weapon_shield_blue_1");
        loadFrame("weapon_shield_blue_2");
        loadFrame("weapon_shield_blue_3");
        loadFrame("weapon_shield_red_0");
        loadFrame("weapon_shield_red_1");
        loadFrame("weapon_shield_red_2");
        loadFrame("weapon_shield_red_3");
    }
    
    @Override
    public void update() {
        if (free) return;
        if (!activated) return;
        
        frameIndex += 0.5;
        if (life == 1 && frameIndex >= 8) {
            frameIndex = 4;
        }
        else if (life > 1 && frameIndex >= 4) {
            frameIndex = 0;
        }
        
        x = ship.x + 28;
        y = ship.y;
    }

    @Override
    public void draw(Graphics2D g) {
        if (!free && activated) {
            super.draw(g);
        }
    }

    @Override
    public void drawCollision(Graphics2D g) {
        //Point p = getCollisionArea(0);
        //int colX = p.x;
        //int colY = p.y;
        //g.setColor(Color.RED);
        //g.drawRect((int) (colX * 8), (int) (colY * 8), 8, 8);
    }
    
    @Override
    public Point getCollisionArea(int n) {
        int colX = (int) (x + 2) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 2) >> 3; // & 0b1111_1111_1111_1000;
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }
    
    public void free() {
        free = true;
        // clear collision buffer
        CollisionBuffer.clear(collisionArea.x, collisionArea.y, type);
        CollisionBuffer.clear(collisionArea.x + 1, collisionArea.y, type);
    }
    
    public void activate() {
        activated = true;
        life = 3;
    }

    public void wearOut() {
        life--;
        if (life == 0) {
            activated = false;
        }
    }
    
}
