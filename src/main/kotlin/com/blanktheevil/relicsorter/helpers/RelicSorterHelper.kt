package com.blanktheevil.relicsorter.helpers

import com.blanktheevil.relicsorter.patches.RelicSorterFields
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic

class RelicSorterHelper {
  companion object {
    fun canMoveRelics(): Boolean {
      AbstractDungeon.player.relics.forEach {
        return !RelicSorterFields.isMoving.get(it)
      }

      return false
    }

    fun moveRelic(relicA: AbstractRelic, relicB: AbstractRelic): Boolean {
      with (RelicSorterFields.isMoving) {
        if (!(get(relicA) || get(relicB))) {
          relicA.targetX = relicB.currentX
          set(relicA, true)
          return true
        }
      }
      return false
    }

    fun moveRelic(relicA: AbstractRelic, currentX: Float): Boolean {
      with(RelicSorterFields.isMoving) {
        if (!get(relicA)) {
          relicA.targetX = currentX
          set(relicA, true)
          return true
        }
      }
      return false
    }
  }
}