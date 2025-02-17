package com.harmony.game.tiles;

import com.harmony.game.graphics.Sprite;
import com.harmony.game.object.Chest;
import com.harmony.game.object.Door;
import com.harmony.game.tiles.block.Block;
import com.harmony.game.tiles.block.NormalBlock;
import com.harmony.game.utils.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public class NormalTileMap extends TileMap {

    private ArrayList<Block> blocks;

    public NormalTileMap(String data, Sprite sprite, int width, int height, int tileWidth, int tileHeight, int tileColumns) {
        blocks = new ArrayList<>();

        String[] block = data.split(",");
        for(int i = 0; i < (width * height); i++) {
            double temp = Double.parseDouble(block[i].replaceAll("\\s+", ""));
            if(temp != 0) {
                if(temp == Block.CHEST_TILE_POSITION) new Chest(new Vector2f((i % width) * tileWidth, (i / height) * tileHeight), tileWidth, tileHeight);
                if(temp == Block.LEVEL_DOOR_POSITION) new Door (new Vector2f((i % width) * tileWidth, (i / height) * tileHeight), tileWidth, tileHeight);

                blocks.add(new NormalBlock(sprite.getSprite((int) (temp - 1)),
                        new Vector2f((i % width) * tileWidth, (i / height) * tileHeight), tileWidth, tileHeight));
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        for(int i = 0; i < blocks.size(); i++) {
           blocks.get(i).render(g, 0);
        }
    }
}
