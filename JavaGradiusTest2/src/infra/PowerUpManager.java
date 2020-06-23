package infra;

import entity.Ship;
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
    
    public static void register(int id) {
        if (id == 0) {
            return;
        }
        
        if (remaining.containsKey(id)) {
            remaining.put(id, remaining.get(id) + 1);
        }
        else {
            remaining.put(id, 1);
        }
    }
    
    public static int getRemaining(int id) {
        return remaining.get(id);
    }
    
    public static boolean returnPowerUp(int id) {
        Integer count = remaining.get(id);
        if (count == null || id == 0) {
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
    
    public static boolean isPowerUpAvailable(Ship ship, int index) {
        switch (index) {
            case 0: return ship.speedUpCount < 3; // speed up available ?
            case 1: return !ship.missilesActivated; // missile available ?
            case 2: return ship.mainWeaponType != 2; // double available ?
            case 3: return ship.mainWeaponType != 0; // laser available ?
            case 4: return ship.activatedOptionsCount < 3; // options available ?
            case 5: return !ship.shield.activated; // ? (shield) available ?
        }
        return false;
    }
    
    public static void confirm(Ship ship) {
        // TODO
        boolean confirmed = false;
        switch (currentSelectedPowerUp) {
            case 0: ship.incrementSpeedUp(); confirmed = true; break; // speed up
            case 1: ship.activateMissiles(); confirmed = true; break; // missiles
            case 2: ship.setMainWeapon(2); confirmed = true; break; // double
            case 3: ship.setMainWeapon(0); confirmed = true; break; // laser
            case 4: ship.incrementOption(); confirmed = true; break; // option
            case 5: ship.activateShield(); confirmed = true; break; // ? -> shield
        }
        if (confirmed) {
            Audio.playSound("power_up_confirm");
        }
        currentSelectedPowerUp = -1;
    }
    
}
