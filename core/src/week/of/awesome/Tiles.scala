package week.of.awesome

/**
 * @author david
 */
object Tiles {
  sealed trait Tile {
    def sampleHeight(xPercent: Float): Float = sampleHeight(xPercent, xPercent)
    def sampleHeight(minXPercent: Float, maxXPercent: Float): Float = {
      if ((minXPercent < 0 || minXPercent > 1) || maxXPercent < 0 || maxXPercent > 1) -1 // sampling off the sides
      else doSampleHeight(math.max(0f, minXPercent), math.min(1f, maxXPercent))
    }
    def doSampleHeight(minXPercent: Float, maxXPercent: Float): Float
  }
  
  case object Empty extends Tile {
    def doSampleHeight(minXPercent: Float, maxXPercent: Float) = -1
  }
  
  case object Block extends Tile {
    def doSampleHeight(minXPercent: Float, maxXPercent: Float) = 1f
  }
  
  case object RightSlope extends Tile {
    def doSampleHeight(minXPercent: Float, maxXPercent: Float) = maxXPercent
  }
  
  case object LeftSlope extends Tile {
    def doSampleHeight(minXPercent: Float, maxXPercent: Float) = 1-minXPercent
  }
}