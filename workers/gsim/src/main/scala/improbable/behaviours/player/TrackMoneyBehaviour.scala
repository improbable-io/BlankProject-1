package improbable.behaviours.player

import improbable.apps.StartOfTurnMoney
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.player.PlayerInfoWriter
import improbable.util.GameSettings

class TrackMoneyBehaviour(entity: Entity, world: World, logger: Logger, playerInfoWriter: PlayerInfoWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case StartOfTurnMoney =>
        updateBank()
    }

  }

  def updateBank(): Unit = {
    playerInfoWriter.update.money(playerInfoWriter.money + GameSettings.moneyPerTurn).finishAndSend()
  }
}
