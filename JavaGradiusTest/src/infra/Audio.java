package infra;


import java.io.BufferedInputStream; 
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Audio class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Audio {
    
    private static final int MAX_SIMULTANEOUS_SOUNDS = 10;
    private static final List<SoundPlayer> SOUND_PLAYERS = 
        new ArrayList<SoundPlayer>();
    
    private static boolean soundInitialized;
    
    private static Sequencer sequencer;
    private static boolean musicInitialized;
    
    private static final Map<Integer, AudioInputStream> SOUNDS = 
        new HashMap<>();

    public static void start() {
        initializeSound();
        initializeMusic();
    }

    public static void initializeSound() {
        soundInitialized = true;
        for (int i = 0; i < MAX_SIMULTANEOUS_SOUNDS; i++) {
            SoundPlayer soundPlayer = new SoundPlayer();
            if (!soundPlayer.initialize()) {
                soundInitialized = false;
                return;
            }
            SOUND_PLAYERS.add(soundPlayer);
        }
    }
    
    public static void initializeMusic() {
        String soundBankFile = Constants.RES_MUSIC_PATH + Constants.RES_MUSIC_SOUND_BANK_FILE;
        try (InputStream is = Audio.class.getResourceAsStream(soundBankFile);
             BufferedInputStream bis = new BufferedInputStream(is)) {

            // load sound bank
            Soundbank soundBank = MidiSystem.getSoundbank(bis);
            // setup synthesizer with loaded sound bank
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
            synthesizer.loadAllInstruments(soundBank);
            // setup sequencer
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
            sequencer.setLoopCount(0);
            musicInitialized = true;
        } catch (Exception ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
            musicInitialized = false;
        }
    }

    public static void stopSound(String soundName) {
        if (!soundInitialized) {
            return;
        }
        try {
            for (SoundPlayer soundPlayer : SOUND_PLAYERS) {
                if (soundPlayer.isPlaying() && 
                    soundPlayer.getCurrentSoundId().equals(soundName)) {
                    
                    soundPlayer.stop();
                    return;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
            soundInitialized = false;
        }
    }
    
    public static void playSound(String soundName) {
        if (!soundInitialized) {
            return;
        }
        try {
            for (SoundPlayer soundPlayer : SOUND_PLAYERS) {
                if (!soundPlayer.isPlaying()) {
                    soundPlayer.play(Assets.getSound(soundName), soundName);
                    return;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
            soundInitialized = false;
        }
    }
    
    public static void playMusic(String musicName) {
        if (!musicInitialized) {
            return;
        }
        try {
            sequencer.setSequence(Assets.getMusic(musicName));
            sequencer.stop();
            sequencer.setTickPosition(0);
            sequencer.start();
            sequencer.setLoopCount(-1);
        } 
        catch (InvalidMidiDataException ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
            musicInitialized = false;
        }
    }

    public static void stopMusic() {
        if (!musicInitialized) {
            return;
        }
        sequencer.stop();
    }

    /*
     * SoundPlayer class.
     */
    public static class SoundPlayer implements Runnable {
        
        private static final int BUFFER_SIZE = 1000;
        
        private Thread thread;
        private boolean running;
        private SourceDataLine line;
        private byte[] currentSound;
        private String currentSoundId;
        
        public SoundPlayer() {
        }

        public String getCurrentSoundId() {
            return currentSoundId;
        }

        public boolean initialize() {
            try {
                createLine();
                line.open();
                line.start();
                running = true;
                thread = new Thread(this);
                thread.start();
            } catch (Exception ex) {
                return false;
            }
            return true;
        }
        
        private void createLine() throws Exception {
            Mixer mixer = AudioSystem.getMixer(null);
            SourceDataLine.Info sourceDataLineInfo = new DataLine.Info(
                    SourceDataLine.class, Constants.SOUND_AUDIO_FORMAT, BUFFER_SIZE);
            
            line = (SourceDataLine) mixer.getLine(sourceDataLineInfo);
        }

        public boolean isPlaying() {
            return currentSound != null;
        }

        public void stop() {
            line.flush();
        }
        
        public synchronized void play(byte[] sound, String soundId) {
            this.currentSoundId = soundId;
            this.currentSound = sound;
            notify();
        }

        @Override
        public void run() {
            while (running) {
                synchronized (this) {
                    try {
                        if (currentSound != null) {
                            line.write(currentSound, 0, currentSound.length);
                            currentSound = null;
                        }
                        wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
        
    }

}
