package improbable.natures

import improbable.behaviours.city.UpdateDemandBehaviour
import improbable.city.CityInfoComponent
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.util.{GameSettings, LatLonPosition}

object City extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[UpdateDemandBehaviour]
  )

  def apply(position: LatLonPosition, name: String, demand: Int): NatureApplication = {
    application(
      states = Seq(CityInfoComponent(name, demand)),
      natures = Seq(BaseNature(GameSettings.cityPrefab), TransformNature(position.convertToVector()))
    )
  }
}