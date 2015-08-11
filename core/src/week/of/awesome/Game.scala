package week.of.awesome

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Input

/**
 * @author David
 */
class Game extends ApplicationAdapter {
  val NanosPerSec = 1000000000L;
  val FixedTimestep = 1f / 60f;
  val FixedTimestepNanos = (FixedTimestep * NanosPerSec).toLong;
  
  var lastFrameTime = 0L
  var accumulatedTime = 0L
  
  val inputMultiplexer = new InputMultiplexer
  var renderer: BasicRenderer = _
  
  var currentState: GameState = _
  
  override def create() = {
    lastFrameTime = TimeUtils.nanoTime()
    
    renderer = new BasicRenderer(new GraphicsResources)
    
    Gdx.input.setInputProcessor(inputMultiplexer)
    inputMultiplexer.addProcessor(new InputAdapter() {
      override def keyDown(keycode: Int): Boolean = {
        if (keycode == Input.Keys.ESCAPE) {
          Gdx.app.exit()
        }
        return false
      }
    })
        
    currentState = new PlayGameState(renderer)
  }

  override def render() = {
    val currentTime = TimeUtils.nanoTime();
    accumulatedTime += (currentTime - lastFrameTime)
    lastFrameTime = currentTime
    
    var simulatedDt = 0f // accumulates how much time as been simulated, to sync the rendering to
    
    while (accumulatedTime >= FixedTimestepNanos) {
      val nextState = currentState.update(FixedTimestep).getOrElse(currentState)
      
      // if the game-state has changed then tear down the old state and setup the new one
      if (nextState ne currentState) {
        currentState.onExit()
        val inputProcessor = nextState.onEnter()
        
        // install the input processor
        inputMultiplexer.removeProcessor(0)
        inputMultiplexer.addProcessor(inputProcessor)
      }
      currentState = nextState
      
      accumulatedTime -= FixedTimestepNanos
      simulatedDt += FixedTimestep
    }
    
    renderer.beginFrame()
    currentState.render(simulatedDt)
    renderer.endFrame()
  }
  
  override def dispose() = {
    renderer.dispose()
  }
}