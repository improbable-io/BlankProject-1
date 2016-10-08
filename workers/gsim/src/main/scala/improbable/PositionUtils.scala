package improbable

import improbable.apps.LatLonPosition
import improbable.math.Vector3d


object PositionUtils {

  private val xFudgeFactor = 600 / 90
  private val yFudgeFactor = 400 / 90

  def convertToVector(latLonPosition: LatLonPosition): Vector3d = {
    Vector3d(xFudgeFactor * latLonPosition.lon, 0, yFudgeFactor * latLonPosition.lat)
  }

}

