package week.of.awesome

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.Input

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
  
  override def getInputProcessor() = new InputAdapter() {
    override def keyDown(keycode: Int): Boolean = {
      keycode match {
        case Input.Keys.RIGHT => { world.player.moveRight(true) }
        case Input.Keys.LEFT => { world.player.moveLeft(true) }
        case Input.Keys.UP => { world.player.jump() }
        case _ =>
      }
      false
    }
    
    override def keyUp(keycode: Int): Boolean = {
      keycode match {
        case Input.Keys.RIGHT => { world.player.moveRight(false) }
        case Input.Keys.LEFT => { world.player.moveLeft(false) }
        case _ =>
      }
      false
    }
  }

  def update(dt: Float): Option[GameState] = {
    world.player.update(dt)
    return None
  }
  
  def render(dt: Float) = {
    renderer.drawWorld(world)
  }
}