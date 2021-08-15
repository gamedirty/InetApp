package com.sovnem.android.inetapp

import android.util.Log
import com.offbytwo.jenkins.JenkinsServer
import com.offbytwo.jenkins.client.JenkinsHttpClient
import com.offbytwo.jenkins.model.JobWithDetails
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.util.regex.Pattern

object JenkinsApi {


    var JENKINS_URL = ""
    private const val JENKINS_USER = "zhaojunhui"
    private const val JENKINS_PWD = "0000"
    private const val mac = "6c:96:cf:d9:e4:31"


    private val ipPatter: Pattern by lazy { Pattern.compile("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))") }
    private val macPatter: Pattern by lazy {
        Pattern.compile("([0-9a-fA-F]{1,2})(([/\\s:-][0-9a-fA-F]{1,2}){5})")
    }

    fun init() {
        getIpMacMap().forEach {
            val isSame = it.key.equals(mac, true)
            Log.e("zhjh", "是$isSame 啥：" + it.key + "," + it.value)

            if (isSame) {
                JENKINS_URL = "http://${it.value}:8080/jenkins"
            }
        }
        Log.e("zhjh", "JENKINS_URL:$JENKINS_URL")
    }

    private fun getIpMacMap(): HashMap<String, String> {
        val result = HashMap<String, String>()
        execute("ip neigh show").forEach { it ->
            println(it)
            val ips = pattern(it, ipPatter)
            val macs = pattern(it, macPatter)
            val ip = if (ips.isEmpty()) "" else ips[0]
            val mac = if (macs.isEmpty()) "" else macs[0]
            result[mac] = ip
        }
        return result
    }

    private fun pattern(it: String, ipPatter: Pattern): List<String> {
        val ma = ipPatter.matcher(it)
        val result = ArrayList<String>()
        while (ma.find()) {
            result.add(ma.group())
        }
        return result
    }


    fun execute(cmd: String): List<String> {
        val p = Runtime.getRuntime().exec(cmd)
        p.waitFor()
        val bufferedReader = BufferedReader(InputStreamReader(p.inputStream))
        val results = ArrayList<String>()
        var line = bufferedReader.readLine()
        while (line != null) {
            results.add(line)
            line = bufferedReader.readLine()
        }
        return results
    }

    fun build() {
        Log.e("zhjh", "JENKINS_URL:$JENKINS_URL")
        val jenkinsClient =
            JenkinsHttpClient(URI(JENKINS_URL), JENKINS_USER, JENKINS_PWD)
        val jenkinsServer =
            JenkinsServer(jenkinsClient)

        jenkinsServer.jobs.forEach {
            Log.w("zhjh", "哈哈哈：" + it.key + "," + it.value.fullName)
            it.value.build()
        }
    }

    fun JobWithDetails.string(): String {
        val detail = this
        return buildString {
            append(detail.description)
            append(detail.displayName)
            append(detail.description)
            append(detail.description)
            append(detail.description)
        }
    }
}