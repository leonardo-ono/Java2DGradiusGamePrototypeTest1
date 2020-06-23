package infra;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Assets class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Assets {
    
    private static final Map<String, BufferedImage> IMAGES = new HashMap<>();
    private static final Map<String, byte[]> SOUNDS = new HashMap<>();
    private static final Map<String, Sequence> MUSICS = new HashMap<>();

    private Assets() {
    }

    public static BufferedImage getImage(String name) {
        BufferedImage image = null;
        try {
            InputStream is = Assets.class.getResourceAsStream(
                Constants.RES_IMAGE_PATH + name + Constants.RES_IMAGE_FILE_EXT);
            
            image = IMAGES.get(name);
            if (image == null) {
                image = ImageIO.read(is);
                IMAGES.put(name, image);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return image;
    }
    
    public static byte[] getSound(String name) {
        byte[] sound;
        sound = SOUNDS.get(name);
        if (sound == null) {
            String soundResource = Constants.RES_SOUND_PATH + name + Constants.RES_SOUND_FILE_EXT;
            try (
                InputStream is = 
                    Assets.class.getResourceAsStream(soundResource);
                    
                InputStream bis = new BufferedInputStream(is);            
                AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {
                if (!ais.getFormat().matches(Constants.SOUND_AUDIO_FORMAT)) {
                    throw new Exception("Sound '" + soundResource + 
                                        "' format not compatible !");
                }
                long soundSize = 
                    ais.getFrameLength() * ais.getFormat().getFrameSize();
                
                sound = new byte[(int) soundSize];
                ais.read(sound);
                SOUNDS.put(name, sound);
            } 
            catch (Exception ex) {
                Logger.getLogger(
                    Assets.class.getName()).log(Level.SEVERE, null, ex);
                
                System.exit(-1);
            }
        }
        return sound;
    }

    public static Sequence getMusic(String name) {
        Sequence music = MUSICS.get(name);
        if (music == null) {
            String musicResource = Constants.RES_MUSIC_PATH + name + Constants.RES_MUSIC_FILE_EXT;
            try (
                InputStream is = 
                    Assets.class.getResourceAsStream(musicResource)) {
                
                music = MidiSystem.getSequence(is);
                MUSICS.put(name, music);
            } 
            catch (Exception ex) {
                Logger.getLogger(
                    Assets.class.getName()).log(Level.SEVERE, null, ex);
                
                System.exit(-1);
            }
        }
        return music;
    }
    
}
