package improbable.util

import improbable.math.Vector3d

class LatLonPosition(lat: Double, lon: Double) {

  private val xFudgeFactor = 600 / 90
  private val yFudgeFactor = 400 / 90

  def convertToVector(): Vector3d = {
    Vector3d(xFudgeFactor * lon, 0, yFudgeFactor * lat)
  }

  def distanceTo(position: LatLonPosition): Double = {
    Math.pow(lat - position.getLat, 2) + Math.pow(lon - position.getLon, 2)
  }

  def getLon: Double = this.lon

  def getLat: Double = this.lat

}