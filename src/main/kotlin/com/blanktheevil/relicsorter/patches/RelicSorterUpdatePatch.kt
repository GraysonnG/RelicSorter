package com.blanktheevil.relicsorter.patches

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.relicsorter.fuzzyEquals
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic

@Suppress("unused")
@SpirePatch(clz = AbstractRelic::class, method = "update")
class RelicSorterUpdatePatch {
  companion object {
    private const val ANIMATION_SPEED = 30f

    @JvmStatic
    @SpirePostfixPatch
    fun run(relic: AbstractRelic) {
      with(RelicSorterFields) {
        if (isMoving.get(relic)) {
          if (!relic.targetX.fuzzyEquals(relic.currentX, 0.1f)) {
            relic.currentX = MathUtils.lerp(relic.currentX, relic.targetX, Gdx.graphics.deltaTime * ANIMATION_SPEED)
          } else {
            relic.currentX = relic.targetX
            isMoving.set(relic, false)
            AbstractDungeon.topPanel.adjustRelicHbs()
          }
        }
      }
    }
  }
}