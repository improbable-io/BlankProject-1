package improbable.behaviours.poacher

import improbable.apps.Expenditure
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.poacher.PoacherInfoComponentWriter

class UpdateActivityBehaviour(entity: Entity, world: World, logger: Logger, poacherInfoComponentWriter: PoacherInfoComponentWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case Expenditure(amount) =>
        reduceActivity(amount)
    }
  }

  def reduceActivity(amount: Int): Unit = {
    val newActivity = poacherInfoComponentWriter.activity - amount
    poacherInfoComponentWriter.update.activity(newActivity).finishAndSend()
  }
}
