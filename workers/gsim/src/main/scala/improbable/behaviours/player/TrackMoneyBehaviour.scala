package improbable.behaviours.player

import improbable.apps.InvestmentInPlayer
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.player.PlayerInfoComponentWriter
import improbable.util.GameSettings

class TrackMoneyBehaviour(entity: Entity, world: World, logger: Logger, playerInfoComponentWriter: PlayerInfoComponentWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case InvestmentInPlayer(amount) =>
        updateBank(amount)
    }
  }

  def updateBank(amount: Int): Unit = {
    playerInfoComponentWriter.update.money(playerInfoComponentWriter.money + amount).finishAndSend()
  }
}
