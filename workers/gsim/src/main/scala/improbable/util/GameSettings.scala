package improbable.util

import improbable.papi.entity.EntityPrefab

object GameSettings {
  val moneyPerTurn = 5000
  val initialMoney = 25000
  val playerPrefab = EntityPrefab("Player")
  val cityPrefab = EntityPrefab("City")
  val habitatPrefab = EntityPrefab("Habitat")
  val poacherPrefab = EntityPrefab("Poacher")
  val initialPoacherActivity = 500
}