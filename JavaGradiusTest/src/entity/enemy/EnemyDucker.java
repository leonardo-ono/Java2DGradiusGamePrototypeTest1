package entity.enemy;

import entity.Ship;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.Terrain;
import infra.Time;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class EnemyDucker extends Entity {
    
    public double baseY;
    public double vy;
    public double vx;
    public Ship ship;
    public int life;
    public int fireMoment = (int) (60 * Math.random());
    
    public double frameIndexAux;
    public boolean walking;
    public double walkStopPosition = 0;
    
    public Terrain terrain;
    
    public EnemyDucker(View view, Terrain terrain, Ship ship, int x, int y) {
        super(view);
        this.terrain = terrain;
        this.ship = ship;
        this.x = x;
        this.y = y;
        this.baseY = y;
        type = CollisionBuffer.ENEMY;
        vy = 0;
        frameIndex = 0;
        frameIndexAux = 0;
        life = 1;
        walking = true;
        walkStopPosition = 80 + 160 * Math.random();
        if (x < 160) {
            vx = +3;
        }
        else {
            vx = -3;
        }
    }

    @Override
    public void start() {
        loadFrame("enemy_ducker_red_down_left_0");
        loadFrame("enemy_ducker_red_down_left_1");
        loadFrame("enemy_ducker_red_down_left_2");
        loadFrame("enemy_ducker_red_down_right_0");
        loadFrame("enemy_ducker_red_down_right_1");
        loadFrame("enemy_ducker_red_down_right_2");
    }
    
    @Override
    public void update() {
        // Para enemy , nao precisa usar o CollisionBuffer, basta mover e verificar a colisao com ship diretamente
        // ou com o ShipBullet
        
        if (walking) {
            int leftOrRight = vx < 0 ? 0 : 3;
            x += vx;
            frameIndex = leftOrRight + (((int) frameIndexAux) % 2);
            frameIndexAux += 0.25;
            if (vx < 0 && x < walkStopPosition) {
                walking = false;
                frameIndexAux = 0;
            } 
            else if (vx > 0 && x > walkStopPosition) {
                walking = false;
                frameIndexAux = 0;
            } 
        }
        else {
            x += -1;
            
            frameIndexAux += 1;
            if (frameIndexAux > 90) {
                frameIndexAux = 0;
                walking = true;
                vx = 3;
                walkStopPosition = 80 + 160 * Math.random();
            }

            int leftOrRight = (ship.x - x) < 0 ? 0 : 3;
            frameIndex = leftOrRight + 2;
            
            // fire
            if (Time.frameCount % 60 == fireMoment) {
                view.fireEnemyBullet((int) x, (int) y);
            }
        }

        // TODO este so funciona para "terrain bottom"
        // fix y 
        y = baseY;
        while (terrain.isWall((int) (x + 8), (int) (y + 15))) {
            y--;
        }
        
        checkCollision();

        // TODO when to destroy
        //if (x < -32) {
        //    destroy();
        //}        
        
    }
    
    private void checkCollision() {

        // set collision buffer
        getCollisionArea(0);
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP)) {
            ship.explode();
            hit();
        }
        else if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_1)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_1)) {
            ship.destroyBullet(1);
            hit();
        }
        else if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_2)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_2)) {
            ship.destroyBullet(2);
            hit();
        }
        else if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_3)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_3)) {
            ship.destroyBullet(3);
            hit();
        }
        else if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.TERRAIN)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.TERRAIN)) {
            ship.destroy();
            hit();
        }
        
        // check collision with ship
        //if (getCollisionArea(0).equals(ship.getCollisionArea(0))
        //        || getCollisionArea(0).equals(ship.getCollisionArea(1))) {
        //    ship.destroy();
        //    destroy();
        //}        
    }

    public void hit() {
        life--;
        if (life == 0) {
            explode();
        }
        else if (life < 5) {
            frameIndex = 1;
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
        destroy();
    }
    
}
