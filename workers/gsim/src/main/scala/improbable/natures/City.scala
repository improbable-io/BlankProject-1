package improbable.natures

import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.util.LatLonPosition

object City extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set()

  def apply(position: LatLonPosition): NatureApplication = {
    application(
      states = Seq(),
      natures = Seq(BaseNature(EntityPrefab("City")), TransformNature(position.convertToVector()))
    )
  }
}