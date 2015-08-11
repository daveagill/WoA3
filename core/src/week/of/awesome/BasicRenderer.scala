package week.of.awesome

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.graphics.GL20

class BasicRenderer(val resources: GraphicsResources) extends Disposable {
  val gl = Gdx.gl
  val batch = new SpriteBatch
  
  gl.glClearColor(0, 0, 1, 1);
  
  def beginFrame() = {
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
  }
  
  def endFrame() = {
    batch.end()
  }
  
  def dispose() = {
    batch.dispose()
    resources.dispose()
  }
}