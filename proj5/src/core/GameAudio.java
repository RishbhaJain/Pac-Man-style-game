package core;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameAudio {
    private static final String BASE_PATH = System.getProperty("user.dir") + File.separator + "sounds" + File.separator;
    private static SourceDataLine backgroundMusicLine;
    private static Thread musicThread;
    private static volatile boolean stopMusic = false;

    // Background music - uses streaming for looping
    public static void playMenuMusic() {
        playBackgroundMusic("menu_music.wav");
    }

    public static void playExplorationMusic() {
        playBackgroundMusic("exploration_music.wav");
    }

    private static void playBackgroundMusic(String filename) {
        // Stop any currently playing background music
        stopMusic = true;
        if (musicThread != null && musicThread.isAlive()) {
            try {
                musicThread.join(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        stopMusic = false;
        musicThread = new Thread(() -> {
            try {
                while (!stopMusic) {
                    File audioFile = new File(BASE_PATH + filename);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioStream.getFormat();

                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    backgroundMusicLine = (SourceDataLine) AudioSystem.getLine(info);
                    backgroundMusicLine.open(format);
                    backgroundMusicLine.start();

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = audioStream.read(buffer)) != -1 && !stopMusic) {
                        backgroundMusicLine.write(buffer, 0, bytesRead);
                    }

                    backgroundMusicLine.drain();
                    backgroundMusicLine.close();
                    audioStream.close();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Error playing background music '" + filename + "': " + e.getMessage());
            }
        });
        musicThread.setDaemon(true);
        musicThread.start();
    }

    // Sound effects - use Java Sound API for mixing with background music
    public static void playWalkSound() {
        playSoundEffect("footstep.wav");
    }

    public static void playCollectSound() {
        playSoundEffect("collect.wav");
    }

    public static void playHitSound() {
        playSoundEffect("hit.wav");
    }

    private static void playSoundEffect(String filename) {
        new Thread(() -> {
            try {
                File audioFile = new File(BASE_PATH + filename);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
                soundLine.open(format);
                soundLine.start();

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = audioStream.read(buffer)) != -1) {
                    soundLine.write(buffer, 0, bytesRead);
                }

                soundLine.drain();
                soundLine.close();
                audioStream.close();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Error playing sound effect '" + filename + "': " + e.getMessage());
            }
        }).start();
    }

    public static void stopMusic() {
        stopMusic = true;
        if (backgroundMusicLine != null) {
            backgroundMusicLine.stop();
            backgroundMusicLine.close();
        }
    }
}