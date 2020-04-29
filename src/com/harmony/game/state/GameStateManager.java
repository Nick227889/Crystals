package com.harmony.game.state;

import com.harmony.game.animation.scene.Scene;
import com.harmony.game.audio.BackgroundAmbience;
import com.harmony.game.graphics.Camera;
import com.harmony.game.graphics.Display;
import com.harmony.game.graphics.Font;
import com.harmony.game.physics.collision.BoxCollider;
import com.harmony.game.save.SaveData;
import com.harmony.game.state.chapters.Chapter1;
import com.harmony.game.gui.GUI;
import com.harmony.game.state.chapters.Chapter2;
import com.harmony.game.utils.ImageUtils;
import com.harmony.game.utils.Timer;
import com.harmony.game.utils.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameStateManager {

    private static BufferedImage loadingImage = ImageUtils.loadImage("/ui/loading.png");

    public static final int MENU_STATE          = -1;
    public static final int PLAYER_STATE        = -2;
    public static final int SETTINGS_STATE      = -3;

    public static final int CHAPTER_1           =  1;
    public static final int CHPATER_2           =  2;

    private static QuitConfirmation quitConfirmationState;
    private static Scene scene;

    private static State currentState;
    private static int currentStateId;

    private static boolean pause = true;
    private static boolean quitConfirmation = false;
    private static boolean cutScene = false;

    private static Graphics2D g;
    private static Timer timer = new Timer();

    private Camera camera = new Camera(new BoxCollider(null, new Vector2f(0, 0), Display.width, Display.height));

    public GameStateManager(Graphics2D g) {
        GameStateManager.g = g;
    }

    public static void setCurrentState(int currentState) {
        pause = true;

        BackgroundAmbience.stopAllBackground();

        if(currentState < 0) {
            g.drawImage(loadingImage, -1, -1, Display.width + 2, Display.height + 2, null);
            Display.update();
        } else {
            showChapter(currentState);
        }

        System.out.println("# Setting Current State To ID: " + currentState);

        currentStateId = currentState;

        if(GameStateManager.currentState != null) GameStateManager.currentState.onDestroy();

        State tempState = null;

        switch (currentState) {
            case MENU_STATE:     tempState = new MenuState();           break;
            case PLAYER_STATE:   tempState = new PlayerState();         break;
            case SETTINGS_STATE: tempState = new SettingsState();       break;

            case CHAPTER_1:      tempState = new Chapter1();            break;
            case CHPATER_2:      tempState = new Chapter2();            break;
        }

        if(tempState == null) return;
        GameStateManager.currentState = tempState;
        Camera.position.reset();
        tempState.onCreate();
        pause = false;
    }

    private static void showChapter(int id) {
        timer.delay(3000);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Display.width, Display.height);
        Font.STANDARD_FONT.centerText(g, "Chapter " + id, 100);
        Display.update();
    }

    public void update() {
        if(quitConfirmation) { quitConfirmationState.update(); cutScene = false; }

        if(cutScene) {
            scene.update();
            pause = true;
            if(scene.isDone()) {
                scene.onDestroy();
                scene = null;
                cutScene = false;
                pause = false;
            }
        }

        if(currentState == null || pause) return;
        if(timer.done()) currentState.update();
        Camera.update();
        GUI.update();
    }

    public void draw() {
        if(quitConfirmation) quitConfirmationState.draw(g);
        if(cutScene) scene.draw(g);
        if(currentState == null || pause) return;
        currentState.draw(g);
        Camera.draw(g);
        GUI.draw(g);
    }

    public static void nextLevel() {
        try {
            MenuState.saveData = new SaveData(currentStateId + 1, MenuState.saveData.playerSave);
            MenuState.saveData.save();
            setCurrentState(currentStateId + 1);
        } catch (Exception e) {
            System.err.println("Could not find a chapter with: " + (currentStateId + 1));
            System.exit(-1);
        }
    }

    public static void requestCloseConfirmation() {
        if(quitConfirmation) return;
        System.out.println("*** Requesting Close Confirmation ***");
        pause = true;
        quitConfirmationState = new QuitConfirmation();
        quitConfirmation = true;
    }

    public static void showCutScene(Scene scene) {
        GameStateManager.scene = scene;
        scene.onCreate();
        pause = true;
        cutScene = true;
    }

    public static State getCurrentState() { return currentState; }
    public static void setPause(boolean pause) { GameStateManager.pause = pause; }
    public static void setQuitConfirmation(boolean quitConfirmation) { GameStateManager.quitConfirmation = quitConfirmation; }

}
