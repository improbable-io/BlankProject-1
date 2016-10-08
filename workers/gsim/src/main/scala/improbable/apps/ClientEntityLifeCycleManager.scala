package improbable.apps

import improbable.logging.Logger
import improbable.natures.Player
import improbable.papi.EntityId
import improbable.papi.engine.EngineId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.{EngineConnected, EngineDisconnected}
import improbable.papi.worldapp.WorldApp
import improbable.unity.fabric.engine.EnginePlatform

class ClientEntityLifeCycleManager(appWorld: AppWorld, logger: Logger) extends WorldApp {

  logger.info("somehting")
  private var clientIdToEntityIdMap = Map[EngineId, EntityId]()

  appWorld.messaging.subscribe {
    case msg: EngineConnected =>
      workerConnected(msg)
    case msg: EngineDisconnected =>
      workerDisconnected(msg)
  }

  private def workerConnected(msg: EngineConnected): Unit = {
    msg match {
      case EngineConnected(clientId, EnginePlatform.UNITY_CLIENT_ENGINE, _) =>
        spawnPlayer(clientId)
      case _ =>
    }
  }

  private def spawnPlayer(clientId: EngineId): Unit = {
    val playerEntityId = appWorld.entities.spawnEntity(Player(clientId))
    logger.info(s"ClientId $clientId connected. Spawning a Player with entityId $playerEntityId")
    clientIdToEntityIdMap += clientId -> playerEntityId
  }

  private def workerDisconnected(msg: EngineDisconnected): Unit = {
    msg match {
      case EngineDisconnected(clientId, EnginePlatform.UNITY_CLIENT_ENGINE) =>
        deletePlayer(clientId)
      case _ =>
    }
  }

  private def deletePlayer(clientId: EngineId) = {
    clientIdToEntityIdMap.get(clientId) match {
      case Some(entityId) =>
        appWorld.entities.destroyEntity(entityId)
        logger.info(s"ClientId $clientId has disconnect. Destroyed entity $entityId")
        clientIdToEntityIdMap -= clientId
      case None =>
        logger.warn(s"User disconnected but could not find entity id for player: $clientId")
    }
  }

}