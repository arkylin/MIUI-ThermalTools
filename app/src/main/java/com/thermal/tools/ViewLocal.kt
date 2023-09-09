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
//import android.content.Context
//import android.content.SharedPreferences
import android.R.attr.key
import android.annotation.SuppressLint
import android.content.Intent
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
class ViewLocal : MIUIActivity() {
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val activity = this;

    private val datadirsrc = "/data/vendor/thermal/config/"
    private val datadir = File(datadirsrc)
    private val datafiles: Array<out String>? = datadir.list()
//    private var datathermalfiles = arrayListOf<String>()

    private val vendordirsrc = "/system/vendor/etc/"
    private val vendordir = File(vendordirsrc)
    private val vendorfiles: Array<out String>? = vendordir.list()
//    private var vendorthermalfiles = arrayListOf<String>()

    private var iswriteable = 0

//    private var dirsrc = datadirsrc


    init {
        initView {
            registerMain(getString(R.string.app_name), false) {
                TextSummary("由于BlockMIUI库更新，导致新版中无法批量生成页面，故做仅作旧版本的Activity跳转，后期修改或者可能会被废除\n\n!!!\n进入此页面后会出现，退回后显示空白的现象，将软件退出重进可解决（Activity跳转原因）\n!!!", colorId = R.color.red)
                Line()
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.explore_thermal_vendor), //vendor专业
                        tips = getString(R.string.explore_thermal_vendor_tips),
                        onClickListener = {
//                            ShellUtils.execCommand("cp /data/vendor/thermal/config/* "+filesDir+"/data", true)
                            var dirsrc = vendordirsrc
                            var dir = File(dirsrc)
                            var files: Array<out String>? = dir.list()
                            register("expert_mode", getString(R.string.explore_thermal_vendor), false) {
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
                                val thermalfiles = Utils.getFileLists(files)
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
                                                        tips = Utils.showConfTips(item),
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
                            showFragment("expert_mode")
                        })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        getString(R.string.explore_thermal_data), //data专业
                        tips = getString(R.string.explore_thermal_data_tips),
                        onClickListener = {
                            ShellUtils.execCommand("rm -rf " + cacheDir + "/data/*", false)
                            ShellUtils.execCommand("cp " + datadirsrc + "/* " + cacheDir + "/data && chmod 777 " + cacheDir + "/data/*", true)
                            var dirsrc = cacheDir.path+"/data/" // cache/data/
//                            var dirsrc = "/data/user/0/com.thermal.tools/cache/data/"
                            var dir = File(dirsrc)
                            var files: Array<out String>? = dir.list()
                            register("data_expert_mode", getString(R.string.explore_thermal_data), false) {
//                                async = object : AsyncInit {
//                                    override val skipLoadItem: Boolean = true
//                                    override fun onInit(fragment: MIUIFragment) {
//                                        fragment.showLoading()
//                                        Thread.sleep(100)
////                                        fragment.addItem(TextV("Test"))
//                                        fragment.closeLoading()
////                        showToast(getString(R.string.got_file))
//                                        fragment.initData()
//                                    }
//                                }
                                val thermalfiles = Utils.getFileLists(files)
                                for (thermalfile in thermalfiles) {
                                    val thermalnametips = thermalfile.replace("thermal-", "").replace(".conf", "")
                                    if (Utils.showThermalTips(thermalnametips) != "不要动") {
                                        TextSummaryArrow(
                                            TextSummaryV(
                                                text = thermalfile,
                                                tips = Utils.showThermalTips(thermalnametips),
                                                onClickListener = {
                                                    showFragment("Viewdata$thermalfile")
                                                })
                                        )
                                        val file = File(filesDir.path + "/data", thermalfile)
                                        val input = FileInputStream(dirsrc + thermalfile)
                                        val output = FileOutputStream(file.absolutePath)
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
                                        val file = File(filesDir.path + "/data", thermalfile)
                                        val content = file.readText()
                                        val pattern = Regex("\\[(.*?)]") //不能听idea提示的去掉"\\"
                                        register("Viewdata$thermalfile", thermalfile, false) {
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
                                                        tips = Utils.showConfTips(item),
                                                        onClickListener = {
                                                            showFragment("data$thermalnametips/$item")
                                                        })
                                                )
                                                //最后显示页面
                                                register("data$thermalnametips/$item", "$thermalnametips/$item", false) {
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
                            showFragment("data_expert_mode")
                        })
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
