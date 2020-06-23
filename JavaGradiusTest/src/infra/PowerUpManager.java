package infra;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author admin
 */
public class PowerUpManager {

    public static int id;
    public static Map<Integer, Integer> remaining = new HashMap<>();
            
    public static int nextId(int count) {
        id++;
        remaining.put(id, count);
        return id;
    }
    
    public static int getId() {
        return id;
    }
    
    public static int getRemaining(int id) {
        return remaining.get(id);
    }
    
    public static boolean returnPowerUp(int id) {
        Integer count = remaining.get(id);
        if (count == null) {
            return false;
        }
        count--;
        remaining.put(id, count);
        return count == 0;
    }
    
    // --- HUD ---
    
    public static int currentSelectedPowerUp = -1;
    
    public static void nextPowerUp() {
        currentSelectedPowerUp++;
        if (currentSelectedPowerUp > 5) {
            currentSelectedPowerUp = 0;
        }
    }
    
    public static boolean isPowerUpAvailable(int index) {
        // TODO
        return false;
    }
    
    public static void confirm() {
        // TODO
        Audio.playSound("power_up_confirm");
        currentSelectedPowerUp = -1;
    }
    
}
