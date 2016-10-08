package improbable.natures

import improbable.behaviours.habitat.UpdatePopulationBehaviour
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.habitat.HabitatInfoComponent
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.util.{GameSettings, LatLonPosition}

object Habitat extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[UpdatePopulationBehaviour]
  )

  def apply(position: LatLonPosition, name: String, population: Int): NatureApplication = {
    application(
      states = Seq(HabitatInfoComponent(name, population)),
      natures = Seq(BaseNature(GameSettings.habitatPrefab, isPhysical = false), TransformNature(position.convertToVector()))
    )
  }
}
