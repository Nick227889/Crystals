package com.harmony.game.graphics;

import com.harmony.game.Game;
import com.harmony.game.entity.Entity;
import com.harmony.game.item.drops.Drop;
import com.harmony.game.object.GameObject;
import com.harmony.game.physics.collision.BoxCollider;
import com.harmony.game.tiles.block.Block;
import com.harmony.game.utils.Vector2f;

import java.awt.*;

public class Camera {

    private static BoxCollider collisionCam;
    private static boolean created = false;

    public static Vector2f position = new Vector2f();
    public static Vector2f defaultPosition = new Vector2f();

    public Camera(BoxCollider collisionCam) {
        Camera.collisionCam = collisionCam;
        created = true;
    }

    public static void update() {}

    public static boolean shouldHandleTile(Block block) {
        if (!created) {
            System.err.println("Crystals: Must create the camera first before asking to show tile");
            return false;
        }

        if (block.getAbsPosition().getWorldPosition().x + block.getWidth() >= collisionCam.getOffset().x)
            if (block.getAbsPosition().getWorldPosition().x + block.getWidth() <= Display.width - collisionCam.getOffset().x + block.getWidth())
                if (block.getAbsPosition().getWorldPosition().y + block.getHeight() >= collisionCam.getOffset().y)
                    return block.getAbsPosition().getWorldPosition().y <= Display.height - collisionCam.getOffset().y;

        return false;
    }

    public static boolean shouldHandleGameObject(GameObject object) {
        if (!created) {
            System.err.println("Crystals: Must create the camera first before asking to show game object");
            return false;
        }

        if (object.position.x >= (position.x + collisionCam.getOffset().x) - object.getWidth())
            if (object.position.x <= position.x + collisionCam.getWidth() + collisionCam.getOffset().x)
                if (object.position.y >= (position.y + collisionCam.getOffset().y) - object.getHeight())
                    if (object.position.y <= position.y + collisionCam.getHeight() + collisionCam.getOffset().y)
                        return true;

        return false;
    }

    public static boolean shouldHandleEntity(Entity entity) {
        if (!created) {
            System.err.println("Crystals: Must create the camera first before asking to show entity");
            return false;
        }

        if (entity.position.x >= (position.x + collisionCam.getOffset().x) - entity.width)
            if (entity.position.x <= position.x + collisionCam.getWidth() + collisionCam.getOffset().x)
                if (entity.position.y >= (position.y + collisionCam.getOffset().y) - entity.height)
                    if (entity.position.y <= position.y + collisionCam.getHeight() + collisionCam.getOffset().y)
                        return true;

        return false;
    }

    public static boolean shouldHandleDrop(Drop drop) {
        if(!created) {
            System.err.println("Crystals: Must create the camera first before asking to show drop");
            return false;
        }

        if(drop.position.getWorldPosition().x + drop.rectangle.width >= collisionCam.getOffset().x)
            if(drop.position.getWorldPosition().x <= collisionCam.getOffset().x + Display.width)
                if(drop.position.getWorldPosition().y + drop.rectangle.height >= collisionCam.getOffset().y)
                    if(drop.position.getWorldPosition().y <= collisionCam.getOffset().y + Display.height)
                        return true;

        return false;
    }

    public static void draw(Graphics2D g) {
        if (!created) return;
        if (Game.debugMode) {
            g.setColor(Color.YELLOW);
            g.drawRect((int) collisionCam.getOffset().x, (int) collisionCam.getOffset().y,
                    collisionCam.getWidth(), collisionCam.getHeight());
        }
    }

    public static void reset() { Camera.position = new Vector2f(Camera.defaultPosition); }

    public static void setDefaultPosition() { Camera.defaultPosition = new Vector2f(Camera.position); }
}