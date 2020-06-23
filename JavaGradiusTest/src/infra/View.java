package infra;

import entity.enemy.EnemyA;
import entity.enemy.EnemyB;
import entity.Ship;
import entity.EnemyBullet;
import entity.enemy.EnemyC;
import entity.enemy.EnemyD;
import entity.enemy.EnemyDee01;
import entity.enemy.EnemyE;
import entity.EnemyExplosion;
import entity.enemy.EnemyF;
import entity.enemy.EnemyG;
import entity.enemy.EnemyHatchDagumu;
import entity.EnemyHatchExplosion;
import entity.PowerUpBlue;
import entity.PowerUpRed;
import entity.ShipExplosion;
import entity.enemy.EnemyDucker;
import entity.ship.weapon.Option;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Enemies: https://strategywiki.org/wiki/Gradius/Enemies
 * 
 * @author admin
 */
public class View extends JPanel implements MouseMotionListener {
    public static boolean DRAW_COLLISION = false;

    private BufferedImage offscreen = new BufferedImage(320, 200, BufferedImage.TYPE_INT_RGB);
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> entitiesAdd = new ArrayList<>();
    private List<Entity> enemyBullets = new ArrayList<>();
    
    private Terrain terrain;
    private Ship ship;
    private HUD hud;
    
    public View() {
    }

