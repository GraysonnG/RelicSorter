package com.blanktheevil.relicsorter

import basemod.*
import basemod.interfaces.PostInitializeSubscriber
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.blanktheevil.relicsorter.models.BindingEnum
import com.blanktheevil.relicsorter.models.Config
import com.blanktheevil.relicsorter.utils.TextureLoaderKt
import java.util.*
import java.util.function.Consumer

class RelicSorterInit : PostInitializeSubscriber {
  companion object {
    val oldInputProcessor: InputProcessor? = null
    private const val DEFAULT_X = 350f
    private const val DEFAULT_Y = 650f

    private const val MOUSEBIND_DEFAULT_Y = DEFAULT_Y - 50f
    private const val MOUSEBIND_DEFAULT_LABEL_Y = DEFAULT_Y

    private const val MOUSEBIND_LABEL_1_X = DEFAULT_X + 50f
    private const val MOUSEBIND_LABEL_2_X = DEFAULT_X + 225f
    private const val MOUSEBIND_LABEL_3_X = DEFAULT_X + 450f

    private const val MOUSEBIND_BUTTON_1_X = DEFAULT_X + 100f
    private const val MOUSEBIND_BUTTON_2_X = DEFAULT_X + 300f
    private const val MOUSEBIND_BUTTON_3_X = DEFAULT_X + 500f

    private const val KEYBIND_BUTTON_X = DEFAULT_X + 25f
    private const val KEYBIND_BUTTON_Y = DEFAULT_Y - 275f
    private const val KEYBIND_LABEL_X = DEFAULT_X + 150f
    private const val KEYBIND_LABEL_Y = KEYBIND_BUTTON_Y + 50f

    private val radioButtons = ArrayList<ModToggleButton>()
  }

  override fun receivePostInitialize() {
    with(ModPanel()) {
      ModLabel("Mouse Binding:", DEFAULT_X + 25f, DEFAULT_Y + 75f, this, doNothingConsumer())
        .also {
          this.addUIElement(it)
        }

      ModLabel("Keyboard Binding:", DEFAULT_X + 25f, KEYBIND_LABEL_Y + 75, this, doNothingConsumer())
        .also {
          this.addUIElement(it)
        }

      ModLabel("", KEYBIND_LABEL_X, KEYBIND_LABEL_Y, this, Consumer {
        it.text = if (it.parent.waitingOnEvent) {
          "Press Key"
        } else {
          "Change sorting hotkey ( ${Input.Keys.toString(RelicSorter.config.keyBind)} )"
        }
      })
        .also {
          this.addUIElement(it)
        }

      ModButton(KEYBIND_BUTTON_X, KEYBIND_BUTTON_Y, this, Consumer {
        it.parent.waitingOnEvent = true
        oldInputProcessor = Gdx.input.inputProcessor
        Gdx.input.inputProcessor = RelicSorterInitInputAdapter(it)
      })
        .also {
          this.addUIElement(it)
        }

      ModToggleButton(MOUSEBIND_BUTTON_1_X, MOUSEBIND_DEFAULT_Y, this, radioConsumer(0))
        .also {
          this.addUIElement(it)
          radioButtons.add(it)
        }

      ModToggleButton(MOUSEBIND_BUTTON_2_X, MOUSEBIND_DEFAULT_Y, this, radioConsumer(1))
        .also {
          this.addUIElement(it)
          radioButtons.add(it)
        }

      ModToggleButton(MOUSEBIND_BUTTON_3_X, MOUSEBIND_DEFAULT_Y, this, radioConsumer(2))
        .also {
          this.addUIElement(it)
          radioButtons.add(it)
        }

      ModLabel("Left Click", MOUSEBIND_LABEL_1_X, MOUSEBIND_DEFAULT_LABEL_Y, this, doNothingConsumer())
        .also {
          this.addUIElement(it)
        }

      ModLabel("Middle Click", MOUSEBIND_LABEL_2_X, MOUSEBIND_DEFAULT_LABEL_Y, this, doNothingConsumer())
        .also {
          this.addUIElement(it)
        }

      ModLabel("Right Click", MOUSEBIND_LABEL_3_X, MOUSEBIND_DEFAULT_LABEL_Y, this, doNothingConsumer())
        .also {
          this.addUIElement(it)
        }

      BaseMod.registerModBadge(
        TextureLoaderKt.getTexture("img/relicsorter/badge.png"),
        RelicSorter.name,
        RelicSorter.author,
        RelicSorter.description,
        this
      )

      initRadioButtons()
    }
  }

  private fun doRadioButton(id: Int) {
    RelicSorter.config.binding = when (id) {
      0 -> BindingEnum.LEFT
      1 -> BindingEnum.MIDDLE
      2 -> BindingEnum.RIGHT
      else -> BindingEnum.NONE
    }

    radioButtons.forEach { it.enabled = false }
    radioButtons[id].enabled = true
  }

  private fun initRadioButtons() {
    when (RelicSorter.config.binding) {
      BindingEnum.LEFT -> radioButtons[0].enabled = true
      BindingEnum.RIGHT -> radioButtons[2].enabled = true
      BindingEnum.MIDDLE -> radioButtons[1].enabled = true
      else -> {}
    }
  }

  private fun radioConsumer(id: Int): Consumer<ModToggleButton> {
    return Consumer {
      doRadioButton(id)
      Config.save(RelicSorter.config)
    }
  }
}