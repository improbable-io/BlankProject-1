package improbable.natures

import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.habitat.PopulationComponent
import improbable.math.Vector3d
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Habitat extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(

  )

  def apply(position: Vector3d = Vector3d.zero): NatureApplication = {
    application(
      states = Seq(PopulationComponent(100)),
      natures = Seq(BaseNature(EntityPrefab("Habitat")), TransformNature(position))
    )
  }
}
