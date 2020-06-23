package infra;

import javax.sound.sampled.AudioFormat;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_UNSIGNED;

/**
 * Constants class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Constants {
    
    public static final int SCREEN_WIDTH = 320;
    public static final int SCREEN_HEIGHT = 200;
    public static final double SCREEN_SCALE_X = 2;
    public static final double SCREEN_SCALE_Y = 2;
    
    public static final int SCALED_SCREEN_WIDTH = (int) (SCREEN_WIDTH * 
                                                         SCREEN_SCALE_X);
    
    public static final int SCALED_SCREEN_HEIGHT = (int) (SCREEN_HEIGHT * 
                                                          SCREEN_SCALE_Y);
    
    public static final int GRAPHIC_SCREEN_REFRESH_RATE = 1000 / 30;
    
    public static final AudioFormat SOUND_AUDIO_FORMAT 
            = new AudioFormat(PCM_UNSIGNED, 11025, 8, 1, 1, 11025, true);
    
    public static final String RES_BITMAP_FONT_ID = "bitmap_font";
    public static final String RES_MUSIC_SOUND_BANK_FILE = "tinypsg.sf2"; 
    
    public static final String RES_IMAGE_FILE_EXT = ".png";
    public static final String RES_SOUND_FILE_EXT = ".wav"; 
    public static final String RES_MUSIC_FILE_EXT = ".mid"; 
    
    public static final String RES_IMAGE_PATH = "/res/image/";
    public static final String RES_SOUND_PATH = "/res/sound/";
    public static final String RES_MUSIC_PATH = "/res/music/";
    
    // scenes id's
    
    public static final String SCENE_OL_PRESENTS = "ol_presents";
    public static final String SCENE_TITLE = "title";
    public static final String SCENE_INSTRUCTIONS = "instructions";
    public static final String SCENE_LEVEL_PRESENTATION = "level_presentation";
    public static final String SCENE_LEVEL = "level";
    public static final String SCENE_GAME_OVER = "game_over";
    
}
