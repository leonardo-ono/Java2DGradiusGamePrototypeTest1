package infra;


import java.awt.Color;
import java.awt.Graphics2D;


/**
 *
 * @author admin
 */
public class CollisionBuffer {

    public static int TERRAIN = 1;
    public static int SHIP = 2;
    public static int ENEMY = 4;
    public static int SHIP_BULLET_1 = 8;
    public static int SHIP_BULLET_2 = 16;
    public static int SHIP_BULLET_3 = 32;
    public static int ENEMY_BULLET = 64;
    
    public static byte[] buffer = new byte[80 * 25];
    
    
    public static void set(int x, int y, int type) {
        try {
            buffer[80 * y + x] |= type;
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }
    }
    
    public static void clear(int x, int y, int type) {
        try {
            buffer[80 * y + x] &= ~type;
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public static boolean get(int x, int y, int type) {
        try {
            return (buffer[80 * y + x] & type) == type;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public static int getInt(int x, int y) {
        try {
            return buffer[80 * y + x] & 0xff;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
 
    public static void draw(Graphics2D g) {
        for (int i = 0; i < buffer.length; i++) {
            int x = i % 80;
            int y = i / 80;
            if (get(x, y, SHIP)) {
                g.setColor(Color.BLUE);
                g.drawRect(x * 8, y * 8, 8, 8);
            }
            else if (get(x, y, SHIP_BULLET_1) || get(x, y, SHIP_BULLET_2) || get(x, y, SHIP_BULLET_3)) {
                g.setColor(Color.GREEN);
                g.drawRect(x * 8, y * 8, 8, 8);
            }
        }
    }
    
    public static void main(String[] args) {
        CollisionBuffer.set(5, 5, TERRAIN);
        CollisionBuffer.set(5, 5, SHIP);
        CollisionBuffer.set(5, 5, ENEMY);
        CollisionBuffer.set(5, 5, ENEMY_BULLET);
        
        System.out.println(CollisionBuffer.get(5, 5, TERRAIN));
        System.out.println(CollisionBuffer.get(5, 5, SHIP));
        System.out.println(CollisionBuffer.get(5, 5, ENEMY));
        System.out.println(CollisionBuffer.get(5, 5, ENEMY_BULLET));
        
        CollisionBuffer.clear(5, 5, SHIP);
        
        System.out.println("---");
        System.out.println(CollisionBuffer.get(5, 5, TERRAIN));
        System.out.println(CollisionBuffer.get(5, 5, SHIP));
        System.out.println(CollisionBuffer.get(5, 5, ENEMY));
        System.out.println(CollisionBuffer.get(5, 5, ENEMY_BULLET));

        System.out.println("---");
        System.out.println("int: " + CollisionBuffer.getInt(5, 5));
    }
}
