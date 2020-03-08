package com.blanktheevil.relicsorter

import java.util.function.Consumer
import kotlin.math.abs

fun <T> doNothingConsumer(): Consumer<T> = Consumer { /* do nothing */ }
fun Float.fuzzyEquals(other: Float, threshold: Float): Boolean =
  abs(this.minus(other)) <= threshold