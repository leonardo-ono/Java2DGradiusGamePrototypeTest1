package entity.enemy.boss;

import entity.Ship;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.PowerUpManager;
import infra.Terrain;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Enemy Sub-Boss Volcano
 * https://gradius.fandom.com/wiki/Volcano
 * @author admin
 */
public class Volcano extends Entity {
    
    public int state;
    public double vy;
    public double vx;
    public int powerUpId;
    public Ship ship;
    public boolean free = true;
    public double angle;
    public double vangle;
    public Terrain terrain;
    
    public Volcano(View view, Terrain terrain, Ship ship, int x, int y, int powerUpId) {
        super(view);
        this.terrain = terrain;
        this.ship = ship;
        this.x = x;
        this.y = y;
        this.powerUpId = powerUpId;
        PowerUpManager.register(powerUpId);
        type = CollisionBuffer.ENEMY;
        vx = -4;
        vy = 0;
        state = 0;
    }

    @Override
    public void start() {
        loadFrame("enemy_subboss_volcano");
    }
    
    // 0=left 1=right
    public void spawn(int leftRight) {
        // left
        if (leftRight == 0) {
            x = 3265 - 3008 - 8;
            y = 131 - 8;
            free = false;
        }
        // right
        else if (leftRight == 1) {
            x = 3138 - 3008 - 8;
            y = 130 - 12;
            free = false;
        }
        vx = Math.random() * 7 - 3;
        vy = -3 + -4 * Math.random();
        vangle = 0.4 * Math.random() - 0.2;
        angle = (2 * Math.PI) * Math.random();
    }
    
    @Override
    public void update() {
        if (free) {
            return;
        }
        
        x += vx;
        y += vy;
        vy += 0.15;
        angle += vangle;
        
        checkCollision();
        checkCollisionWithTerrain();
    }

    @Override
    public void draw(Graphics2D g) {
        if (!free) {
            AffineTransform originalTransform = g.getTransform();
            g.translate(x + 8, y + 8);
            g.rotate(angle);
            g.translate(-8, -8);
            BufferedImage sprite = frames.get(((int) frameIndex) % frames.size());
            g.drawImage(sprite, (int) 0, (int) 0, null);
            g.setTransform(originalTransform);
        }
    }
    
    private void checkCollision() {

        // set collision buffer
        getCollisionArea(0);
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP)) {
            ship.explode();
            explode();
        }
        
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_1)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_1)) {
            ship.destroyBullet(1);
            explode();
        }
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_2)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_2)) {
            ship.destroyBullet(2);
            explode();
        }
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_3)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_3)) {
            ship.destroyBullet(3);
            explode();
        }
        
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.TERRAIN)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.TERRAIN)) {
            ship.destroy();
            explode();
        }
        
        // check collision with ship
        //if (getCollisionArea(0).equals(ship.getCollisionArea(0))
        //        || getCollisionArea(0).equals(ship.getCollisionArea(1))) {
        //    ship.destroy();
        //    destroy();
        //}        

        if (x < 0 || y < 0 || x > 319 || y > 199) {
            free = true;
        }
        
    }


    @Override
    public void drawCollision(Graphics2D g) {
        Point p = getCollisionArea(0);
        int colX = p.x;
        int colY = p.y;
        g.setColor(Color.RED);
        g.drawRect((int) (colX * 8), (int) (colY * 8), 8, 8);
        g.drawRect((int) (colX * 8), (int) (colY * 8 + 8), 8, 8);
    }
    
    @Override
    public Point getCollisionArea(int n) {
        int colX = (int) (x + 8) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 4) >> 3; // & 0b1111_1111_1111_1000;
        //if ((Time.frameCount % 2) == 1) {
        //    colY += 1;
        //}
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }

    @Override
    public void explode() {
        Audio.playSound("enemy_explosion");
        view.spawnEnemyExplosion((int) x, (int) y);
        free = true;
    }
    
    public void checkCollisionWithTerrain() {
        int sx = (int) (x + 8);
        int sy = (int) (y + 8);
        if (terrain.isWall(sx, sy)) {
            explode();
        }
    }
    
}
