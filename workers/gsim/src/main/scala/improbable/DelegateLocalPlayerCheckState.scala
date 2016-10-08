package improbable

import improbable.corelib.util.EntityOwnerDelegation.entityOwnerDelegation
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.player.LocalPlayerCheck


class DelegateLocalPlayerCheckState(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {

  override def onReady(): Unit = {
    entity.delegateStateToOwner[LocalPlayerCheck]
  }
}