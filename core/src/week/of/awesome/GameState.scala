package week.of.awesome

import com.badlogic.gdx.InputProcessor

/**
 * @author David
 */
trait GameState {
  def onEnter(): InputProcessor
  def onExit()
  def update(dt: Float): Option[GameState] 
  def render(dt: Float)
}