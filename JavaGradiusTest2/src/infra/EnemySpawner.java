package infra;

import entity.enemy.EnemyDee01Down;
import entity.enemy.EnemyDee01Top;
import entity.enemy.EnemyDuckerDown;
import entity.enemy.EnemyDuckerTop;
import entity.enemy.EnemyFans;
import entity.enemy.EnemyGarun;
import entity.enemy.EnemyGarunRed;
import entity.enemy.EnemyHatchDagumuDown;
import entity.enemy.EnemyHatchDagumuTop;
import entity.enemy.EnemyJumper;
import entity.enemy.EnemyJumperRed;
import entity.enemy.EnemyRugal;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class EnemySpawner {
    
    public static Properties enemies = new Properties();
    
    public static void start(int level) {
        try {
            enemies.load(EnemySpawner.class.getResourceAsStream("/res/level/enemies_" + level + ".txt"));
        } catch (IOException ex) {
            Logger.getLogger(EnemySpawner.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    public static void spawn(int currentX, View view) {
        String key = "x" + (-currentX);
        if (enemies.containsKey(key)) {
            String[] v = ((String) enemies.get(key)).split(",");
            int kx = Integer.parseInt(key.replace("x", ""));
            int ky = Integer.parseInt(v[0]);
            String kname = v[1];
            int kpowerUpId = 0;
            try {
                kpowerUpId = Integer.parseInt(v[2]);
            }
            catch (Exception e) {
                System.out.println("power up id not informed !");
            }
            
            if (kname.equals("dagumu_bottom")) {
                Entity enemy = new EnemyHatchDagumuDown(view, view.ship, 320, 128);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("dagumu_top")) {
                Entity enemy = new EnemyHatchDagumuTop(view, view.ship, 320, 8);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("dee01_bottom")) {
                Entity enemy = new EnemyDee01Down(view, view.ship, 320, 144, "blue", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("dee01_red_bottom")) {
                Entity enemy = new EnemyDee01Down(view, view.ship, 320, 144, "red", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("dee01_top")) {
                Entity enemy = new EnemyDee01Top(view, view.ship, 320, 8, "blue", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("dee01_red_top")) {
                Entity enemy = new EnemyDee01Top(view, view.ship, 320, 8, "red", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_bottom_left")) {
                Entity enemy = new EnemyDuckerDown(view, view.terrain, view.ship, -16, 144, "blue", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_bottom_right")) {
                Entity enemy = new EnemyDuckerDown(view, view.terrain, view.ship, 320, 144, "blue", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_red_bottom_left")) {
                Entity enemy = new EnemyDuckerDown(view, view.terrain, view.ship, -16, 144, "red", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_red_bottom_right")) {
                Entity enemy = new EnemyDuckerDown(view, view.terrain, view.ship, 320, 144, "red", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_red_top_left")) {
                Entity enemy = new EnemyDuckerTop(view, view.terrain, view.ship, -16, 8, "red", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_red_top_right")) {
                Entity enemy = new EnemyDuckerTop(view, view.terrain, view.ship, 320, 8, "red", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_top_left")) {
                Entity enemy = new EnemyDuckerTop(view, view.terrain, view.ship, -16, 8, "blue", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("ducker_top_right")) {
                Entity enemy = new EnemyDuckerTop(view, view.terrain, view.ship, 320, 8, "blue", kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("fans")) {
                Entity enemy = new EnemyFans(view, view.ship, 320, ky, kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("fose")) {
                Entity enemy = new EnemyGarun(view, view.ship, 320, ky);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("fose_red")) {
                Entity enemy = new EnemyGarunRed(view, view.ship, 320, ky, kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("garun")) {
                Entity enemy = new EnemyGarun(view, view.ship, 320, ky);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("garun_red")) {
                Entity enemy = new EnemyGarunRed(view, view.ship, 320, ky, kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("jumper")) {
                Entity enemy = new EnemyJumper(view, view.ship, 320, ky, kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("jumper_red")) {
                Entity enemy = new EnemyJumperRed(view, view.ship, 320, ky, kpowerUpId);
                enemy.start();
                view.addEntity(enemy);
            }
            else if (kname.equals("rugal")) {
                Entity enemy = new EnemyRugal(view, view.ship, 320, ky);
                enemy.start();
                view.addEntity(enemy);
            }
            else {
                throw new RuntimeException("invalid enemy id " + kname);
            }
        }
    }
    
    
    
}
