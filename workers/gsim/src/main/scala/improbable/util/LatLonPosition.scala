package improbable.util

import improbable.math.Vector3d

class LatLonPosition(lat: Double, lon: Double) {

  private val xFudgeFactor = 6f
  private val yFudgeFactor = 6f
  private val xOffset = -5f
  private val yOffset = -105f

  def convertToVector(): Vector3d = {
    Vector3d(xFudgeFactor * lon + xOffset, 0, yFudgeFactor * lat + yOffset)
  }

  def distanceTo(position: LatLonPosition): Double = {
    Math.pow(lat - position.getLat, 2) + Math.pow(lon - position.getLon, 2)
  }

  def getLon: Double = this.lon

  def getLat: Double = this.lat

}