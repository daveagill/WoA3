package week.of.awesome

/**
 * @author david
 */
object Tiles {
  sealed trait Tile {
    def sampleHeight(xPercent: Float): Float
  }
  
  case object Empty extends Tile {
    def sampleHeight(xPercent: Float) = -1
  }
  
  case object Block extends Tile {
    def sampleHeight(xPercent: Float) = 1f
  }
}