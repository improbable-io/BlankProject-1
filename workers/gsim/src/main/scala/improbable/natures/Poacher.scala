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

  def apply(position: LatLonPosition): NatureApplication = {
    application(
      states = Seq(PoacherInfoComponent("Morrison", GameSettings.initialPoacherActivity, Life.Living)),
      natures = Seq(BaseNature(GameSettings.poacherPrefab), TransformNature(position.convertToVector()))
    )
  }
}