package week.of.awesome

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.OrthographicCamera

class BasicRenderer(val resources: GraphicsResources) extends Disposable {
  val gl = Gdx.gl
  val batch = new SpriteBatch
  
  var camera = new OrthographicCamera()
  
  def beginFrame() = {
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
  }
  
  def endFrame() = {
    batch.end()
  }
  
  def resizeViewport(width: Int, height: Int) = {
    camera.setToOrtho(false, width, height)
    camera.update()
  }
  
  def setBackgroundColour(r: Float, g: Float, b: Float, a: Float) = {
    gl.glClearColor(r, g, b, a)
  }
  
  def drawCenteredAtBase(t: Texture, pos: Vector2, width: Float, height: Float, flipX: Boolean) = {
    val actualWidth = if (flipX) -width else width
    batch.draw(t, pos.x - actualWidth/2, pos.y, actualWidth, height)
  }
  
  def dispose() = {
    batch.dispose()
    resources.dispose()
  }
}