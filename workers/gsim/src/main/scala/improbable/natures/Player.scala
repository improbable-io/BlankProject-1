package improbable.natures

import improbable.behaviours.player.{DelegateToPlayer, PlayerControlsBehaviour, TrackMoneyBehaviour}
import improbable.corelib.natures.{BaseNature, NatureApplication, NatureDescription}
import improbable.corelib.util.EntityOwner
import improbable.corelibrary.transforms.TransformNature
import improbable.math.Vector3d
import improbable.papi.engine.EngineId
import improbable.papi.entity.behaviour.EntityBehaviourDescriptor
import improbable.player.{LocalPlayerCheck, PlayerControls, PlayerInfoComponent}
import improbable.util.GameSettings

object Player extends NatureDescription {

  override val dependencies = Set[NatureDescription](BaseNature, TransformNature)

  override def activeBehaviours: Set[EntityBehaviourDescriptor] = Set(
    descriptorOf[DelegateToPlayer],
    descriptorOf[PlayerControlsBehaviour],
    descriptorOf[TrackMoneyBehaviour]
  )

  def apply(clientId: EngineId): NatureApplication = {
    application(
      states = Seq(EntityOwner(Some(clientId)), LocalPlayerCheck(), PlayerControls(), PlayerInfoComponent(GameSettings.initialMoney)),
      natures = Seq(BaseNature(GameSettings.playerPrefab), TransformNature(Vector3d.zero))
    )
  }
}