package week.of.awesome

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Matrix4

/**
 * @author David
 */
object WorldRenderer {
  val WorldScale = 25f
  
  val TileWidth = 1f
  
  val PlayerWidth = 0.8f
  val PlayerHeight = 1.2f
}

class WorldRenderer(renderer: BasicRenderer) {
  import WorldRenderer._
  
  val playerTex = renderer.resources.newTexture("block.png")
  
  val blockTex = renderer.resources.newTexture("block.png")
  
  def drawWorld(world: World) = {
    renderer.batch.setTransformMatrix(new Matrix4().scale(WorldScale, WorldScale, WorldScale).translate(TileWidth/2f, 0, 0))
    
    // draw the map
    for ((row, y) <- world.tilemap.tiles.zipWithIndex) {
      for ((tile, x) <- row.zipWithIndex) {
        drawTile(tile, x, y)
      }
    }
    
    // draw the character
    renderer.drawCenteredAtBase(playerTex, world.player.position, PlayerWidth, PlayerHeight, false)
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