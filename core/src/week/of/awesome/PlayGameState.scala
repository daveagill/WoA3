package week.of.awesome

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

/**
 * @author David
 */
class PlayGameState(basicRenderer: BasicRenderer) extends GameState {
  val renderer = new WorldRenderer(basicRenderer)
  
  val world = new World(
        TileMap.loadLevel("worldmap.txt"))
  
  override def onEnter() = {
    basicRenderer.setBackgroundColour(1, 1, 1, 1)
  }

  def update(dt: Float): Option[GameState] = {
    return None
  }
  
  def render(dt: Float) = {
    renderer.drawWorld(world)
  }
}