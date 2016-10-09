package improbable.behaviours.player

import improbable.apps.StartOfTurnMoney
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.player.PlayerInfoComponentWriter
import improbable.util.GameSettings

class TrackMoneyBehaviour(entity: Entity, world: World, logger: Logger, playerInfoComponentWriter: PlayerInfoComponentWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case StartOfTurnMoney =>
        updateBank()
    }

  }

  def updateBank(): Unit = {
    playerInfoComponentWriter.update.money(playerInfoComponentWriter.money + GameSettings.moneyPerTurn).finishAndSend()
  }
}
