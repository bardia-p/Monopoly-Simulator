import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound implements Runnable {

    private String soundName;
    private static Thread th;

    public Sound(String soundName){
        this.soundName = soundName;
        th = new Thread(this);
    }

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

    @Override
    public void run() {
        monopolyMusic();
    }

    public void play(){
        th.start();
    }

    public void setSoundName(String soundName){
        this.soundName = soundName;
    }
}
