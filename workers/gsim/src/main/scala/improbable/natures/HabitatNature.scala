package improbable.natures

import improbable.PositionUtils
import improbable.apps.LatLonPosition
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.habitat.HabitatInfoComponent
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object HabitatNature extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(

  )

  def apply(position: LatLonPosition, name: String, population: Float): NatureApplication = {
    application(
      states = Seq(HabitatInfoComponent(name, population)),
      natures = Seq(BaseNature(EntityPrefab("Habitat")), TransformNature(PositionUtils.convertToVector(position)))
    )
  }
}
