package com.thermal.tools.pages

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.TextSummaryV
import com.thermal.tools.R

@BMPage("about", "关于")
class About: BasePage() {
    override fun onCreate() {
        Author(
            authorHead = getDrawable(R.drawable.author)!!,
            authorName = getString(R.string.author_name),
            authorTips = "酷安ID：Arkylin",
            onClickListener = {
                try {
                    activity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("coolmarket://u/3207650")
//                                    Uri.parse("coolmarket://apk/com.lt2333.simplicitytools")
                        )
                    )
//                            Toast.makeText(activity, "恳求一个五星好评，Thanks♪(･ω･)ﾉ", Toast.LENGTH_LONG)
//                                .show()
                } catch (e: Exception) {
                    val uri =
                        Uri.parse("https://github.com/arkylin")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    activity.startActivity(intent)
                }
            })
        TextSummaryArrow(
            TextSummaryV(
                textId = R.string.QQqun,
                tips = getString(R.string.QQqunum),
                onClickListener = {
                    try {
                        activity.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3Drbe6sOM-OYOxW1OGnRRrPUv1ixHMYwdK")
//                                    Uri.parse("coolmarket://apk/com.lt2333.simplicitytools")
                            )
                        )
//                                val intent = Intent(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key))
//                                intent.setData()
//                                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intent)

                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                })
        )
        TitleText(text = getString(R.string.thank_list))
        TextSummaryArrow(
            TextSummaryV(
                textId = R.string.BlockMiui,
                onClickListener = {
                    try {
                        val uri =
                            Uri.parse("https://github.com/Block-Network/blockmiui")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        activity.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                })
        )
        TextSummaryArrow(
            TextSummaryV(
                textId = R.string.Scene,
                onClickListener = {
                    try {
                        val uri =
                            Uri.parse("https://github.com/helloklf/vtools")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        activity.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                })
        )
        TextSummaryArrow(
            TextSummaryV(
                textId = R.string.SimplicityTools,
                onClickListener = {
                    try {
                        val uri =
                            Uri.parse("https://github.com/LittleTurtle2333/SimplicityTools")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        activity.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                })
        )
    }
}