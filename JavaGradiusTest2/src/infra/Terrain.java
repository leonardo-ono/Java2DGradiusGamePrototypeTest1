package infra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author admin
 */
public class Terrain extends Entity {
    
    private BufferedImage collisionMap;
    private int subBossX = -3008;
    private int state = 0;
    private int startSubbossFrameCount = 0;
    public double vx = -1;
    
    public Terrain(View view) {
        super(view);
    }

    @Override
    public void start() {
        collisionMap = Assets.getImage("stage_1_collision_map");
        loadFrame("stage_1");
        //loadFrame("stage_1_collision_map");
        y = 0;
        x = 0;
        
        // boss test
        //x = -3300;
        //state = 2;
    }
    
    @Override
    public void update() {
        switch (state) {
            case 0: updateState0(); break;
            case 1: updateState1(); break; // subboss
            case 2: updateState2(); break; // boss
            case 3: updateState3(); break; // during boss fight
        }
        
        //System.out.println("x:" + x);
        
        // TODO music
        if (x == -1100) {
            Audio.playMusic("grlev1");
        }
}
    
    private void updateState0() {
        x += vx;
        
        if ((int) x == subBossX) {
            state = 1;
            vx = 0;
            Audio.playMusic("boss");
            startSubbossFrameCount = Time.frameCount;
            return;
        }
        
        if (x < -3625 + 320) {
            x = -3625 + 320;
        }
    }
    
    private void updateState1() {
        view.spawnSubbossVolcano(Time.frameCount % 2);
        if (Time.frameCount - startSubbossFrameCount > 300) {
            state = 2;
            vx = -1;
        }
    }
    
    // boss
    private void updateState2() {
        x += vx;
        if (x < -3329) {
            x = -3329;
            vx = 0;
            view.spawnEnemyBossBigCore(352, 100);
            state = 3;
        }
    }
    
    private void updateState3() {
    }
    
    public boolean isWall(int ex, int ey) {
        int cx = (int) (ex - x);
        int cy = (int) (ey);
        try {
            // System.out.println("iswall(" + cx + "," + cy + ")=" + collisionMap.getRGB(cx, cy));
            return (collisionMap.getRGB(cx, cy) == Color.BLACK.getRGB());
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
