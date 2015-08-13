package week.of.awesome

import com.badlogic.gdx.math.Vector2

class World(val tilemap: TileMap) {
  val player = new Player(new Vector2(3, 10), tilemap.getTile, tilemap.eval)
}