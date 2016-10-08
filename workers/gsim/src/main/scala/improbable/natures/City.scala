package improbable.natures

import improbable.behaviours.city.UpdateDemandBehaviour
import improbable.city.{ArrowData, CityInfoComponent}
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.math.Vector3d
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.util.{GameSettings, LatLonPosition}

object City extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[UpdateDemandBehaviour]
  )

  def apply(position: LatLonPosition, name: String, demand: Int): NatureApplication = {
    application(
      states = Seq(CityInfoComponent(name, demand, ArrowData(Vector3d.zero, 0))),
      natures = Seq(BaseNature(GameSettings.cityPrefab), TransformNature(position.convertToVector()))
    )
  }
}