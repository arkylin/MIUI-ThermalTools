package com.thermal.tools.pages

import android.content.Context
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.TextSummaryV
import com.thermal.tools.R
import com.thermal.tools.Utils

@BMPage("setting","设置")
class Setting: BasePage() {
    override fun onCreate() {
        TitleText(getString(R.string.about))
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.about), //快充
                tips = getString(R.string.about_tips),
                onClickListener = {
                    showFragment("about")
                })
        )
        Line()
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.clear_cache),
                onClickListener = { Utils.showSetClearDialog(activity, activity.cacheDir) }
            )
        )
        TextSummaryArrow(
            TextSummaryV(
                getString(R.string.clear_files),
                onClickListener = { Utils.showSetClearDialog(activity, activity.filesDir) }
            )
        )
    }
}