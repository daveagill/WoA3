package week.of.awesome

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.InputAdapter

/**
 * @author David
 */
trait GameState {
  def onEnter() = ()
  def onExit() = ()
  def getInputProcessor(): InputProcessor = new InputAdapter
  def update(dt: Float): Option[GameState] 
  def render(dt: Float)
}