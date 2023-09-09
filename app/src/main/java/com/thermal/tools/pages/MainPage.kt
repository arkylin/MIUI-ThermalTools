package com.thermal.tools.pages

import android.content.Intent
import android.view.View
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.thermal.tools.Activity
import com.thermal.tools.MainActivity
import com.thermal.tools.R
import com.thermal.tools.ViewLocal
import java.io.File
import kotlin.system.exitProcess

@BMMainPage("MIUI 温控工具箱")
class MainPage: BasePage() {
    override fun onCreate() {

        val firstRun = File(activity.cacheDir,"FirstRun")
        if (!firstRun.exists()) {
            // 应用第一次启动, 运行 MIUIDialog 代码
            MIUIDialog(activity) {
                setTitle(R.string.Tips)
                setMessage(R.string.private_text)
                setLButton(R.string.cancel) {
                    exitProcess(0)
                    dismiss()
                }
                setRButton(R.string.confirm) {
                    dismiss()
                    firstRun.mkdirs()
                    // 记录应用已启动过
                }
            }.show()
        }
        //生成文件夹
        val fileNames = arrayOf("origin", "onekey", "batch", "expert", "Super_Charge","data")
        fileNames.forEach { folder ->
            listOf(activity.filesDir, activity.cacheDir).forEach { dir ->
                val file = File(dir, folder)
                if (!file.exists()) {
                    file.mkdirs()
                }
            }
        }

        TitleText(getString(R.string.edit_thermal_file))
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.onekey_mode), //一键
                tips = getString(R.string.onekey_mode_tips),
                onClickListener = {
                    showFragment("onekey_mode")
                })
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.batch_mode), //批量
                tips = getString(R.string.batch_mode_tips),
                onClickListener = {
                    showFragment("batch_mode")
                },
                colorId = R.color.red
            )
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.explore_thermal_vendor), //vendor专业
                tips = getString(R.string.explore_thermal_vendor_tips),
                onClickListener = {
                    val intent = Intent(activity, ViewLocal::class.java)
                    activity.startActivity(intent)
                }
            )
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.explore_thermal_data), //data专业
                tips = getString(R.string.explore_thermal_data_tips),
                onClickListener = {
                    val intent = Intent(activity, ViewLocal::class.java)
                    activity.startActivity(intent)
                }
            )
        )
        Line()
        TitleText(getString(R.string.edit_expert_file))
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.expert_mode), //专业
                tips = getString(R.string.expert_mode_tips),
                onClickListener = {

                }
            )
        )
        Line()
        TitleText(getString(R.string.additional_features))
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.super_charge), //快充
                tips = getString(R.string.super_charge_tips),
                onClickListener = {
                })
        )
        Line()
        TitleText(getString(R.string.about))
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.about),
                tips = getString(R.string.about_tips),
                onClickListener = {
                    showFragment("about")
                })
        )
    }
}