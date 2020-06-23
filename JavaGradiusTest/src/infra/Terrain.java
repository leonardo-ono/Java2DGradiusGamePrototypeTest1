package infra;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author admin
 */
public class Terrain extends Entity {
    
    private BufferedImage collisionMap;
    
    public Terrain(View view) {
        super(view);
    }

    @Override
    public void start() {
        collisionMap = Assets.getImage("stage_1_collision_map");
        loadFrame("stage_1");
        //loadFrame("stage_1_collision_map");
        x = 0;
        y = 0;
    }
    
    @Override
    public void update() {
        double speed = 1;
        x -= speed;
        if (x < -3625 + 320) {
            x = -3625 + 320;
        }
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
    
}
