package entity.enemy.boss;

import entity.EnemyBossBullet;
import entity.Ship;
import infra.Audio;
import infra.CollisionBuffer;
import infra.Entity;
import infra.View;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Enemy Boss Big Core MK I
 * https://gradius.fandom.com/wiki/Big_Core_MK_I
 * https://strategywiki.org/wiki/Gradius/Enemies
 * @author admin
 */
public class BigCore extends Entity {
    
    public int state;
    public double vy;
    public double vx;
    public Ship ship;
    public int life;
    public boolean vulnerable;
    public double angle;
    public int frameCount;
    
    public BigCore(View view, Ship ship, int x, int y) {
        super(view);
        this.ship = ship;
        this.x = x;
        this.y = 100 - 24 - 14;
        type = CollisionBuffer.ENEMY;
        vx = -2;
        vy = 0;
        state = 0;
        life = 49;
        vulnerable = false;
        angle = 0;
        frameCount = 0;
    }

    @Override
    public void start() {
        loadFrame("enemy_boss_big_core_0");
        loadFrame("enemy_boss_big_core_1");
        loadFrame("enemy_boss_big_core_2");
        loadFrame("enemy_boss_big_core_3");
        loadFrame("enemy_boss_big_core_4");
        loadFrame("enemy_boss_big_core_5");
    }
    
    @Override
    public void update() {
        switch (state) {
            case 0: updateState0(); break;
            case 1: updateState1(); break;
            case 2: updateState2(); break;
            case 3: updateState3(); break;
            case 4: updateState4(); break;
            case 5: updateState5(); break;
        }
        
        checkCollision();
    }

    private void updateState0() {
        frameIndex = 0;
        x += vx;
        y += vy;
        if (x < 320 - 128) {
            x = 320 - 128;
            state = 1; 
            frameCount = 0;
        }
    }
    
    private void updateState1() {
        frameIndex = 0;
        x = 320 - 128 + 48 * Math.sin(angle * 1.25);
        y = 100 - 24 - 14 + 48 * Math.sin(angle);
        angle += 0.05;
        if (Math.abs(y - ship.y) < 32) {
            fire();
        }
        frameCount++;
        if (frameCount > 30 * 5) {
            state = 2;
            vulnerable = true;
        }
    }
    private void updateState2() {
        frameIndex = 1 + 4 - (int) (life / 10.0);
        System.out.println("boss life: " + (int) (life / 10.0));
        x = 320 - 128 + 48 * Math.sin(angle * 1.25);
        y = 100 - 24 - 14 + 48 * Math.sin(angle);
        angle += 0.05;
        if (Math.abs(y - ship.y) < 16) {
            fire();
        }
    }
    
    private void updateState3() {
    }
    private void updateState4() {
    }
    private void updateState5() {
    }
    private void updateState6() {
    }
    
    private void checkCollision() {

        // set collision buffer
        getCollisionArea(0);
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP)) {
            //ship.explode();
            hit();
        }
        
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_1)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_1)) {
            //ship.destroyBullet(1);
            hit();
        }
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_2)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_2)) {
            //ship.destroyBullet(2);
            hit();
        }
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.SHIP_BULLET_3)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.SHIP_BULLET_3)) {
            //ship.destroyBullet(3);
            hit();
        }
        
        if (CollisionBuffer.get(collisionArea.x, collisionArea.y, CollisionBuffer.TERRAIN)
                || CollisionBuffer.get(collisionArea.x, collisionArea.y + 1, CollisionBuffer.TERRAIN)) {
            //ship.destroy();
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
        if (!vulnerable) {
            return;
        }
        life--;
        if (life == 0) {
            explode();
        }
        else {
            view.spawnEnemyExplosion((int) (x + 16), (int) (y + 16));
            Audio.playSound("enemy_boss_hit");
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
        int colX = (int) (x + 24) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 24) >> 3; // & 0b1111_1111_1111_1000;
        //if ((Time.frameCount % 2) == 1) {
        //    colY += 1;
        //}
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }

    @Override
    public void explode() {
        Audio.playSound("enemy_boss_explosion");
        /*
        view.spawnEnemyExplosion((int) x, (int) y);
        view.spawnEnemyExplosion((int) x + 16, (int) y);
        view.spawnEnemyExplosion((int) x + 32, (int) y);
        view.spawnEnemyExplosion((int) x, (int) y + 16);
        view.spawnEnemyExplosion((int) x + 16, (int) y + 16);
        view.spawnEnemyExplosion((int) x + 32, (int) y + 16);
        view.spawnEnemyExplosion((int) x, (int) y + 32);
        view.spawnEnemyExplosion((int) x + 16, (int) y + 32);
        view.spawnEnemyExplosion((int) x + 32, (int) y + 32);
        */
        view.spawnEnemyBossExplosion((int) x, (int) y);
        destroy();
        Audio.stopMusic();
    }

    private void fire() {
        // check all 4 bullets available
        for (Entity entity : view.enemyBossBullets) {
            EnemyBossBullet enemyBossBullet = (EnemyBossBullet) entity;
            if (!enemyBossBullet.free) {
                return;
            }
        }
        
        view.fireEnemyBossBullet((int) (x + 25), (int) (y + 4));
        view.fireEnemyBossBullet((int) (x + 25), (int) (y + 44));
        view.fireEnemyBossBullet((int) (x + 13), (int) (y + 14));
        view.fireEnemyBossBullet((int) (x + 13), (int) (y + 34));
    }
    
}
