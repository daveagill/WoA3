package week.of.awesome

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

/**
 * @author David
 */
object WorldRenderer {
  val TileWidth = 25f
}

class WorldRenderer(renderer: BasicRenderer) {
  import WorldRenderer._
  
  val blockTex = renderer.resources.newTexture("block.png")
  
  def drawWorld(world: World) = {
    for ((row, y) <- world.tilemap.tiles.zipWithIndex) {
      for ((tile, x) <- row.zipWithIndex) {
        drawTile(tile, x, y)
      }
    }
  }
  
  def drawTile(tile: Tiles.Tile, x: Int, y: Int) = {
    selectTileTexture(tile).foreach {
      val tilePos = new Vector2(x * TileWidth, y * TileWidth)
      renderer.drawCenteredAtBase(_, tilePos, TileWidth, TileWidth, false)
    }
  }
  
  def selectTileTexture(tile: Tiles.Tile) : Option[Texture] = tile match {
    case Tiles.Block => Some(blockTex)
    case Tiles.Empty => None
  }
}