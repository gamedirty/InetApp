package com.sovnem.android.inetapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var ip: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JenkinsApi.init()

//        IpScanner.context = this
//        IpScanner.setOnScanListener(object : OnScanListener {
//            override fun scan(resultMap: Map<String, String>?) {
//                resultMap?.forEach {
//                    println("是啥：" + it.key + "," + it.value)
//                }
//            }
//        })
//        IpScanner.startScan()
//        thread {
//            println("接发打发")
//            execute("ip neigh show").forEach {
//                Log.i("zhjh","是啥："+it)
//            }
//        }


//        File("/proc/net/arp").readLines().forEach {
//            Log.i("zhjh","是啥："+it)
//        }


    }


    fun buildIt(view: View) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(JenkinsApi.JENKINS_URL)
        startActivity(intent)
    }
}