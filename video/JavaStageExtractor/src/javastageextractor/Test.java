package javastageextractor;

import java.io.FileReader;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author admin
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String outPropfile = "D:/javaGradius/video/stage_prop.txt";
        Properties p = new Properties();
        p.load(new FileReader(outPropfile));
        Set<String> enemies = new HashSet<>();
        for (Entry entry : p.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            String enemy = value.split(",")[1];
            enemies.add(enemy);
        }
        
        enemies.stream().sorted().forEach(System.out::println);
    }
    
}
