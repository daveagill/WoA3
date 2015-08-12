package week.of.awesome

import com.badlogic.gdx.Gdx
import scala.collection.breakOut


class TileMap(val tiles: Vector[Vector[Tiles.Tile]])

object TileMap {
  def loadLevel(path: String): TileMap = {
    val f = Gdx.files.internal(path)
    val br = scala.io.Source.fromInputStream(f.read())
    val tiles = br.getLines().map(tileLine => tileLine.map(parseTile).toVector).toVector
    new TileMap(tiles)
  }
  
  private def parseTile(t: Char): Tiles.Tile = t match {
    case ' ' => Tiles.Empty
    case '1' => Tiles.Block
    case _ => throw new RuntimeException("Attempt to parse invalid tile type: " + t)
  }
}