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
    
    // work out how the height of the player has changed from old position to new
    val oldFloorY = projectToFloor(oldPosition, true)
    val newFloorY = projectToFloor(position, false)

    // resolve wall collision
    val wallCollision = position.y < newFloorY && math.abs(newFloorY - oldFloorY) > StepThreshold
    val wallCorrectedFloorY = if (wallCollision) {
      val wasLeftWall = position.x < oldPosition.x
      if (wasLeftWall) {
        position.x = position.x.intValue - (0.5f+Width/2)
      }
      else {
        position.x = position.x.intValue + (0.5f+Width/2)
      }
      oldFloorY
    }
    else newFloorY
    
    // resolve ground collision
    isGrounded = position.y <= wallCorrectedFloorY && verticalVelocity <= 0
    if (isGrounded) {
      verticalVelocity = 0f                // stop falling
      position.y = wallCorrectedFloorY     // snap to floor
    }
    
    println(oldPosition.x)
  }
  
  def moveRight(value: Boolean) = movingRight = value
  def moveLeft(value: Boolean) = movingLeft = value
  def jumping(value: Boolean) = isJumping = value

  def projectToFloor(position: Vector2, b:Boolean): Float = {
    val (leftX, midX, rightX) = (position.x+(0.5f-Width/2f), position.x+0.5f, position.x+(0.5f+Width/2f))
    val (mapLeftX, mapMidX, mapRightX) = (leftX.intValue, midX.intValue, if (rightX.isWhole()) rightX.intValue-1 else rightX.intValue())
    val mapY = (position.y + 0.5f).intValue
    
    // xPercents for left/mid/right tiles
    val leftXPercent = leftX - mapLeftX
    val leftMidXPercent = midX - mapLeftX
    val midXPercent = midX - mapMidX
    val rightXPercent = rightX - rightX.intValue
    val rightMidXPercent = midX - rightX.intValue
    
    println(leftX + "  " + mapLeftX)
    
    val leftY = mapEval2(mapLeftX, mapY, leftXPercent, leftMidXPercent)
    val rightY = mapEval2(mapRightX, mapY, rightMidXPercent, rightXPercent)
    val midY = mapEval2(mapMidX, mapY, midXPercent, midXPercent)
    
    val maxY = math.max(leftY, rightY)
    
    val cliffHanging = maxY - midY > StepThreshold
    if (cliffHanging) maxY.intValue else midY
  }
}