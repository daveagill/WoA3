package week.of.awesome

/**
 * @author david
 */
object Tiles {
  sealed trait Tile
  case object Empty extends Tile
  case object Block extends Tile
}