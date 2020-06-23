package entity.enemy;

import entity.Ship;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.PowerUpManager;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class EnemyRushDown extends Entity {
    
    public int state;
    public double vy;
    public double vx;
    public int index;
    public int powerUpId;
    public Ship ship;
    public double minY;
    
    public EnemyRushDown(View view, Ship ship, int x, int y, int minY, int index, int powerUpId) {
        super(view);
        this.ship = ship;
        this.x = x;
        this.y = y;
        this.minY = minY;
        this.index = index;
        this.powerUpId = powerUpId;
        type = CollisionBuffer.ENEMY;
        
        //vx = -1;
        //vy = 0;
        //state = 0;
        vx = -1;
        vy = -4;
        state = 1;
        frameIndex = index;
    }

    @Override
    public void start() {
        loadFrame("enemy_g_0");
        loadFrame("enemy_g_1");
        loadFrame("enemy_g_2");
        loadFrame("enemy_g_3");
    }
    
    @Override
    public void update() {
        frameIndex += 0.5;
        // Para enemy , nao precisa usar o CollisionBuffer, basta mover e verificar a colisao com ship diretamente
        // ou com o ShipBullet

        switch (state) {
            //case 0: update0(); break;
            case 1: update1(); break;
            case 2: update2(); break;
        }

        x += vx;
        y += vy;
        
        checkCollision();
        
    }
    
    //private void update0() {
    //    if (x < (300 - index * 8)) {
    //        vx = -1;
    //        vy = -4;
    //        state = 1;
    //    }
    //}
    
    private void update1() {
        //if (y < minY) {
        if (y < ship.y && y < 172 - 48) {
            vx = -4;
            vy = 0;
            state = 2;
        }
    }
    
    private void update2() {
        if (x < -16 || y < -16 || x > 319 || y > 199) {
            destroy();
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
        
        if (PowerUpManager.returnPowerUp(powerUpId)) {
            view.spawnPowerUpRed((int) x, (int) y);
        }
        
        view.spawnEnemyExplosion((int) x, (int) y);
        
        destroy();
    }
    
}
