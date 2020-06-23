package entity.ship.weapon;


import entity.Ship;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.Terrain;
import infra.Time;
import infra.View;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Option extends Entity {
    
    public int index;
    public boolean free = false;
    public double vx;
    public Ship ship;
    public Terrain terrain;
    
    public List<ShipBullet> bullets = new ArrayList<>();
    public List<BulletDouble> bulletsDouble = new ArrayList<>();
    public List<Laser> lasers = new ArrayList<>();
    public List<Missile> missiles = new ArrayList<>();
    
    public Option(View view, Terrain terrain, Ship ship, int index) {
        super(view);
        this.terrain = terrain;
        this.ship = ship;
        this.index = index;

        frameIndex = index;
        
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
        
        bullets.add(new ShipBullet(view, terrain, ship, 1));
        bullets.add(new ShipBullet(view, terrain, ship, 2));
        bullets.add(new ShipBullet(view, terrain, ship, 3));

        bulletsDouble.add(new BulletDouble(view, terrain, ship, 3));

        lasers.add(new Laser(view, terrain, ship, 1));
        lasers.add(new Laser(view, terrain, ship, 2));
        lasers.add(new Laser(view, terrain, ship, 3));
        lasers.add(new Laser(view, terrain, ship, 4));
        lasers.add(new Laser(view, terrain, ship, 5));
        lasers.add(new Laser(view, terrain, ship, 6));
        
        missiles.add(new Missile(view, terrain, ship, 1));
        //missiles.add(new Missile(view, terrain, ship, 2));
        //missiles.add(new Missile(view, terrain, ship, 3));
        
    }

    @Override
    public void start() {
        loadFrame("weapon_option_0");
        loadFrame("weapon_option_1");
    }
    
    public void fire() {
        /*
        for (ShipBullet bullet : bullets) {
            if (bullet.free) {
                bullet.fire((int) x, (int) y);
                Audio.playSound("ship_shot");
                break;
            }
        }
        
        for (BulletDouble bulletDouble : bulletsDouble) {
            if (bulletDouble.free) {
                bulletDouble.fire((int) x, (int) y);
                Audio.playSound("ship_shot");
                break;
            }
        }
        */
        
        if (lasers.get(0).free && lasers.get(1).free && lasers.get(2).free) {
            lasers.get(0).fire((int) x, (int) y);
            lasers.get(1).fire((int) (x + 40), (int) y);
            lasers.get(2).fire((int) (x + 80), (int) y);
            Audio.playSound("ship_shot");
        }
        else if (lasers.get(3).free && lasers.get(4).free && lasers.get(5).free) {
            lasers.get(3).fire((int) x, (int) y);
            lasers.get(4).fire((int) (x + 40), (int) y);
            lasers.get(5).fire((int) (x + 80), (int) y);
            Audio.playSound("ship_shot");
        }
        
        for (Missile missile : missiles) {
            if (missile.free) {
                missile.fire((int) x, (int) y);
                Audio.playSound("ship_shot");
                break;
            }
        }
    }

    @Override
    public void update() {
        if (free) return;
        
        frameIndex += 0.25;
        
        Point p = ship.trail.get(index * 8);
        x = p.x;
        y = p.y;
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
    
    
}
