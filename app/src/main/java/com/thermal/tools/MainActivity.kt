package com.thermal.tools

//import android.os.Handler
//import android.os.Looper
//import android.view.View
//import android.widget.Switch
//import android.widget.Toast
//import cn.fkj233.ui.activity.data.AsyncInit
//import cn.fkj233.ui.activity.fragment.MIUIFragment
//import cn.fkj233.ui.activity.view.SpinnerV
//import cn.fkj233.ui.activity.view.SwitchV
//import cn.fkj233.ui.activity.view.TextV
//import cn.fkj233.ui.dialog.NewDialog
//import android.support.v4.app.ActivityCompat
import android.annotation.SuppressLint
//import android.content.Context
import android.content.Intent
//import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.AsyncInit
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.system.exitProcess

@SuppressLint("SuspiciousIndentation")
class MainActivity : MIUIActivity() {
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val activity = this;

    private val dirsrc = "/system/vendor/etc/"
    private val dir = File(dirsrc)
    private val files: Array<out String>? = dir.list()
    private var thermalfiles = arrayListOf<String>()

    init {
        initView {
            val firstRun = File(cacheDir,"FirstRun")
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
                    }
                }.show()
                firstRun.mkdirs()
                // 记录应用已启动过
            }
            val fileNames = arrayOf("origin", "onekey", "batch", "expert", "Super_Charge")
            fileNames.forEach { folder ->
                listOf(filesDir, cacheDir).forEach { dir ->
                    val file = File(dir, folder)
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                }
            }

            registerMain(getString(R.string.app_name), false) {
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
                        })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.explore_thermal), //专业
                        tips = getString(R.string.explore_thermal_tips),
                        onClickListener = {
//                            showToast(getString(R.string.getting_thermal_file))
                            showFragment("expert_mode")
                        })
                )
                Line()
                TitleText(getString(R.string.edit_expert_file))
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.expert_mode), //专业
                        tips = getString(R.string.expert_mode_tips),
                        onClickListener = {
//                            showToast(getString(R.string.getting_thermal_file))
                            showFragment("expert_mode")
                        })
                )
                Line()
                TitleText(getString(R.string.additional_features))
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.super_charge), //快充
                        tips = getString(R.string.super_charge_tips),
                        onClickListener = {
                            showFragment("super_charge")
                        })
                )
                Line()
                TitleText(getString(R.string.about))
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.about), //快充
                        tips = getString(R.string.about_tips),
                        onClickListener = {
                            showFragment("about")
                        })
                )
            }


            register("onekey_mode", getString(R.string.onekey_mode), false) {
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
                                    thermalfiles = Utils.getFileLists(files)
                                    for (thermalfile in thermalfiles) {
                                        val thermalnametips = thermalfile.replace("thermal-", "").replace(".conf", "")
                                        if (Utils.showThermalTips(thermalnametips) != "不要动") {
                                            val file = File(filesDir.path + "/origin", thermalfile)
                                            val output = FileOutputStream(file.absolutePath)
                                            val input = FileInputStream(dirsrc + thermalfile)
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
                                            "cp -r " + filesDir.path + "/origin/* " + "/data/vendor/thermal/config/"
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
                                    Utils.onekeyWriteToFiles(assets, filesDir.path, "extreme")
                                    ShellUtils.execCommand(
                                        arrayOf(
                                            "rm -rf /data/vendor/thermal/config/*",
                                            "cp -r " + filesDir.path + "/extreme/* " + "/data/vendor/thermal/config/"
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
                                    Utils.onekeyWriteToFiles(assets, filesDir.path, "danger")
                                    ShellUtils.execCommand(
                                        arrayOf(
                                            "rm -rf /data/vendor/thermal/config/*",
                                            "cp -r " + filesDir.path + "/danger/* " + "/data/vendor/thermal/config/"
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
                                    Utils.onekeyWriteToFiles(assets, filesDir.path, "Super_Charge")
                                    ShellUtils.execCommand(
                                        arrayOf(
                                            "rm -rf /data/vendor/thermal/config/*",
                                            "cp -r " + filesDir.path + "/Super_Charge/* " + "/data/vendor/thermal/config/"
                                        ), true
                                    )
                                    dismiss()
                                }
                            }.show()
                        })
                )
            }

            register("batch_mode", getString(R.string.batch_mode), false) {

            }
            register("expert_mode", getString(R.string.expert_mode), false) {
                async = object : AsyncInit {
                    override val skipLoadItem: Boolean = true
                    override fun onInit(fragment: MIUIFragment) {
                        fragment.showLoading()
                        Thread.sleep(100)
//                                        fragment.addItem(TextV("Test"))
                        fragment.closeLoading()
//                        showToast(getString(R.string.got_file))
                        fragment.initData()
                    }
                }
                thermalfiles = Utils.getFileLists(files)
                for (thermalfile in thermalfiles) {
                    val thermalnametips = thermalfile.replace("thermal-", "").replace(".conf", "")
                    if (Utils.showThermalTips(thermalnametips) != "不要动") {
                        TextSummaryArrow(
                            TextSummaryV(
                                thermalfile,
                                tips = Utils.showThermalTips(thermalnametips),
                                onClickListener = {
                                    showFragment("Edit$thermalfile")
                                })
                        )
                        val file = File(cacheDir.path + "/origin", thermalfile)
                        val output = FileOutputStream(file.absolutePath)
                        val input = FileInputStream(dirsrc + thermalfile)
                        AESCode.decrypt(input, output)
                        input.close()
                        output.close()
                    }
                }
                //
                for (thermalfile in thermalfiles) {
                    //4k 8k normal ...
                    val thermalnametips = thermalfile.replace("thermal-", "").replace(".conf", "")
                    if (Utils.showThermalTips(thermalnametips) != "不要动") {
                        val file = File(cacheDir.path + "/origin", thermalfile)
                        val content = file.readText()
                        val pattern = Regex("\\[(.*?)]") //不能听idea提示的去掉"\\"
                        register("Edit$thermalfile", thermalfile, false) {
                            pattern.findAll(content).forEach {
                                //SS-CPU0 MONITOR-BAT...
                                val withoutzhongkuohaocate = it.value.replace(pattern, "$1")
                                val item = it.value.replace(pattern, "$1")
                                    .replace(thermalnametips.uppercase() + "-", "")
                                //                            val iteml = "$thermalnametips/$item"
                                TextSummaryArrow(
                                    TextSummaryV(
                                        item,
                                        //                                    tips = showThermalTips(thermalnametips),
                                        onClickListener = {
                                            showFragment("$thermalnametips/$item")
                                        })
                                )
                                //最后显示页面
                                register("$thermalnametips/$item", "$thermalnametips/$item", false) {
                                    Utils.getValue(
                                        content,
                                        withoutzhongkuohaocate,
                                        "algo_type"
                                    )?.get(0)?.let { it2 ->
                                        SpinnerV(
                                            it2.toString(), dropDownWidth = 170F
                                        ) {
                                            add("ss") {}
                                            add("monitor") {}
                                            add("sic") {}
                                            add("simulated") {}
                                        }.let { it3 ->
                                            TextWithSpinner(
                                                TextV("algo_type"),
                                                it3
                                            )
                                        }
                                    }
                                    Utils.getValue(
                                        content,
                                        withoutzhongkuohaocate,
                                        "sensor"
                                    )?.get(0)?.let { it2 ->
                                        SpinnerV(
                                            it2.toString(), dropDownWidth = 200F
                                        ) {
                                            add("VIRTUAL-SENSOR0") {}
                                            add("battery") {}
                                            add("flash_therm") {}
                                            add("quiet_therm") {}
                                            add("wifi_therm") {}
                                            add("cpu_therm") {}
                                            add("charger_therm0") {}
                                            add("pa_therm0") {}
                                            add("pa_therm1") {}
                                        }.let { it3 ->
                                            TextWithSpinner(
                                                TextV("sensor"),
                                                it3
                                            )
                                        }
                                    }
                                    Utils.getValue(
                                        content,
                                        withoutzhongkuohaocate,
                                        "device"
                                    )?.get(0)?.let { it2 ->
                                        SpinnerV(
                                            it2.toString(), dropDownWidth = 200F
                                        ) {
                                            add("cpu0") {}
                                            add("cpu4") {}
                                            add("cpu7") {}
                                            add("VIRTUAL-SENSOR0") {}
                                            add("thermal_fcc_override") {}
                                            add("battery") {}
                                            add("temp_state") {}
                                            add("hotplug_cpu2+hotplug_cpu3+hotplug_cpu7") {}
                                            add("boost_limit") {}
                                            add("cpu4+cpu7+hotplug_cpu2+hotplug_cpu3") {}
                                            add("gpu") {}
                                            add("blacklight-clone") {}
                                            add("modem_limit") {}
                                            add("market_download_limit") {}
                                            add("lmh_cpu4") {}
                                            add("lmh_cpu7") {}
                                            add("modem_pa_nr") {}
                                            add("modem_pa_lte") {}
                                        }.let { it3 ->
                                            TextWithSpinner(
                                                TextV("device"),
                                                it3
                                            )
                                        }
                                    }
                                    Utils.getValue(
                                        content,
                                        withoutzhongkuohaocate,
                                        "polling"
                                    )?.get(0)?.let { it2 ->
                                        SpinnerV(
                                            it2.toString()
                                        ) {
                                            add("10000") {}
                                            add("1000") {}
                                            add("2000") {}
                                        }.let { it3 ->
                                            TextWithSpinner(
                                                TextV("polling"),
                                                it3
                                            )
                                        }
                                    }
                                    Line()
                                    val trig = Utils.getValue(content, withoutzhongkuohaocate, "trig")
//                                                    var trig1 = ""
                                    val trig1 = if (trig != null && trig.size == 1) {
                                        trig[0]
                                    } else {
                                        trig.toString()
                                    }
                                    TextWithSpinner(TextV("trig"), SpinnerV(trig1) {
                                    })
//                                                    Text { }
                                    val clr = Utils.getValue(content, withoutzhongkuohaocate, "clr")
//                                                    var clr1 = ""
                                    val clr1 = if (clr != null && clr.size == 1) {
                                        clr[0]
                                    } else {
                                        clr.toString()
                                    }
                                    TextWithSpinner(TextV("clr"), SpinnerV(clr1) {
                                    })
//                                                    Text { }
                                    val target = Utils.getValue(content, withoutzhongkuohaocate, "target")
//                                                    var target1 = ""
                                    val target1 = if (target != null && target.size == 1) {
                                        target[0]
                                    } else {
                                        target.toString()
                                    }
                                    TextWithSpinner(TextV("target"), SpinnerV(target1) {
                                    })

                                }
                            }
                        }
                    }
                }
                //
            }

            register("super_charge", getString(R.string.super_charge), false) {

                //Todo

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
                                    Utils.onekeyWriteToFiles(assets, filesDir.path, "Super_Charge")
                                    ShellUtils.execCommand(
                                        arrayOf(
                                            "rm -rf /data/vendor/thermal/config/*",
                                            "cp -r " + filesDir.path + "/Super_Charge/* " + "/data/vendor/thermal/config/"
                                        ), true
                                    )
                                    dismiss()
                                }
                            }.show()
                        })
                )
