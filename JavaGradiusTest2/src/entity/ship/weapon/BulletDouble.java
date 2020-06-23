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
public class BulletDouble extends Entity {
    
    public int index;
    public boolean free = true;
    public double vx;
    public double vy;
    public Ship ship;
    public Terrain terrain;

    public BulletDouble(View view, Terrain terrain, Ship ship, int index) {
        super(view);
        this.terrain = terrain;
        this.ship = ship;
        this.index = index;
        switch (index) {
            case 1:
                type = CollisionBuffer.SHIP_BULLET_1;
                break;
            case 2:
                type = CollisionBuffer.SHIP_BULLET_2;
                break;
            case 3:
                type = CollisionBuffer.SHIP_BULLET_3;
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {
        loadFrame("weapon_bullet_double");
    }
    
    public void fire(int sx, int sy) {
        x = sx + 20;
        y = sy + 8;
        vx = 4;
        vy = -4;
        free = false;
        frameIndex = 0;
    }

    @Override
    public void update() {
        if (free) return;

        // clear collision buffer
        CollisionBuffer.clear(collisionArea.x, collisionArea.y, type);
        CollisionBuffer.clear(collisionArea.x + 1, collisionArea.y, type);
        
        if (x > 320 || y < 0) {
            free = true;
            return;
        }
        
        x += vx;
        y += vy;

        checkCollisionWithTerrain();
        
        // set collision buffer
        getCollisionArea(0);
        CollisionBuffer.set(collisionArea.x, collisionArea.y, type);
        CollisionBuffer.set(collisionArea.x + 1, collisionArea.y, type);
        
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.TERRAIN)
                || CollisionBuffer.get(collisionArea.x + 1, collisionArea.y, CollisionBuffer.TERRAIN)) {
            destroy();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!free) {
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
    
    public void checkCollisionWithTerrain() {
        int sx = (int) (x + 4);
        int sy = (int) (y + 4);
        if (terrain.isWall(sx, sy)) {
            free();
        }
    }
    
}
