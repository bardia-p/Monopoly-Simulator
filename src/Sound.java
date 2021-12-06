import javax.sound.sampled.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 Sound
 *
 * This document is the Sound. This class handles playing music.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 *
 */
public class Sound implements Runnable, Serializable {
    /**
     * Keeps track of sound filename.
     */
    private final String soundName;
    /**
     * Keeps track of the thread object.
     */
    private static Thread th;

    /**
     * The main constructor of the Sound class.
     * @param soundName the name of the file, String.
     * @author Sarah Chow 101143033
     */
    public Sound(String soundName){
        this.soundName = soundName;
        th = new Thread(this);
    }

    /**
     * Plays a music file.
     * @author Sarah Chow 101143033
     */
    public void monopolyMusic(){
        try {
            URL url = this.getClass().getResource(soundName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    /**
     * Starts the thread for playing music.
     * @author Sarah Chow 101143033
     */
    @Override
    public void run() {
        monopolyMusic();
    }

    /**
     * Plays music.
     * @author Sarah Chow 101143033
     */
    public void play(){
        th.start();
    }
}
