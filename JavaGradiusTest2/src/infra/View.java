package infra;

import entity.EnemyBossBullet;
import entity.EnemyBossExplosion;
import entity.enemy.EnemyFans;
import entity.enemy.EnemyRugal;
import entity.Ship;
import entity.EnemyBullet;
import entity.enemy.EnemyGarun;
import entity.enemy.EnemyGarunRed;
import entity.enemy.EnemyDee01Top;
import entity.enemy.EnemyJumper;
import entity.EnemyExplosion;
import entity.enemy.EnemyJumperRed;
import entity.enemy.EnemyRushDown;
import entity.enemy.EnemyHatchDagumuDown;
import entity.EnemyHatchExplosion;
import entity.PowerUpBlue;
import entity.PowerUpRed;
import entity.ShipExplosion;
import entity.enemy.EnemyDuckerDown;
import entity.enemy.boss.BigCore;
import entity.enemy.boss.Volcano;
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
    public List<Entity> enemyBossBullets = new ArrayList<>();
    
    private List<Volcano> subbosses = new ArrayList<>();
    
    public Terrain terrain;
    public Ship ship;
    private HUD hud;
    
    public View() {
    }

    public void start() {
        hud = new HUD(this);
        terrain = new Terrain(this);
        entities.add(new Background(this, terrain, 0));
        entities.add(new Background(this, terrain, 1));
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
        entities.add(ship.shield);
        
        // cache enemy bullets
        for (int i=0; i<30; i++) {
            enemyBullets.add(new EnemyBullet(this, terrain, ship));
        }
        entities.addAll(enemyBullets);

        // cache enemy boss bullets
        for (int i=0; i<4; i++) {
            enemyBossBullets.add(new EnemyBossBullet(this, terrain, ship));
        }
        entities.addAll(enemyBossBullets);

        //entities.add(terrain);
        
        // subboss
        for (int i = 0; i < 30; i++) {
            Volcano subboss = new Volcano(this, terrain, ship, 0, 0, 0);
            entities.add(subboss);
            subbosses.add(subboss);
        }
        
        startAllEntities();
        EnemySpawner.start(1);
        
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
        terrain.start();
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
        
        terrain.update();
        
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
        
        terrain.draw(g);
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
            frame.setTitle("Java 2D Gradius Game Prototype / Test #1");
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
        EnemySpawner.spawn((int) terrain.x, this);
    }
    
    public void spawnEnemyExplosion(int x, int y) {
        EnemyExplosion explosion = new EnemyExplosion(this, x, y);
        explosion.start();
        entitiesAdd.add(explosion);
    }

    public void spawnEnemyBossExplosion(int x, int y) {
        EnemyBossExplosion explosion = new EnemyBossExplosion(this, x, y);
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
            if (entity instanceof EnemyFans || entity instanceof EnemyRugal || entity instanceof EnemyGarun || entity instanceof EnemyGarunRed) {
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

    public void fireEnemyBossBullet(int x, int y) {
        for (Entity entity : enemyBossBullets) {
            EnemyBossBullet enemyBossBullet = (EnemyBossBullet) entity;
            if (enemyBossBullet.free) {
                enemyBossBullet.fire(x, y);
                return;
            }
        }
    }

    public void addEntity(Entity enemy) {
        entitiesAdd.add(enemy);
    }

    // 0=left, 1=right
    public void spawnSubbossVolcano(int leftRight) {
        for (Volcano subboss : subbosses) {
            if (subboss.free) {
                subboss.spawn(leftRight);
                break;
            }
        }
    }    
    
    public void spawnEnemyBossBigCore(int x, int y) {
        BigCore bigCore = new BigCore(this, ship, x, y);
        bigCore.start();
        entitiesAdd.add(bigCore);
    }
    
}
