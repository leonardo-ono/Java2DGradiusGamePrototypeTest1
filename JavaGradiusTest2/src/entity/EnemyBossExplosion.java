package entity;


import infra.Entity;
import infra.View;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class EnemyBossExplosion extends Entity {
    
    public EnemyBossExplosion(View view, int x, int y) {
        super(view);
        this.x = x;
        this.y = y;
    }

    @Override
    public void start() {
        loadFrame("enemy_boss_explosion_0");
        for (int i = 0; i < 2; i++) {
            loadFrame("enemy_boss_explosion_1");
            loadFrame("enemy_boss_explosion_2");
            loadFrame("enemy_boss_explosion_3");
            loadFrame("enemy_boss_explosion_4");
            loadFrame("enemy_boss_explosion_5");
        }
    }
    
    @Override
    public void update() {
        frameIndex += 0.5;
        
        if (frameIndex >= 11) {
            destroy();
        }
    }


    @Override
    public void drawCollision(Graphics2D g) {
        //Point p = getCollisionArea(0);
        //int colX = p.x;
        //int colY = p.y;
        //g.setColor(Color.ORANGE);
        //g.drawRect((int) (colX * 8), (int) (colY * 8), 8, 8);
    }
    
    @Override
    public Point getCollisionArea(int n) {
        int colX = (int) (x + 2) >> 3; //& 0b1111_1111_1111_1000;
        int colY = (int) (y + 2) >> 3; // & 0b1111_1111_1111_1000;
        collisionArea.setLocation(colX, colY);
        return collisionArea;
    }
    
}
