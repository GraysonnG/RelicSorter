package com.blanktheevil.relicsorter.models

import com.badlogic.gdx.Input
import com.blanktheevil.relicsorter.RelicSorter
import com.blanktheevil.relicsorter.helpers.GZIPHelper
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils
import com.google.gson.Gson
import java.io.File

class Config(
  var binding: BindingEnum = BindingEnum.RIGHT,
  var keyBind: Int = Input.Keys.R
) {
  companion object {
    private val dirPath = ConfigUtils.CONFIG_DIR + File.separator + RelicSorter.modid + File.separator + "config.gz"

    fun init(): Config {
      val file = File(dirPath)

      if (!file.exists()) {
        file.parentFile.mkdirs()
        file.createNewFile()
        return getDefault()
      }

      return load()
    }

    fun save(config: Config) {
      val file = File(dirPath)

      try {
        GZIPHelper.saveDataToFile(file, Gson().toJson(config))
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun load(): Config {
      val file = File(dirPath)

      return try {
        Gson().fromJson(GZIPHelper.loadDataFromFile(file), Config::class.java) ?: getDefault()
      } catch (e: Exception) {
        getDefault()
      }
    }

    private fun getDefault(): Config {
      return Config(BindingEnum.RIGHT, Input.Keys.R)
    }
  }
}