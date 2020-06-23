package entity.enemy;

import entity.Ship;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.PowerUpManager;
import infra.Time;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class EnemyDee01Down extends Entity {
    
    public double vy;
    public double vx;
    public Ship ship;
    public int life;
    public int fireMoment = (int) (60 * Math.random());
    public String color;
    public int powerUpId;
    
    public EnemyDee01Down(View view, Ship ship, int x, int y, String color, int powerUpId) {
        super(view);
        this.ship = ship;
        this.x = x;
        this.y = y;
        this.color = color;
        this.powerUpId = powerUpId;
        PowerUpManager.register(powerUpId);
        type = CollisionBuffer.ENEMY;
        vx = -1;
        vy = 0;
        frameIndex = 0;
        life = 1;
    }

    @Override
    public void start() {
        loadFrame("enemy_cannon_" + color + "_down_0");
        loadFrame("enemy_cannon_" + color + "_down_1");
        loadFrame("enemy_cannon_" + color + "_down_2");
        loadFrame("enemy_cannon_" + color + "_down_3");
        loadFrame("enemy_cannon_" + color + "_down_4");
        loadFrame("enemy_cannon_" + color + "_down_5");
    }
    
    @Override
    public void update() {
        // Para enemy , nao precisa usar o CollisionBuffer, basta mover e verificar a colisao com ship diretamente
        // ou com o ShipBullet
        
        x += vx;

        // turret target ship
        double sy = ship.y;
        if (sy >= y) {
            sy = y - 1;
        }
        double a = Math.atan((x - ship.x) / (sy - y));
        double p = a / (Math.PI * 0.5);
        //System.out.println("a = " + Math.toDegrees(a));
        frameIndex = p * 3 + 3;
        
        // fire
        if (Time.frameCount % 60 == fireMoment) {
            view.fireEnemyBullet((int) x, (int) y);
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
        Audio.playSound("enemy_terrain_explosion");
        
        boolean returnPowerUp = PowerUpManager.returnPowerUp(powerUpId);
        if (returnPowerUp && powerUpId < 128) {
            view.spawnPowerUpRed((int) x, (int) y);
        }
        else if (returnPowerUp && powerUpId >= 128) {
            view.spawnPowerUpBlue((int) x, (int) y);
        }
        
        view.spawnEnemyExplosion((int) x, (int) y);
        destroy();
    }
    
}
