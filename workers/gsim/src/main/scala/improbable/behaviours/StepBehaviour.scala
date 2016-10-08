package improbable.behaviours

import improbable.apps.SimulationSpawner
import improbable.corelib.util.EntityOwnerDelegation.entityOwnerDelegation
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor
import improbable.player.PlayerControls

case object StepRequest extends CustomMsg

class StepBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {

  private val stepWatcher = entity.watch[PlayerControls]

  override def onReady(): Unit = {
    entity.delegateStateToOwner[PlayerControls]

    stepWatcher.onStep {
      _ => world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, StepRequest)
    }
  }
}
