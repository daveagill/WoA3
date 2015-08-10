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
class PlayGameState(gfx: GraphicsResources) extends GameState {
  val img = gfx.newTexture("badlogic.jpg")
  
  def onEnter(): InputProcessor = {
    return new InputAdapter()
  }

  def onExit(): Unit = {
    
  }

  def update(dt: Float): Option[GameState] = {
    return None
  }
  
  def render(dt: Float): Unit = {
    gfx.batch.draw(img, 0, 0)
  }
}