package com.sameerahman.pacman;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    private static void playSound(String resourcePath) {
        try {
            URL url = Sound.class.getResource(resourcePath);
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } else {
                System.err.println("Sound file not found: " + resourcePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playMunchSound() {
        playSound("/com/sameerahman/pacman/sounds/pacman_chomp.wav");
    }

    public static void playEatingFruitSound() {
        playSound("/com/sameerahman/pacman/sounds/pacman_eatfruit.wav");
    }

    public static void playLoseLifeSound() {
        playSound("/com/sameerahman/pacman/sounds/pacman_death.wav");
    }

    public static void playGameOverSound() {
        playSound("/com/sameerahman/pacman/sounds/game_over.wav");
    }

    public static void playEatGhostSound() {
        playSound("/com/sameerahman/pacman/sounds/pacman_eatghost.wav");
    }

    public static void playIntermissionSound() {
        playSound("/com/sameerahman/pacman/sounds/pacman_intermission.wav");
    }
}
