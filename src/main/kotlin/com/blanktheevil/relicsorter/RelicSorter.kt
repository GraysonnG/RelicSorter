package com.blanktheevil.relicsorter

import basemod.BaseMod
import basemod.interfaces.PreDungeonUpdateSubscriber
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.blanktheevil.relicsorter.helpers.RelicSorterHelper
import com.blanktheevil.relicsorter.models.BindingEnum
import com.blanktheevil.relicsorter.models.Config
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.relics.AbstractRelic
import java.io.IOException
import java.util.*


@Suppress("unused")
@SpireInitializer
class RelicSorter : PreDungeonUpdateSubscriber {
  companion object {

    lateinit var config: Config

    var name: String = ""
    private var version: String = ""
    var modid: String = ""
    var author: String = ""
    var description: String = ""

    @JvmStatic
    fun initialize() {
      loadProjectProperties()
      config = Config.init()
      BaseMod.subscribe(RelicSorterInit())
      BaseMod.subscribe(RelicSorter())
    }

    private fun loadProjectProperties() {
      try {
        with(Properties()) {
          load(RelicSorter::class.java.getResourceAsStream("/META-INF/relicsorter.prop"))
          name = getProperty("name")
          version = getProperty("version")
          modid = getProperty("id")
          author = getProperty("author")
          description = getProperty("description")
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }

    private fun log(vararg items: Any?) {
      if (Settings.isDebug) {
        print("Relic Sorter")
        items.forEach {
          print(" : ${it.toString()}")
        }
        println()
      }
    }
  }

  override fun receivePreDungeonUpdate() {
    if (AbstractDungeon.player == null || AbstractDungeon.player.relics == null) return
    with(AbstractDungeon.player) {
      var clicked = when (config.binding) {
        BindingEnum.LEFT -> InputHelper.justClickedLeft
        BindingEnum.RIGHT -> InputHelper.justClickedRight
        BindingEnum.MIDDLE -> handleMiddleClick()
        BindingEnum.NONE -> false
      }

      if (!clicked) clicked = Gdx.input.isKeyJustPressed(config.keyBind)

      if (relics.size > 2 && RelicSorterHelper.canMoveRelics() && clicked) {
        var selIndex: Int? = null

        for (i in relics.size - 1 downTo 2) {
          val relic: AbstractRelic = relics[i]
          if (relic.hb.hovered) {
            selIndex = i
            log("Clicked ${relic.name} $i")
          }
        }

        if (selIndex != null) {
          val tempCurrentX = relics[1].currentX

          for (i in 1 until selIndex) {
            val relicA = relics[i]
            val relicB = relics[i + 1]
            log("Moving ${relicA.name} to ${relicB.name}")
            relicA.moveTo(relicB)
          }

          relics[selIndex].moveTo(tempCurrentX)
          relics.add(1, relics.removeAt(selIndex))

          when (config.binding) {
            BindingEnum.LEFT -> InputHelper.justClickedLeft = false
            BindingEnum.RIGHT -> InputHelper.justClickedRight = false
            else -> { /* do nothing */ }
          }
        }
      }
    }
  }

  private var justPressedMiddleLast = false

  private fun handleMiddleClick(): Boolean {
    val isMiddleDown = Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)
    if (isMiddleDown && !justPressedMiddleLast) {
      justPressedMiddleLast = true
      return true
    }

    if(!isMiddleDown && justPressedMiddleLast) {
      justPressedMiddleLast = false
    }

    return false
  }
}