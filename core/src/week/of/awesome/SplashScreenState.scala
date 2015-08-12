package week.of.awesome

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.graphics.OrthographicCamera

/**
 * @author David
 */
object SplashScreenState {
  val FadeDuration = 0.5f
  val SplashDuration = 3f
}

class SplashScreenState(gfx: BasicRenderer, nextState: GameState) extends GameState {
  var fadeIn: Boolean = _
  var fadeOut: Boolean = _
  var fadePercent: Float = _
  var showSplashTimeout: Float = _
  
  val splashScreen = gfx.resources.newTexture("splash.png")

  override def onEnter() = {
    fadeIn = true
    fadeOut = false
    fadePercent = 0f
    showSplashTimeout = 1f
    gfx.setBackgroundColour(0, 0, 0, 1)
  }
  
  override def onExit() = {
    // change down to the game resolution
    Gdx.graphics.setDisplayMode(640, 400, false)
  }
  
  override def getInputProcessor() = new InputAdapter() {
    override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
      fadePercent = -1f
      fadeOut = true
      fadeIn = false
      false
    }
  }
  
  def update(dt: Float): Option[GameState] = {
    if (fadeIn) {
      fadePercent = math.min(1f, fadePercent + (dt / SplashScreenState.FadeDuration))
      if (fadePercent >= 1f) {
        fadeIn = false
        showSplashTimeout = 1f
      }
    }
    else if (fadeOut) {
      fadePercent = math.max(0f, fadePercent - (dt / SplashScreenState.FadeDuration))
      if (fadePercent <= 0f) {
        fadeOut = false
        return Some(nextState)
      }
    }
    else {
      showSplashTimeout -= (dt / SplashScreenState.SplashDuration)
      fadeOut = showSplashTimeout <= 0
    }
    
    None
  }

  def render(dt: Float): Unit = {
    gfx.batch.setColor(1f, 1f, 1f, fadePercent)
    gfx.batch.draw(splashScreen, 0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    gfx.batch.setColor(1f, 1f, 1f, 1f)
  }
}