package week.of.awesome

import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

/**
 * @author David
 */
class GraphicsResources extends Disposable {
  val gl = Gdx.gl
  val batch = new SpriteBatch
  
  val toDispose = scala.collection.mutable.Buffer[Disposable]()
  val textureCache = scala.collection.mutable.Map[String, Texture]()
  
  gl.glClearColor(0, 0, 1, 1);
  
  def beginFrame() = {
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
  }
  
  def endFrame() = {
    batch.end()
  }
  
  def newTexture(path: String): Texture = {
    val t = textureCache.get(path)
    t match {
      case Some(tex) => tex
      case None => {
        val tex = new Texture(path)
        toDispose += tex
        textureCache.put(path, tex)
        return tex
      }
    }
  }
  
  def dispose() = {
    batch.dispose()
    toDispose.foreach(_.dispose())
  }
}