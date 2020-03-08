package com.blanktheevil.relicsorter.patches

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.relics.AbstractRelic

@SpirePatch(clz = AbstractRelic::class, method = SpirePatch.CLASS)
class RelicSorterFields {
  companion object {
    @JvmField
    var isMoving: SpireField<Boolean> = SpireField { false }
  }
}