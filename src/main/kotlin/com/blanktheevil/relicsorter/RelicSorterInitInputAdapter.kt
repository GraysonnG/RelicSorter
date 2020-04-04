package com.blanktheevil.relicsorter

import basemod.ModButton
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.blanktheevil.relicsorter.models.Config

class RelicSorterInitInputAdapter(private val keyBindingButton: ModButton) : InputAdapter() {
  override fun keyUp(keycode: Int): Boolean {
    RelicSorter.config.keyBind = keycode
    Config.save(RelicSorter.config)
    keyBindingButton.parent.waitingOnEvent = false
    Gdx.input.inputProcessor = RelicSorterInit.oldInputProcessor
    return true
  }
}