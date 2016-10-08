package improbable.behaviours.player

import improbable.apps.SimulationSpawner
import improbable.corelib.util.EntityOwnerDelegation.entityOwnerDelegation
import improbable.logging.Logger
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor
import improbable.player.{PlayerControls, StepData}

case class StepRequest(playerId: EntityId, step: StepData) extends CustomMsg

class PlayerControlsBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {

  private val stepWatcher = entity.watch[PlayerControls]

  override def onReady(): Unit = {
    entity.delegateStateToOwner[PlayerControls]

    stepWatcher.onStep {
      stepData => world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, StepRequest(entity.entityId, stepData))
    }
  }
}
