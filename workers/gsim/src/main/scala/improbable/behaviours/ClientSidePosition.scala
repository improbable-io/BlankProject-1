package improbable.behaviours

import improbable.corelibrary.transforms.TransformInterface
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

class ClientSidePosition(entity: Entity, world: World, logger: Logger, transformInterface: TransformInterface) extends EntityBehaviour {

  override def onReady(): Unit = {
    transformInterface.delegatePhysicsToClientOwner()
  }
}
