package improbable.apps

import improbable.logging.Logger
import improbable.natures.{City, Poacher}
import improbable.papi.world.AppWorld
import improbable.papi.worldapp.WorldApp

case class LatLonPosition(lat: Double, lon: Double)

class SimulationSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp {

  spawnCities()
  spawnPoachers()

  def spawnCities() = {
    appWorld.entities.spawnEntity(City(LatLonPosition(39.9, 116.4)))
  }

  def spawnPoachers() = {
    appWorld.entities.spawnEntity(Poacher(LatLonPosition(52, 0)))
  }
}