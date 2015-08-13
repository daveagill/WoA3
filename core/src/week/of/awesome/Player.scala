package week.of.awesome

import com.badlogic.gdx.math.Vector2

/**
 * @author David
 */
object Player {
  val CollisionThreshold = 0.1f
  val StepThreshold = 0.5f
  val JumpSpeed = 11f
  val Gravity = 40f
}

class Player(
    startPosition: Vector2,
    mapEval: (Int, Int) => Tiles.Tile) {
  
  import Player._
  
  var position = startPosition
  
  private var movingRight = false
  private var movingLeft = false
  private var verticalVelocity = 0f
  
  def update(dt: Float) = {
    // apply gravity
    verticalVelocity -= (Gravity * dt)
    
    val velocity = {
      val HorizontalSpeed = 5f
      val rightSpeed = if (movingRight) HorizontalSpeed else 0f
      val leftSpeed = if (movingLeft) HorizontalSpeed else 0f
      new Vector2(rightSpeed - leftSpeed, verticalVelocity)
    }
    
    val oldPosition = position.cpy
    position.add(velocity.scl(dt))

    // query for surrounding tiles
    val (leftX, midX, rightX) = (position.x+CollisionThreshold, position.x+0.5f, position.x+1-CollisionThreshold)
    val (mapLeftX, mapMidX, mapRightX) = (leftX.intValue, midX.intValue, rightX.intValue)
    val (mapFloorY, mapWallY) = ((position.y-0.5f).intValue, (position.y+0.5f).intValue)
    val leftFloorTile = mapEval(mapLeftX, mapFloorY)
    
    val rightFloorTile = mapEval(mapRightX, mapFloorY)
    val leftWallTile = mapEval(mapLeftX, mapWallY)
    val rightWallTile = mapEval(mapRightX, mapWallY)
    val lowRoadTile = mapEval(mapMidX, mapFloorY)
    val highRoadTile = mapEval(mapMidX, mapWallY)
    
    // work out xPercent for left/right tiles
    val leftXPercent = leftX - mapLeftX
    val midXPercent = midX - mapMidX
    val rightXPercent = rightX - mapRightX
    
    // sample the tiles to work out the height of the tile at this position
    val leftFloorSampleY = leftFloorTile.sampleHeight(leftXPercent)
    val rightFloorSampleY = rightFloorTile.sampleHeight(rightXPercent)
    val leftWallSampleY = leftWallTile.sampleHeight(leftXPercent)
    val rightWallSampleY = rightWallTile.sampleHeight(rightXPercent)
    val midLowRoadSampleY = lowRoadTile.sampleHeight(midXPercent)
    val midHighRoadSampleY = highRoadTile.sampleHeight(midXPercent)

    val approachingFloor = leftFloorSampleY >= 0 || rightFloorSampleY >= 0
    if (approachingFloor) { // then we are soon to land on something
      val floorHeight = math.max(midLowRoadSampleY + mapFloorY, midHighRoadSampleY + mapWallY)

      // see if we've collided with the either the low-road or the high-road
      if (position.y <= floorHeight) {
        verticalVelocity = 0f         // stop falling
        position.y = floorHeight  // snap to floor
      }
    }
    
    val atLeftWall = leftWallSampleY >= 0
    val atRightWall = rightWallSampleY >= 0
    if (atLeftWall) {
      val wallHeight = leftWallSampleY + mapWallY
      // pass into the wall if it is below a threshold, the floor-snapping code will make us treat the wall like a step/slope
      if (wallHeight - position.y > StepThreshold) {
        position.x = if (position.x < oldPosition.x) oldPosition.x else position.x
      }
    }
    if (atRightWall) {
      val wallHeight = rightWallSampleY + mapWallY
      // pass into the wall if it is below a threshold, the floor-snapping code will make us treat the wall like a step/slope
      if (wallHeight - position.y > StepThreshold) {
        position.x = if (position.x > oldPosition.x) oldPosition.x else position.x
      }
    }


  }
  
  def moveRight(value: Boolean) = movingRight = value
  def moveLeft(value: Boolean) = movingLeft = value
  def jump() = verticalVelocity = if (isGrounded) JumpSpeed else verticalVelocity
    
  private def isGrounded = verticalVelocity == 0
}