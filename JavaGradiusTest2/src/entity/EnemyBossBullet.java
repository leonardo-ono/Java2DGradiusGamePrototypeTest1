package entity;


import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.Terrain;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class EnemyBossBullet extends Entity {
    
    public boolean free = true;
    public double vy;
    public double vx;
    public Ship ship;
    public Terrain terrain;
    
    public EnemyBossBullet(View view, Terrain terrain, Ship ship) {
        super(view);
        this.ship = ship;
        this.terrain = terrain;
    }

    @Override
    public void start() {
        loadFrame("enemy_boss_bullet");
    }

    @Override
    public void update() {
        if (free) {
            return;
        }
        
        x += vx;
        y += vy;

        checkCollisionWithTerrain();
        
        // set collision buffer
        getCollisionArea(0);
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP)) {
            ship.explode();
            destroy();
        }
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.TERRAIN)) {
            ship.destroy();
            destroy();
        }
        
        // check collision with ship
        //if (getCollisionArea(0).equals(ship.getCollisionArea(0))
        //        || getCollisionArea(0).equals(ship.getCollisionArea(1))) {
        //    ship.destroy();
        //    destroy();
        //}
        
        if (x < 0 || y < 0 || x > 319 || y > 199) {
            //reset();
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
        Point p = getCollisionArea(0);
        int colX = p.x;
        int colY = p.y;
        g.setColor(Color.ORANGE);
        g.drawRect((int) (colX * 8), (int) (colY * 8), 8, 8);
    }
    
    @Override
    public Point getCollisionArea(int n) {
        int colX = (int) (x + 8) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 2) >> 3; // & 0b1111_1111_1111_1000;
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }
        
    public void fire(int x, int y) {
        if (!free) {
            return;
        }
        
        vx = -4;
        vy = 0;
        
        this.x = x;
        this.y = y;
        
        free = false;
    }

    @Override
    public void destroy() {
        free = true;
    }

    public void checkCollisionWithTerrain() {
        int sx = (int) (x + 8);
        int sy = (int) (y + 4);
        if (terrain.isWall(sx, sy)) {
            destroy();
        }
    }
        
}