//                Line()
//                val templists = arrayOf(32,35,37.7,38.7,39.5,40,41,43.7,44.7,46.5)
//                val content = File(filesDir.path+"/Super_Charge/thermal-normal.conf").readText()
//                val target = Utils.getValue(content, "NORMAL-MONITOR-BAT", "target")
//                var i = 0
//                templists.forEach {
//                    TextWithSpinner(
//                        TextV(it.toString()+"℃"),
//                        SpinnerV(
//                            "120W"
////                            target[i].toString()
//                        ) {
////                            add("120W") {}
////                            add("100W") {}
////                            add("90W") {}
////                            add("80W") {}
////                            add("60W") {}
////                            add("50W") {}
////                            add("40W") {}
////                            add("34W") {}
////                            add("30W") {}
////                            add("25W") {}
////                            add("25W") {}
////                            add("10W") {}
////                            add("5W") {}
////                            add("2.5W") {}
//                        })
//                    i++
//                }

            }

            register("about", getString(R.string.about), false) {
                Author(
                    authorHead = getDrawable(R.drawable.author)!!,
                    authorName = getString(R.string.author_name),
                    authorTips = "酷安ID：Arkylin",
                    onClickListener = {
                        try {
                            startActivity(
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
                            startActivity(intent)
                        }
                    })
                TitleText(text = getString(R.string.thank_list))
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.BlockMiui,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/Block-Network/blockmiui")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
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
                                startActivity(intent)
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
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            }

            registerMenu(getString(R.string.menu)) {
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.clear_cache),
                        onClickListener = { Utils.showSetClearDialog(activity, cacheDir) }
                    )
                )
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.clear_files),
                        onClickListener = { Utils.showSetClearDialog(activity, filesDir) }
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setSP(getPreferences(0))
        super.onCreate(savedInstanceState)
    }


    private fun showToast(string: String) {
        handler.post {
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
        }
    }

}
