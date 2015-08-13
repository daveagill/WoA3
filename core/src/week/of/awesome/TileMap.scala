package week.of.awesome

import com.badlogic.gdx.Gdx
import scala.collection.breakOut


class TileMap(val tiles: Vector[Vector[Tiles.Tile]]) {
  
  def getTile(x: Int, y: Int): Tiles.Tile = {
    if (x < 0 || y < 0) return Tiles.Empty
    if (y >= tiles.length || x >= tiles(y).length) return Tiles.Empty
    tiles(y)(x)
  }
  
  def eval(x: Int, y: Int, minXPercent: Float, maxXPercent: Float): Float = {
    /*val atTile = getTile(x, y)
    val belowTile = getTile(x, y-1)
    val atSample= atTile.sampleHeight(minXPercent, maxXPercent)
    val belowSample = belowTile.sampleHeight(minXPercent, maxXPercent)
    if (atSample < 0 && belowSample < 0) { return -1 }
    math.max(atSample+y, belowSample+y-1)*/
    
        var currentY = y
    while (currentY >= 0) {
      val tile = getTile(x, currentY)
      val sample = tile.sampleHeight(minXPercent, maxXPercent)
      if (sample >= 0) {
        return currentY + sample
      }
      currentY -= 1
    }
    -1
  }
}

object TileMap {
  def loadLevel(path: String): TileMap = {
    val f = Gdx.files.internal(path)
    val br = scala.io.Source.fromInputStream(f.read())
    val tiles = br.getLines().map(tileLine => tileLine.map(parseTile).toVector).toVector.reverse
    new TileMap(tiles)
  }
  
  private def parseTile(t: Char): Tiles.Tile = t match {
    case ' ' => Tiles.Empty
    case 'x' => Tiles.Block
    case '/' => Tiles.RightSlope
    case '\\' => Tiles.LeftSlope
    case _ => throw new RuntimeException("Attempt to parse invalid tile type: " + t)
  }
}