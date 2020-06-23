package entity;


import entity.ship.weapon.BulletDouble;
import entity.ship.weapon.Laser;
import entity.ship.weapon.Missile;
import entity.ship.weapon.Option;
import entity.ship.weapon.ShipBullet;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.Keyboard;
import infra.PowerUpManager;
import infra.Terrain;
import infra.Time;
import infra.Trail;
import infra.View;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Ship extends Entity {
    
    public Terrain terrain;
    public List<ShipBullet> bullets = new ArrayList<>();
    public List<BulletDouble> bulletsDouble = new ArrayList<>();
    public List<Laser> lasers = new ArrayList<>();
    public List<Missile> missiles = new ArrayList<>();
    public List<Option> options = new ArrayList<>();
    
    public Point[] terrainCollisionPoints = {
        new Point(7, 8),
        new Point(13, 14),
        new Point(20, 11),
        new Point(28, 9),
        new Point(19, 6),
        new Point(11, 4)
    };
    
    private boolean invincible = false;
    
    public Trail trail;
    
    public Ship(View view, Terrain terrain) {
        super(view);
        this.terrain = terrain;
        bullets.add(new ShipBullet(view, terrain, this, 1));
        bullets.add(new ShipBullet(view, terrain, this, 2));
        bullets.add(new ShipBullet(view, terrain, this, 3));
        
        bulletsDouble.add(new BulletDouble(view, terrain, this, 3));

        lasers.add(new Laser(view, terrain, this, 1));
        lasers.add(new Laser(view, terrain, this, 2));
        lasers.add(new Laser(view, terrain, this, 3));
        lasers.add(new Laser(view, terrain, this, 4));
        lasers.add(new Laser(view, terrain, this, 5));
        lasers.add(new Laser(view, terrain, this, 6));
        
        missiles.add(new Missile(view, terrain, this, 1));
        //missiles.add(new Missile(view, terrain, this, 2));
        //missiles.add(new Missile(view, terrain, this, 3));
        options.add(new Option(view, terrain, this, 1));
        options.add(new Option(view, terrain, this, 2));
        options.add(new Option(view, terrain, this, 3));
    }

    @Override
    public void start() {
        loadFrame("ship");
        loadFrame("ship_up");
        loadFrame("ship_down");
        x = 80;
        y = 100;
        type = CollisionBuffer.SHIP;
        trail = new Trail(8 * 4, (int) x, (int) y);
    }
    
    @Override
    public void update() {
        // clear collision buffer
        CollisionBuffer.clear(collisionArea.x, collisionArea.y, type);
                
        frameIndex = 0;
        
        boolean moved = false;

        double speed = 2;

        if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
            x -= speed;
            moved = true;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
            x += speed;
            moved = true;
        }

        if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
            y -= speed;
            moved = true;
            frameIndex = 1;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
            y += speed;
            moved = true;
            frameIndex = 2;
        }

        if (moved) {
            trail.update((int) x, (int) y);
        }
        
        if (Keyboard.isKeyPressedOnce(KeyEvent.VK_SPACE)) {
            fire();
        }

        if (Keyboard.isKeyPressedOnce(KeyEvent.VK_C)) {
            PowerUpManager.confirm();
        }
        
        checkCollisionWithTerrain();
                
        // set collision buffer
        getCollisionArea(0);
        CollisionBuffer.set(collisionArea.x, collisionArea.y, type);
        // TODO check collision with terrain
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        // trail.draw(g);
    }

    
    @Override
    public void drawCollision(Graphics2D g) {
        //g.setColor(Color.BLUE);
        //Point p = getCollisionArea(0);
        //g.drawRect((int) (p.x * 8), (int) (p.y * 8), 8, 8);
    }
    
    @Override
    public Point getCollisionArea(int n) {
        n = Time.frameCount & 1;
        
        int colX = (int) (x + 4) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 0) >> 3; // & 0b1111_1111_1111_1000;
        colX += 1;
        colY += 1;
        if (n==1) {
            colX += 1;
        }
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }
    
    public void fire() {
        for (Option option : options) {
            option.fire();
        }
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
    
    public void destroyBullet(int index) {
        for (ShipBullet bullet : bullets) {
            if (bullet.index == index) {
                bullet.free();
                return;
            }
        }
    }
    
    @Override
    public void explode() {
        if (invincible) {
            return;
        }
        CollisionBuffer.clear(collisionArea.x, collisionArea.y, type);
        view.spawnShipExplosion((int) x, (int) y);
        destroy();
        Audio.stopMusic();
        Audio.playSound("ship_explosion");
    }
    
    public void checkCollisionWithTerrain() {
        for (Point p : terrainCollisionPoints) {
            int sx = (int) (x + p.x);
            int sy = (int) (y + p.y);
            if (terrain.isWall(sx, sy)) {
                explode();
            }
        }
    }
    
}
