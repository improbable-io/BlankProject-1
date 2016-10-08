package improbable.natures

import improbable.DelegateLocalPlayerCheckState
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelib.util.EntityOwner
import improbable.corelibrary.transforms.TransformNature
import improbable.papi.engine.EngineId
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.player.LocalPlayerCheck

object Player extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[DelegateLocalPlayerCheckState]
  )

  def apply(clientId: EngineId): NatureApplication = {
    application(
      states = Seq(EntityOwner(Some(clientId)), LocalPlayerCheck()),
      natures = Seq(BaseNature(EntityPrefab("Player")), TransformNature())
    )
  }
}