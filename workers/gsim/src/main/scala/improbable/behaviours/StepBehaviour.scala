package improbable.behaviours

import improbable.apps.{SimulationSpawner, StepRequest}
import improbable.corelib.util.EntityOwnerDelegation.entityOwnerDelegation
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.worldapp.WorldAppDescriptor
import improbable.player.Step

class StepBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {

  private val stepWatcher = entity.watch[Step]

  override def onReady(): Unit = {
    entity.delegateStateToOwner[Step]

    stepWatcher.onTrigger {
      _ => world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, StepRequest)
    }

  }
}
