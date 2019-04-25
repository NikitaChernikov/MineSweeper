package sweeper;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Audio {
    private String track;
    private Clip clip = null;
    private FloatControl volumeC = null;
    private double wt;
    private boolean background;

    public Audio(String track, double wt){
        this.track = track;
        this.wt = wt;
        this.background = false;
    }

    public void sound(){
        File f = new File(this.track);
        AudioInputStream tr = null;
        try {
            tr = AudioSystem.getAudioInputStream(f);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clip = AudioSystem.getClip();
            clip.open(tr);
            volumeC = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            clip.setFramePosition(0);
            clip.start();

        } catch (LineUnavailableException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(){
        File f = new File(this.track);
        AudioInputStream tr = null;
        try {
            tr = AudioSystem.getAudioInputStream(f);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clip = AudioSystem.getClip();
            clip.open(tr);
            volumeC = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            if (!this.background) {
                clip.setFramePosition(0);
                clip.start();
                this.background = true;
            }

        } catch (LineUnavailableException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        clip.stop();
        clip.close();
        this.background = false;
    }

    public void setVolume() {
        if (wt<0) wt = 0;
        if (wt>1) wt =1;
        float min = volumeC.getMinimum();
        float max = volumeC.getMaximum();
        volumeC.setValue((max-min) * (float)wt + min);
    }

    public void repeat() {
        if (this.background)
            clip.loop(100);
    }
}
