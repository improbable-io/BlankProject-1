package improbable.apps

import improbable.logging.Logger
import improbable.natures.Player
import improbable.papi.EntityId
import improbable.papi.engine.EngineId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.{EngineConnected, EngineDisconnected}
import improbable.papi.worldapp.WorldApp
import improbable.unity.fabric.engine.EnginePlatform

class MapSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp{
  logger.info("Spawned map")
  appWorld.entities.spawnEntity(improbable.natures.Map())
}