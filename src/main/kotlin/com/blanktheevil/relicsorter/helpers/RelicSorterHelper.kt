package com.blanktheevil.relicsorter.helpers

import com.blanktheevil.relicsorter.patches.RelicSorterFields
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class RelicSorterHelper {
  companion object {
    fun canMoveRelics(): Boolean {
      AbstractDungeon.player.relics.forEach {
        return !RelicSorterFields.isMoving.get(it)
      }

      return false
    }
  }
}