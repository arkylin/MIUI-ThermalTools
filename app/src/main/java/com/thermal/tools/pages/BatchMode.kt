package com.thermal.tools.pages

import android.widget.Switch
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.activity.view.TextWithSwitchV
import cn.fkj233.ui.dialog.MIUIDialog
import com.thermal.tools.R
import com.thermal.tools.ShellUtils
import com.thermal.tools.Utils
import com.thermal.tools.Utils.batchBan

@BMPage("batch_mode","批量设置")
class BatchMode: BasePage() {
    override fun onCreate() {
        TextSummary("功能测试：\n以下设置暂不支持关闭，请谨慎开启", colorId = R.color.red)
        TextSummary("直接修改/data/vendor/thermal/config目录下的温控文件，如果该目录下面没有文件，请先在一键设置中选择设置（例如：出厂温控）", colorId = R.color.red)
        TextWithSwitch(
            TextV(textId = R.string.ban_5gto4g),
            SwitchV("ban_5gto4g", onClickListener = {
                ShellUtils.execCommand("rm -rf " + activity.cacheDir + "/batch/*", false)
                ShellUtils.execCommand("rm -rf " + activity.filesDir + "/batch/*", false)
                ShellUtils.execCommand("cp " + "/data/vendor/thermal/config" + "/* " + activity.cacheDir + "/batch && chmod 777 " + activity.cacheDir + "/batch/*", true)
                batchBan("MONITOR-MODEM-5GTO4G")
                ShellUtils.execCommand("cp " + activity.filesDir + "/batch/en/*" + " /data/vendor/thermal/config/", true)
            })
        )
        TextWithSwitch(
            TextV(textId = R.string.ban_partoser),
            SwitchV("ban_partoser", onClickListener = {
                ShellUtils.execCommand("rm -rf " + activity.cacheDir + "/batch/*", false)
                ShellUtils.execCommand("rm -rf " + activity.filesDir + "/batch/*", false)
                ShellUtils.execCommand("cp " + "/data/vendor/thermal/config" + "/* " + activity.cacheDir + "/batch && chmod 777 " + activity.cacheDir + "/batch/*", true)
                batchBan("MONITOR-PARALLEL-TO-SERIAL")
                ShellUtils.execCommand("cp " + activity.filesDir + "/batch/en/*" + " /data/vendor/thermal/config/", true)
            })
        )
        TextWithSwitch(
            TextV(textId = R.string.ban_backlight),
            SwitchV("ban_backlight", onClickListener = {
                ShellUtils.execCommand("rm -rf " + activity.cacheDir + "/batch/*", false)
                ShellUtils.execCommand("rm -rf " + activity.filesDir + "/batch/*", false)
                ShellUtils.execCommand("cp " + "/data/vendor/thermal/config" + "/* " + activity.cacheDir + "/batch && chmod 777 " + activity.cacheDir + "/batch/*", true)
                batchBan("MONITOR-BACKLIGHT")
                ShellUtils.execCommand("cp " + activity.filesDir + "/batch/en/*" + " /data/vendor/thermal/config/", true)

            })
        )
        TextWithSwitch(
            TextV(textId = R.string.ban_gpu),
            SwitchV("ban_gpu", onClickListener = {
                ShellUtils.execCommand("rm -rf " + activity.cacheDir + "/batch/*", false)
                ShellUtils.execCommand("rm -rf " + activity.filesDir + "/batch/*", false)
                ShellUtils.execCommand("cp " + "/data/vendor/thermal/config" + "/* " + activity.cacheDir + "/batch && chmod 777 " + activity.cacheDir + "/batch/*", true)
                batchBan("MONITOR-GPU")
                ShellUtils.execCommand("cp " + activity.filesDir + "/batch/en/*" + " /data/vendor/thermal/config/", true)

            })
        )
        Line()
        TitleText("充电限速\n以下仅需选择一个即可")
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
        TextWithSwitch(
            TextV(textId = R.string.ban_charge),
            SwitchV("ban_charge", onClickListener = {
                ShellUtils.execCommand("rm -rf " + activity.cacheDir + "/batch/*", false)
                ShellUtils.execCommand("rm -rf " + activity.filesDir + "/batch/*", false)
                ShellUtils.execCommand("cp " + "/data/vendor/thermal/config" + "/* " + activity.cacheDir + "/batch && chmod 777 " + activity.cacheDir + "/batch/*", true)
                batchBan("MONITOR-BAT")
                ShellUtils.execCommand("cp " + activity.filesDir + "/batch/en/*" + " /data/vendor/thermal/config/", true)
                //
                ShellUtils.execCommand("rm -rf " + activity.cacheDir + "/batch/*", false)
                ShellUtils.execCommand("rm -rf " + activity.filesDir + "/batch/*", false)
                ShellUtils.execCommand("cp " + "/data/vendor/thermal/config" + "/* " + activity.cacheDir + "/batch && chmod 777 " + activity.cacheDir + "/batch/*", true)
                batchBan("SIC-BAT")
                ShellUtils.execCommand("cp " + activity.filesDir + "/batch/en/*" + " /data/vendor/thermal/config/", true)
            })
        )
    }
}