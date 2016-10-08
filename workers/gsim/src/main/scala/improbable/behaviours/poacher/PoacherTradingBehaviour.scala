package improbable.behaviours.poacher

import improbable.apps.{SimulationSpawner, TriggerPoacher}
import improbable.behaviours.habitat.HabitatResponse
import improbable.logging.Logger
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor
import improbable.poacher.{Life, PoacherInfoComponent, PoacherInfoComponentWriter}
import improbable.util.GameSettings

case class PoacherResponse(poacherId: EntityId, killedElephants: Int) extends CustomMsg

case class PoachRequest(poacherId: EntityId, poacherDemand: Int) extends CustomMsg

case class PoacherDead(poacherId: EntityId) extends CustomMsg

class PoacherTradingBehaviour(entity: Entity, world: World, logger: Logger, poacherInfoComponentWriter: PoacherInfoComponentWriter) extends EntityBehaviour {

  val poacherInfo = entity.watch[PoacherInfoComponent]

  override def onReady(): Unit = {

    poacherInfo.bind.activity {
      activity =>
        if (activity < GameSettings.minimumPoacherActivity)
          poacherInfoComponentWriter.update.life(Life.Dead)
        world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, PoacherDead(entity.entityId))
    }

    world.messaging.onReceive {
      case msg@TriggerPoacher(habitatId, targetDemand) =>
        logger.info("trigger poacher received " + msg)
        poachFromHabitat(habitatId, targetDemand)
      case msg@HabitatResponse(killedElephants) =>
        logger.info("habitat response received " + msg)
        poacherInfoComponentWriter.update.activity(killedElephants).finishAndSend()
        world.messaging.sendToApp(
          WorldAppDescriptor.forClass[SimulationSpawner].name, PoacherResponse(entity.entityId, killedElephants)
        )
    }
  }

  def poachFromHabitat(habitatId: EntityId, targetDemand: Int): Unit = {
    val poachingActivity = poacherInfoComponentWriter.activity
    // essentially ignoreing targetDemand here
    val poachingDemand = Math.max(targetDemand, poachingActivity)
    world.messaging.sendToEntity(habitatId, PoachRequest(entity.entityId, poachingDemand))
    logger.info("sent poach request message")
  }
}
