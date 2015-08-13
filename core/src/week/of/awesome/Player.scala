package week.of.awesome

import com.badlogic.gdx.math.Vector2

/**
 * @author David
 */
object Player {
  val Width = 0.8f;
  val StepThreshold = 0.5f
  val HorizontalSpeed = 2f
  val JumpSpeed = 11f
  val Gravity = 40f
}

class Player(
    startPosition: Vector2,
    mapEval: (Int, Int) => Tiles.Tile,
    mapEval2: (Int, Int, Float, Float) => Float) {
  
  import Player._
  
  var position = startPosition
  
  private var movingRight = false
  private var movingLeft = false
  private var isJumping = false
  private var verticalVelocity = 0f
  private var isGrounded = false
  
  class MapData(position: Vector2) {
    val (leftX, midX, rightX) = (position.x+0.5f-Width/2f, position.x+0.5f, position.x+0.5f+Width/2f)
    val (mapLeftX, mapMidX, mapRightX) = (leftX.intValue, midX.intValue, if (rightX.isWhole()) rightX.intValue-1 else rightX.intValue())
    val (mapFloorY, mapY) = ((position.y-0.5f).intValue, (position.y+0.5f).intValue)
    
    // xPercents for left/mid/right tiles
    val leftXPercent = leftX - mapLeftX
    val midXPercent = midX - mapMidX
    val rightXPercent = rightX - rightX.intValue
  }
  
  def update(dt: Float) = {
    // apply gravity
    verticalVelocity -= (Gravity * dt)
    
    // apply jumping velocity
    verticalVelocity = if (isGrounded && isJumping) JumpSpeed else verticalVelocity
    
    val velocity = {
      val rightSpeed = if (movingRight) HorizontalSpeed else 0f
      val leftSpeed = if (movingLeft) HorizontalSpeed else 0f
      new Vector2(rightSpeed - leftSpeed, verticalVelocity)
    }
    
    val oldPosition = position.cpy
    position.add(velocity.scl(dt))
    
    // must resolve wall collisions first, otherwise we might step up a wall
    resolveWallCollisions(oldPosition, position)
    resolveFloorCollisions(oldPosition, position)
  }
  
  def moveRight(value: Boolean) = movingRight = value
  def moveLeft(value: Boolean) = movingLeft = value
  def jumping(value: Boolean) = isJumping = value
    
  def resolveWallCollisions(oldPosition: Vector2, newPosition: Vector2) = {
    val bounds = new MapData(newPosition)
    import bounds._
    
    val closestAllowableDistanceToWall = 0.5f+Width/2
    
        val leftY = mapEval2(mapLeftX, mapY, leftXPercent, 1f)
    val rightY = mapEval2(mapRightX, mapY, 0f, rightXPercent)
    val midY = mapEval2(mapMidX, mapY, midXPercent, midXPercent)
    
    // if there is neither a high-road nor a low-road then it's a cliff - don't fall off cliffs
    val cliffHanging = midY <= mapFloorY
    val floorHeight = {
      if (cliffHanging) { // need to use the left/right corners to set the height, rather than the middle
        math.min(mapY, math.max(leftY, rightY))
      }
      else midY
    }
    
    // sample the tiles
    val leftWallSampleY = mapEval2(mapLeftX, mapY, leftXPercent, leftXPercent)
    val rightWallSampleY = mapEval2(mapRightX, mapY, rightXPercent, rightXPercent)
    val midSampleY =
      //math.max(
        mapEval2(mapMidX, mapY, midXPercent, midXPercent)//,
        //if (isGrounded) mapY+0 else -1)

    val atLeftWall = leftWallSampleY > oldPosition.y
    val atRightWall = rightWallSampleY > oldPosition.y
    if (atLeftWall && leftWallSampleY - floorHeight >= StepThreshold) {
      println(leftWallSampleY + "  " + oldPosition.y +"   " + midSampleY + "  " + mapMidX + "  " + mapY + "  " + midXPercent + "  " + isGrounded)
      newPosition.x = math.max(mapLeftX+closestAllowableDistanceToWall, newPosition.x)
    }
    if (atRightWall) {
      
    if (atRightWall && rightWallSampleY - floorHeight >= StepThreshold) {
      println(rightWallSampleY + "  " + oldPosition.y +"   " + floorHeight + "  " + mapMidX + "  " + mapY + "  " + midXPercent + "  " + isGrounded)
      newPosition.x = math.min(mapRightX-closestAllowableDistanceToWall, newPosition.x)
    }
    }
  }
  
  def resolveFloorCollisions(oldPosition: Vector2, newPosition: Vector2) = {
    val bounds = new MapData(new Vector2(newPosition.x, oldPosition.y)) // sample he map using the wall-resolved X position and the old Y position (to prevent tunelling)
    import bounds._
    
    val leftY = mapEval2(mapLeftX, mapY, leftXPercent, 1f)
    val rightY = mapEval2(mapRightX, mapY, 0f, rightXPercent)
    val midY = mapEval2(mapMidX, mapY, midXPercent, midXPercent)
    
    // if there is neither a high-road nor a low-road then it's a cliff - don't fall off cliffs
    val cliffHanging = midY <= mapFloorY
    val floorHeight = {
      if (cliffHanging) { // need to use the left/right corners to set the height, rather than the middle
        math.min(mapY, math.max(leftY, rightY))
      }
      else midY
    }
    
    val wasGrounded = isGrounded
    isGrounded = false

    val approachingFloor = verticalVelocity <= 0 && floorHeight >= mapFloorY
    if (approachingFloor) { // then we are soon to land on something
      // see if we need to snap to the ground
      val heightOffFloor = newPosition.y - floorHeight
      if (heightOffFloor <= 0 || (wasGrounded && heightOffFloor < StepThreshold)) {
        verticalVelocity = 0f        // stop falling
        newPosition.y = floorHeight  // snap to floor
        isGrounded = true
      }
    }
  }
}