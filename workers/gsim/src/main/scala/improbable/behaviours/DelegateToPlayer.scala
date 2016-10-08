package improbable.behaviours

import improbable.corelib.util.EntityOwnerDelegation.entityOwnerDelegation
import improbable.corelibrary.transforms.TransformInterface
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.player.LocalPlayerCheck


class DelegateToPlayer(entity: Entity, world: World, logger: Logger, transformInterface: TransformInterface) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateStateToOwner[LocalPlayerCheck]
    transformInterface.delegatePhysicsToClientOwner
  }
}