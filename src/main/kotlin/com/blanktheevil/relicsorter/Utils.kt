package com.blanktheevil.relicsorter

import com.blanktheevil.relicsorter.patches.RelicSorterFields
import com.megacrit.cardcrawl.relics.AbstractRelic
import java.util.function.Consumer
import kotlin.math.abs

fun <T> doNothingConsumer(): Consumer<T> = Consumer { /* do nothing */ }
fun Float.fuzzyEquals(other: Float, threshold: Float): Boolean =
  abs(this.minus(other)) <= threshold

fun AbstractRelic.moveTo(relic: AbstractRelic): Boolean {
  if(!(RelicSorterFields.isMoving.get(this) || RelicSorterFields.isMoving.get(relic))) {
    this.targetX = relic.currentX
    RelicSorterFields.isMoving.set(this, true)
    return true
  }
  return false
}

fun AbstractRelic.moveTo(target: Float): Boolean {
  if (!RelicSorterFields.isMoving.get(this)) {
    this.targetX = target
    RelicSorterFields.isMoving.set(this, true)
    return true
  }
  return false
}