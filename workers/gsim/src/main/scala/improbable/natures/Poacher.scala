package improbable.natures

import improbable.PositionUtils
import improbable.apps.LatLonPosition
import improbable.behaviours.PoacherTradingBehaviour
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor

object Poacher extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[PoacherTradingBehaviour]
  )

  def apply(position: LatLonPosition): NatureApplication = {
    application(
      states = Seq(),
      natures = Seq(BaseNature(EntityPrefab("Poacher")), TransformNature(PositionUtils.convertToVector(position)))
    )
  }
}