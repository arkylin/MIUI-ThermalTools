package com.thermal.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.AssetManager
import cn.fkj233.ui.dialog.MIUIDialog
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

object Utils {

    fun showThermalTips(string: String): String {
        var a = "不要动"
        when (string) {
            "4k" -> a = "不清楚"
            "8k" -> a = "不清楚"
            "camera" -> a = "启用摄像头时"
            "chg-only" -> a = "不要动"
            "class0" -> a = "特殊应用分组，例如：QQ等"
            "dolbyvision" -> a = "杜比"
            "engine" -> a = "不要动"
            "hp-mgame" -> a = "性能模式下运行miHoYo游戏"
            "hp-normal" -> a = "性能模式-日常"
            "huanji" -> a = "小米换机"
            "map-india" -> a = "不要动"
            "map" -> a = "不要动"
            "mgame" -> a = "运行miHoYo游戏"
            "navigation" -> a = "运行导航App"
            "nolimits" -> a = "跑分软件，例如：安兔兔"
            "normal" -> a = "日常"
            "per-class0" -> a = "性能模式下，特殊应用分组，例如：QQ"
            "per-navigation" -> a = "不清楚"
            "per-normal" -> a = "性能模式-日常"
            "per-video" -> a = "性能模式下播放视频"
            "phone" -> a = "通话，包括QQ、微信"
            "region-map" -> a = "不要动"
            "tgame" -> a = "腾讯游戏"
            "video" -> a = "播放视频"
            "videochat" -> a = "视频通话"
            "yuanshen" -> a = "原神游戏中开启性能模式"
        }
        return a
    }

    fun showConfTips(string: String): String {
        var a = "不要动"
        when (string) {
            "BAT_SOC" -> a = "电池SOC"
            "VIRTUAL-SENSOR0" -> a = "虚拟传感器权值配置"
            "SS-CPU0" -> a = "CPU0 CPU1 CPU2 CPU3"
            "SS-CPU4" -> a = "CPU4 CPU5 CPU6"
            "SS-CPU7" -> a = "CPU7"
            "MONTIOR-SENSOR0" -> a = "虚拟传感器校正？（不清楚）"
            "SIC-BAT" -> a = "充电流控配置"
            "MONITOR-BAT" -> a = "充电档位调节"
            "MONITOR-FLASH" -> a = "FLASH控制"
            "MONITOR-CCC" -> a = "拔CPU2、CPU3、CPU7"
            "MONITOR-BOOST_LIMIT" -> a = "Boost升频限制"
            "MONITOR-BCL" -> a = "保CPU4 拔CPU2 CPU3 CPU7"
            "MONITOR-GPU" -> a = "GPU频率档位控制"
            "MONITOR-BACKLIGHT" -> a = "背光亮度控制"
            "MONITOR-MODEM-5GTO4G" -> a = "达到温度切换5G到4G"
            "MONITOR-PARALLEL-TO-SERIAL" -> a = "应用市场下载限制"
            "MONITOR-TEMP_STATE" -> a = "TEMP_STATE"
            "SS-LMH_CPU4" -> a = "LMH_CPU4"
            "SS-LMH_CPU7" -> a = "LMH_CPU7"
            "MONITOR-MODEM_PA_NR" -> a = "基站、信号相关"
            "MONITOR-MODEM_PA_LTE" -> a = "基站、信号相关"

        }
        return a
    }

//    fun getParameter(text: String, section: String, parameter: String): String? {
//        val lines = text.split("\n")
//        var currentSection = ""
//        for (line in lines) {
//            if (line.startsWith("[")) {
//                currentSection = line
//            } else {
//                val parts = line.split("\t")
//                if (parts.size == 2 && currentSection == "[$section]" && parts[0] == parameter) {
//                    return parts[1]
//                }
//            }
//        }
//        return null
//    }

