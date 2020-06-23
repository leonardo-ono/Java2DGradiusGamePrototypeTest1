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
        loadFrame("hud"); // 0
        loadFrame("hud_power_up_0_0"); // 1
        loadFrame("hud_power_up_1_0"); // 2
        loadFrame("hud_power_up_2_0"); // 3
        loadFrame("hud_power_up_3_0"); // 4
        loadFrame("hud_power_up_4_0"); // 5
        loadFrame("hud_power_up_5_0"); // 6
        loadFrame("hud_power_up_0_1"); // 7
        loadFrame("hud_power_up_1_1"); // 8
        loadFrame("hud_power_up_2_1"); // 9
        loadFrame("hud_power_up_3_1"); // 10
        loadFrame("hud_power_up_4_1"); // 11
        loadFrame("hud_power_up_5_1"); // 12
        loadFrame("hud_power_up_empty_0"); // 13
        loadFrame("hud_power_up_empty_1"); // 14
        x = 0;
        y = 172;
    }
    
    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        
        for (int i = 0; i < 6; i++) {
            BufferedImage powerUp = null;
            if (PowerUpManager.isPowerUpAvailable(view.ship, i)) {
                if (PowerUpManager.currentSelectedPowerUp == i) {
                    powerUp = frames.get(i + 7);
                }
                else {
                    powerUp = frames.get(i + 1);
                }
            }
            else {
                if (PowerUpManager.currentSelectedPowerUp == i) {
                    powerUp = frames.get(14);
                }
                else {
                    powerUp = frames.get(13);
                }
            }
            g.drawImage(powerUp, 64 + 32 * i, 176, null);
        }
        
        // draw current selected power up
        //if (PowerUpManager.currentSelectedPowerUp >= 0) {
        //    BufferedImage powerUp = frames.get(PowerUpManager.currentSelectedPowerUp + 1);
        //}
        
    }
    
    
}
