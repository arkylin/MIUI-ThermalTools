package com.thermal.tools.pages

import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.thermal.tools.R
import com.thermal.tools.ShellUtils
import com.thermal.tools.Utils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@BMPage("onekey_mode", "一键设置")
class OneKeyMode: BasePage() {
    override fun onCreate() {
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.thermal_default),
                tips = getString(R.string.thermal_default_tips),
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.Tips)
                        setMessage(R.string.are_you_sure)
                        setLButton(R.string.cancel) {
                            dismiss()
                        }
                        setRButton(R.string.confirm) {
                            ShellUtils.execCommand("rm -rf /data/vendor/thermal/config/*", true)
                            dismiss()
                        }
                    }.show()
                })
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.thermal_factory),
                tips = getString(R.string.thermal_factory_tips),
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.Tips)
                        setMessage(R.string.are_you_sure)
                        setLButton(R.string.cancel) {
                            dismiss()
                        }
                        setRButton(R.string.confirm) {
                            val vendorthermalfiles = Utils.getFileLists(File("/system/vendor/etc/").list())
                            for (thermalfile in vendorthermalfiles) {
                                val thermalnametips = thermalfile.replace("thermal-", "").replace(".conf", "")
                                if (Utils.showThermalTips(thermalnametips) != "不要动") {
                                    val file = File(activity.filesDir.path + "/origin", thermalfile)
                                    val output = FileOutputStream(file.absolutePath)
                                    val input = FileInputStream("/system/vendor/etc/" + thermalfile)
//                                            AESCode.decrypt(input, output)
                                    val buffer = ByteArray(1024)
                                    var length = input.read(buffer)
                                    while (length > 0) {
                                        output.write(buffer, 0, length)
                                        length = input.read(buffer)
                                    }
                                    input.close()
                                    output.close()
                                }
                            }
                            ShellUtils.execCommand(
                                arrayOf(
                                    "rm -rf /data/vendor/thermal/config/*",
                                    "cp -r " + activity.filesDir.path + "/origin/* " + "/data/vendor/thermal/config/"
                                ), true
                            )
                            dismiss()
                        }
                    }.show()
                })
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.thermal_extreme),
                tips = getString(R.string.thermal_extreme_tips),
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.Tips)
                        setMessage(R.string.are_you_sure)
                        setLButton(R.string.cancel) {
                            dismiss()
                        }
                        setRButton(R.string.confirm) {
                            Utils.onekeyWriteToFiles(activity.assets, activity.filesDir.path, "extreme")
                            ShellUtils.execCommand(
                                arrayOf(
                                    "rm -rf /data/vendor/thermal/config/*",
                                    "cp -r " + activity.filesDir.path + "/extreme/* " + "/data/vendor/thermal/config/"
                                ), true
                            )
                            dismiss()
                        }
                    }.show()
                })
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.thermal_danger),
                tips = getString(R.string.thermal_danger_tips),
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.Tips)
                        setMessage(R.string.are_you_sure)
                        setLButton(R.string.cancel) {
                            dismiss()
                        }
                        setRButton(R.string.confirm) {
                            Utils.onekeyWriteToFiles(activity.assets, activity.filesDir.path, "danger")
                            ShellUtils.execCommand(
                                arrayOf(
                                    "rm -rf /data/vendor/thermal/config/*",
                                    "cp -r " + activity.filesDir.path + "/danger/* " + "/data/vendor/thermal/config/"
                                ), true
                            )
                            dismiss()
                        }
                    }.show()
                })
        )
        Line()
        TitleText(getString(R.string.super_charge))
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.thermal_super_charge),
                tips = getString(R.string.thermal_super_charge_tips),
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.Tips)
                        setMessage(R.string.are_you_sure)
                        setLButton(R.string.cancel) {
                            dismiss()
                        }
                        setRButton(R.string.confirm) {
                            Utils.onekeyWriteToFiles(activity.assets, activity.filesDir.path, "Super_Charge")
                            ShellUtils.execCommand(
                                arrayOf(
                                    "rm -rf /data/vendor/thermal/config/*",
                                    "cp -r " + activity.filesDir.path + "/Super_Charge/* " + "/data/vendor/thermal/config/"
                                ), true
                            )
                            dismiss()
                        }
                    }.show()
                })
        )
    }
}