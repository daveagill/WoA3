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
  val toDispose = scala.collection.mutable.Buffer[Disposable]()
  val textureCache = scala.collection.mutable.Map[String, Texture]()
  
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
    toDispose.foreach(_.dispose())
  }
}