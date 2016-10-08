package improbable.natures

import improbable.behaviours.{DelegateToPlayer, StepBehaviour}
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelib.util.EntityOwner
import improbable.corelibrary.transforms.TransformNature
import improbable.math.Vector3d
import improbable.papi.engine.EngineId
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.player.{LocalPlayerCheck, PlayerControls}

object Player extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[DelegateToPlayer],
    descriptorOf[StepBehaviour]
  )

  def apply(clientId: EngineId): NatureApplication = {
    application(
      states = Seq(EntityOwner(Some(clientId)), LocalPlayerCheck(), PlayerControls()),
      natures = Seq(BaseNature(EntityPrefab("Player")), TransformNature(Vector3d(0, 5, 0)))
    )
  }
}