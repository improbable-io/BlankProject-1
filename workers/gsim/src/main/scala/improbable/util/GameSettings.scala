package improbable.util

import improbable.papi.entity.EntityPrefab

object GameSettings {
  val moneyPerTurn = 1000
  val initialMoney = 25000
  val initialPoacherActivity = 500
  val minimumPoacherActivity = 10 //dies below this amount
  val reproductionRate = 0.1 //per turn
  val demandRandomisationSD = 0.1
  val playerPrefab = EntityPrefab("Player")
  val cityPrefab = EntityPrefab("City")
  val habitatPrefab = EntityPrefab("Habitat")
  val poacherPrefab = EntityPrefab("Poacher")
}