package entity.enemy;

import entity.Ship;
import entity.ship.weapon.ShipBullet;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.Time;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class EnemyHatchDagumuDown extends Entity {
    
    public double vy;
    public double vx;
    public Ship ship;
    public int life;
    
    public int enterFrame;
    private int enemyRushIndex = 0;
    
    public EnemyHatchDagumuDown(View view, Ship ship, int x, int y) {
        super(view);
        this.ship = ship;
        this.x = x;
        this.y = y;
        type = CollisionBuffer.ENEMY;
        vx = -1;
        vy = 0;
        frameIndex = 0;
        life = 5;
    }

    @Override
    public void start() {
        loadFrame("enemy_hatch_dagumu_down_0");
        loadFrame("enemy_hatch_dagumu_down_1");
        enterFrame = Time.frameCount + 1;
    }
    
    @Override
    public void update() {
        // Para enemy , nao precisa usar o CollisionBuffer, basta mover e verificar a colisao com ship diretamente
        // ou com o ShipBullet
        
        x += vx;

        //if ((Time.frameCount - enterFrame) % 10 == 0 && enemyRushIndex < 5) {
        if (x < (290 - enemyRushIndex * 8) && enemyRushIndex < 5) {
            Entity enemyRush = new EnemyRushDown(view, ship, (int) (x + 7), (int) y, 112, enemyRushIndex++, 0);
            enemyRush.start();
            view.addEntity(enemyRush);
        }
        
        checkCollision();
        
        if (x < -32) {
            destroy();
        }        
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
            return;
        }
        else if (life < 2) {
            frameIndex = 1;
        }
        Audio.playSound("enemy_boss_hit"); // TODO
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
        Audio.playSound("enemy_terrain_explosion");
        view.spawnEnemyHatchExplosion((int) x, (int) y);
        destroy();
    }
    
}
