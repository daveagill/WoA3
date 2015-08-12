package week.of.awesome

import com.badlogic.gdx.math.Vector2

/**
 * @author David
 */
object Player {
  val CollisionThreshold = 0.1f
  val StepThreshold = 0.2f
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
    val velocity = {
      val HorizontalSpeed = 5f
      val rightSpeed = if (movingRight) HorizontalSpeed else 0f
      val leftSpeed = if (movingLeft) HorizontalSpeed else 0f
      new Vector2(rightSpeed - leftSpeed, verticalVelocity)
    }
    
    position.add(velocity.scl(dt))

    // query for surrounding tiles
    val (leftX, rightX) = (position.x+CollisionThreshold, position.x+1-CollisionThreshold)
    val (mapLeftX, mapRightX) = (leftX.intValue, rightX.intValue)
    val (mapFloorY, mapWallY) = ((position.y-0.5f).intValue, (position.y+0.5f).intValue)
    val leftFloorTile = mapEval(mapLeftX, mapFloorY)
    val rightFloorTile = mapEval(mapRightX, mapFloorY)
    val leftWallTile = mapEval(mapLeftX, mapWallY)
    val rightWallTile = mapEval(mapRightX, mapWallY)
    
    // work out xPercent for left/right tiles
    val leftXPercent = leftX - mapLeftX
    val rightXPercent = rightX - mapRightX
    
    // sample the tiles to work out the height of the tile at this position
    val leftFloorSampleY = leftFloorTile.sampleHeight(leftXPercent)
    val rightFloorSampleY = rightFloorTile.sampleHeight(rightXPercent)
    val leftWallSampleY = leftWallTile.sampleHeight(leftXPercent)
    val rightWallSampleY = rightWallTile.sampleHeight(rightXPercent)
    
    // apply gravity
    verticalVelocity -= (10f * dt)

    val approachingWall = leftWallSampleY >= 0 || rightWallSampleY >= 0
    if (approachingWall) {
      val wallCollisionSampleY = Seq(leftWallSampleY, rightWallSampleY).filter(_ >= 0).min
      
      val wallCollisionY = wallCollisionSampleY + mapWallY
      
      // if we are grounded and the wall collision is below a threshold then treat it like a step rather than a wall
      if (verticalVelocity == 0f && position.y - wallCollisionY < StepThreshold) {
        println("wallCollision   " + wallCollisionY + "   " + position.y)
        position.y = wallCollisionY // snap to floor
      }
      else { // otherwise we collide with the wall
        val isLeftWall = leftWallSampleY >= 0
        val isRightWall = rightWallSampleY >= 0
        
        if (isLeftWall) {
          position.x = mapLeftX+1-CollisionThreshold
          movingLeft = false
        }
        else if (isRightWall) {
          position.x = mapRightX-1f+CollisionThreshold
          movingRight = false
        }
      }
    }

    val approachingFloor = leftFloorSampleY >= 0 || rightFloorSampleY >= 0
    if (approachingFloor) { // then we are soon to land on something
      // collision height is the minimum of these (ignoring holes)
      val floorCollisionSampleY = Seq(leftFloorSampleY, rightFloorSampleY).filter(_ >= 0).min

      // include the tile's Y position to calculate the collision height in world-space
      val floorCollisionY = floorCollisionSampleY + mapFloorY

      // see if we've collided with the floor
      if (position.y <= floorCollisionY) {
        verticalVelocity = 0f         // stop falling
        position.y = floorCollisionY  // snap to floor
      }
    }
  }
  
  def moveRight(value: Boolean) = movingRight = value
  def moveLeft(value: Boolean) = movingLeft = value
  
}