    @SuppressLint("SuspiciousIndentation")
    fun getValue(text: String, section: String, parameter: String): List<String>? {
//        val lines = text.lines()
        val lines = text.split("\n")
        var foundSection = false
        for (line in lines) {
            if (line == "[$section]") {
                foundSection = true
            } else if (line.startsWith("[") && foundSection) {
                break
            } else if (foundSection && line.startsWith("$parameter\t")) {
                val parts = line.split("\t")
//                if (parts.size == 2) {
//                    return parts[1]
//                } else {
//                    return parts.drop(1)
//                }
//                if (parts!=null) {
                    return parts.drop(1)
//                }
            } else if (foundSection && line.startsWith("$parameter ")) {
                val parts = line.split(" ")
//                if (parts.size == 2) {
//                    return parts[1]
//                } else {
//                    return parts.drop(1)
//                }
//                if (parts!=null) {
                return parts.drop(1)
//                }
            }
        }
        return null
    }

//    fun getTriggerClearTarget(text: String, section: String, parameter: String): Any? {
//        val lines = text.lines()
//        var foundSection = false
//        for (line in lines) {
//            if (line == "[$section]") {
//                foundSection = true
//            } else if (line.startsWith("[") && foundSection) {
//                break
//            } else if (foundSection && line.startsWith("$parameter\t")) {
//                val parts = line.split("\t")
//                if (parts.size == 2) {
//                    return try {
//                        parts[1]
//                    } catch (e: NumberFormatException) {
//                        0
//                    }
//                } else {
//                    return try {
//                        parts.drop(1).map { it.toInt() }
//                    } catch (e: NumberFormatException) {
//                        listOf(0)
//                    }
//                }
//            }
//        }
//        return null
//    }


//    fun getTriggerClearTarget(text: String): Map<String, List<Int>> {
//        val lines = text.split("\n")
//        val parameters = mutableMapOf<String, List<Int>>()
//        for (line in lines) {
//            val parts = line.split("\t")
//            if (parts.size == 2) {
//                when (parts[0]) {
//                    "trig" -> parameters["trig"] = parts[1].split("\t").map { it.toInt() }
//                    "clr" -> parameters["clr"] = parts[1].split("\t").map { it.toInt() }
//                    "target" -> parameters["target"] = parts[1].split("\t").map { it.toInt() }
//                }
//            }
//        }
//        return parameters
//    }

//    fun getTriggerClearTarget(text: String, section: String, parameter: String): List<Int> {
//        val sectionRegex = Regex("""\[([A-Z])\]""")
//        val parameterRegex = Regex("""([a-z]+)\t([\d\t]+)""")
//        val lines = text.split("\n")
//        var currentSection = ""
//        var currentParameters = mutableMapOf<String, List<Int>>()
//        for (line in lines) {
//            val sectionMatch = sectionRegex.matchEntire(line)
//            if (sectionMatch != null) {
//                currentSection = sectionMatch.groupValues[1]
//            } else {
//                val parameterMatch = parameterRegex.matchEntire(line)
//                if (parameterMatch != null) {
//                    currentParameters[parameterMatch.groupValues[1]] = parameterMatch.groupValues[2].split("\t").map { it.toInt() }
//                }
//            }
//            if (currentSection == section) {
//                return currentParameters[parameter] ?: emptyList()
//            }
//        }
//        return emptyList()
//    }





    fun getFileLists (files:Array<out String>?):ArrayList<String> {
        var i=0
        var thermalFiles = arrayListOf<String>()
        if (files != null) {
            for (file in files) {
                if ( file.indexOf("thermal-") != -1 ) {
                    thermalFiles += file
                    i++
                }
            }
        }
        return thermalFiles
    }

    fun showSetClearDialog(activity: Activity, dir: File) {
        MIUIDialog(activity) {
            setTitle(R.string.Tips)
            setMessage(R.string.are_you_sure)
            setLButton(R.string.cancel) { dismiss() }
            setRButton(R.string.confirm) {
                val files = dir.listFiles()
                for (file in files!!) {
                    file.delete()
                }
                dismiss()
            }
        }.show()
    }

    fun onekeyWriteToFiles (assets: AssetManager,filesDir: String, mode: String) {
        val extremeDir = File(filesDir, mode)
        if (!extremeDir.exists()) {
            extremeDir.mkdirs()
        }
        val extremeFiles = assets.list(mode)
        if (extremeFiles != null) {
            for (fileName in extremeFiles) {
                val inputStream = assets.open("$mode/$fileName")
                val outputFile = File(extremeDir, fileName)
                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(1024)
                var length = inputStream.read(buffer)
                while (length > 0) {
                    outputStream.write(buffer, 0, length)
                    length = inputStream.read(buffer)
                }
                inputStream.close()
                outputStream.close()
            }
        }
    }


}