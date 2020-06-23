package infra;

import java.awt.Graphics2D;

/**
 *
 * @author admin
 */
public class Background extends Entity {
    
    private int index;
    private double vx;
    public Terrain terrain;
    
    public Background(View view, Terrain terrain, int index) {
        super(view);
        this.terrain = terrain;
        this.index = index;
    }

    @Override
    public void start() {
        loadFrame("background_" + index);
        x = 0;
        y = 0;
        vx = index == 0 ? -0.25 : -0.5;
    }
    
    @Override
    public void update() {
        if (terrain.vx == 0) {
            return;
        }
        x += vx;
        if (x < -320) {
            x = 0;
        }
    }
    
    public boolean isWall(int ex, int ey) {
        return false;
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g); //To change body of generated methods, choose Tools | Templates.
    }
    
}