    public void start() {
        hud = new HUD(this);
        entities.add(terrain = new Terrain(this));
        ship = new Ship(this, terrain);
        entities.addAll(ship.bullets);
        entities.addAll(ship.bulletsDouble);
        entities.addAll(ship.lasers);
        entities.addAll(ship.missiles);
        entities.addAll(ship.options);
        for (Option option : ship.options) {
            entities.addAll(option.bullets);
            entities.addAll(option.bulletsDouble);
            entities.addAll(option.lasers);
            entities.addAll(option.missiles);
        }

        entities.add(ship);
        
        // cache enemy bullets
        for (int i=0; i<30; i++) {
            enemyBullets.add(new EnemyBullet(this, terrain, ship));
        }
        entities.addAll(enemyBullets);
        
        startAllEntities();
        
        addKeyListener(new Keyboard());
        addMouseMotionListener(this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                repaint();
            }
        }, 100, 1000 / 30);
        
        Audio.initializeMusic();
        Audio.initializeSound();
        //Audio.playMusic("grlev1");
        Audio.playMusic("gradius");
    }

    private void startAllEntities() {
        entities.forEach(entity -> entity.start());
        hud.start();
    }
    
    private void update() {
        Time.frameCount++;
        spawnNewEnemies();
        entities.forEach(entity -> {
            if (!entity.isDestroyed()) {
                entity.update();
            }
        });
        hud.update();
        if (!entitiesAdd.isEmpty()) {
            entities.addAll(entitiesAdd);
            entitiesAdd.clear();
        }
        entities.removeIf(entity -> entity.isDestroyed());
    }

    private void draw(Graphics2D g) {
        //g.setBackground(Color.BLACK);
        //g.clearRect(0, 0, 320, 200);
        
        entities.forEach(entity -> {
            if (!entity.isDestroyed()) {
                entity.draw(g);
            }
        });
        
        if (DRAW_COLLISION) {
            CollisionBuffer.draw(g);
        }
        
        hud.draw(g);
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        draw((Graphics2D) offscreen.getGraphics());
        ((Graphics2D) g).scale(2, 2);
        g.drawImage(offscreen, 0, 0, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.start();
            view.setPreferredSize(new Dimension(640, 400));
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            view.requestFocus();
        });
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private void spawnNewEnemies() {
        if (Time.frameCount == 150 || Time.frameCount == 300 || Time.frameCount == 450) {
            int powerUpId = PowerUpManager.nextId(5);
            int y = (int) (100 + (100 * Math.random() - 50));
            for (int i = 0; i < 5; i++) {
                Entity enemy = new EnemyA(this, ship, 320 + 20 * i, y, powerUpId);
                enemy.start();
                entities.add(enemy);
            }
        }
        
        if (Time.frameCount == 550 || Time.frameCount == 600 || Time.frameCount == 650) {
            for (int i = 0; i < 3; i++) {
                int x = (int) (50 * Math.random());
                //int y = (int) (100 + (100 * Math.random() - 50));
                Entity enemy = new EnemyB(this, ship, 320 + x, 60 + i * 40);
                enemy.start();
                entities.add(enemy);
            }
        }

        if (Time.frameCount == 700) {
            Entity enemy1 = new EnemyC(this, ship, 320, 80);
            Entity enemy2 = new EnemyC(this, ship, 330, 140);
            enemy1.start();
            enemy2.start();
            entities.add(enemy1);
            entities.add(enemy2);
        }

        if (Time.frameCount == 750) {
            int powerUpId = PowerUpManager.nextId(2);
            Entity enemy1 = new EnemyD(this, ship, 320, 60, powerUpId);
            Entity enemy2 = new EnemyD(this, ship, 330, 100, powerUpId);
            enemy1.start();
            enemy2.start();
            entities.add(enemy1);
            entities.add(enemy2);
        }

        if (Time.frameCount == 900) {
            //int powerUpId = PowerUpManager.nextId(2);
            Entity enemy1 = new EnemyE(this, ship, 320, 60, 0);
            Entity enemy2 = new EnemyF(this, ship, 370, 60, 0);
            enemy1.start();
            enemy2.start();
            entities.add(enemy1);
            entities.add(enemy2);
        }
        
        if (Time.frameCount == 1066) {
            Entity enemy1 = new EnemyDee01(this, ship, 328, 144);
            Entity enemy2 = new EnemyDee01(this, ship, 344, 144);
            enemy1.start();
            enemy2.start();
            entities.add(enemy1);
            entities.add(enemy2);
        }
        
        if (Time.frameCount == 1122) {
            Entity enemy1 = new EnemyG(this, ship, 328, 147, 126, 4, 0);
            Entity enemy2 = new EnemyG(this, ship, 328, 147, 110, 3, 0);
            Entity enemy3 = new EnemyG(this, ship, 328, 147, 94, 2, 0);
            Entity enemy4 = new EnemyG(this, ship, 328, 147, 78, 1, 0);
            Entity enemy5 = new EnemyG(this, ship, 328, 147, 62, 0, 0);
            Entity enemyHatch = new EnemyHatchDagumu(this, ship, 320, 128);
            enemy1.start();
            enemy2.start();
            enemy3.start();
            enemy4.start();
            enemy5.start();
            enemyHatch.start();
            entities.add(enemy1);
            entities.add(enemy2);
            entities.add(enemy3);
            entities.add(enemy4);
            entities.add(enemy5);
            entities.add(enemyHatch);
        }        

        if (Time.frameCount == 1410) {
            Entity enemy1 = new EnemyG(this, ship, 328, 147, 126, 4, 0);
            Entity enemy2 = new EnemyG(this, ship, 328, 147, 110, 3, 0);
            Entity enemy3 = new EnemyG(this, ship, 328, 147, 94, 2, 0);
            Entity enemy4 = new EnemyG(this, ship, 328, 147, 78, 1, 0);
            Entity enemy5 = new EnemyG(this, ship, 328, 147, 62, 0, 0);
            Entity enemyHatch = new EnemyHatchDagumu(this, ship, 320, 128);
            enemy1.start();
            enemy2.start();
            enemy3.start();
            enemy4.start();
            enemy5.start();
            enemyHatch.start();
            entities.add(enemy1);
            entities.add(enemy2);
            entities.add(enemy3);
            entities.add(enemy4);
            entities.add(enemy5);
            entities.add(enemyHatch);
        }        
        
        if (Time.frameCount == 1000 || Time.frameCount == 1150) {
            Entity enemy1 = new EnemyDucker(this, terrain, ship, -32, 144);
            enemy1.start();
            entities.add(enemy1);
        }
        
    }
    
    public void spawnEnemyExplosion(int x, int y) {
        EnemyExplosion explosion = new EnemyExplosion(this, x, y);
        explosion.start();
        entitiesAdd.add(explosion);
    }

    public void spawnEnemyHatchExplosion(int x, int y) {
        EnemyHatchExplosion explosion = new EnemyHatchExplosion(this, x, y);
        explosion.start();
        entitiesAdd.add(explosion);
    }

    public void spawnShipExplosion(int x, int y) {
        ShipExplosion explosion = new ShipExplosion(this, x, y);
        explosion.start();
        entitiesAdd.add(explosion);
    }
    
    public void spawnPowerUpRed(int x, int y) {
        PowerUpRed powerup = new PowerUpRed(this, x, y);
        powerup.start();
        entitiesAdd.add(powerup);
    }    

    public void spawnPowerUpBlue(int x, int y) {
        PowerUpBlue powerup = new PowerUpBlue(this, x, y);
        powerup.start();
        entitiesAdd.add(powerup);
    }    
    
    public void explodeAllEntities() {
        entities.forEach(entity -> {
            if (entity instanceof EnemyA || entity instanceof EnemyB || entity instanceof EnemyC || entity instanceof EnemyD) {
                entity.explode();
            }
        });
    }
    
    public void fireEnemyBullet(int x, int y) {
        for (Entity entity : enemyBullets) {
            EnemyBullet enemyBullet = (EnemyBullet) entity;
            if (enemyBullet.free) {
                enemyBullet.fire(x, y);
                return;
            }
        }
    }
    
}
