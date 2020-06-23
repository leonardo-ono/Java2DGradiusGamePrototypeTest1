package infra;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;



/**
 *
 * @author admin
 */
public class HUD extends Entity {

    public HUD(View view) {
        super(view);
    }

    @Override
    public void start() {
        loadFrame("hud");
        loadFrame("hud_power_up_0_1");
        loadFrame("hud_power_up_1_1");
        loadFrame("hud_power_up_2_1");
        loadFrame("hud_power_up_3_1");
        loadFrame("hud_power_up_4_1");
        loadFrame("hud_power_up_5_1");
        x = 0;
        y = 172;
    }
    
    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        
        // draw current selected power up
        if (PowerUpManager.currentSelectedPowerUp >= 0) {
            BufferedImage powerUp = frames.get(PowerUpManager.currentSelectedPowerUp + 1);
            g.drawImage(powerUp, 64 + 32 * PowerUpManager.currentSelectedPowerUp, 176, null);
        }
    }
    
    
}
