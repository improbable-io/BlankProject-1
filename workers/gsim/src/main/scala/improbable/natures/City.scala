package improbable.natures

import improbable.behaviours.CityBehaviour
import improbable.city.CityInfoComponent
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.papi.entity.EntityPrefab
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.util.LatLonPosition

object City extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[CityBehaviour]
  )

  def apply(position: LatLonPosition, name: String, demand: Int): NatureApplication = {
    application(
      states = Seq(CityInfoComponent(name, demand)),
      natures = Seq(BaseNature(EntityPrefab("City")), TransformNature(position.convertToVector()))
    )
  }
}