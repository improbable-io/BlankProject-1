package improbable.natures

import improbable.behaviours.poacher.PoacherTradingBehaviour
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelibrary.transforms.TransformNature
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.poacher.{Life, PoacherInfoComponent}
import improbable.util.{GameSettings, LatLonPosition}

object Poacher extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[PoacherTradingBehaviour]
  )

  def apply(position: LatLonPosition, name: String): NatureApplication = {
    application(
      states = Seq(PoacherInfoComponent("Poachers: " + name, GameSettings.initialPoacherActivity, Life.Living)),
      natures = Seq(BaseNature(GameSettings.poacherPrefab, isPhysical = false), TransformNature(position.convertToVector()))
    )
  }
}