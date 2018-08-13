package com.toy.qiaodata.slidingconflict

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.Comparator

class encode {
    companion object {
        fun getString(context:Context): HashMap<String,String> {

            val fis = context.assets.open("data.txt")
            val hm = HashMap<String,String>()
            if (fis != null) {
                val br = BufferedReader(InputStreamReader(fis, "UTF-8"))

                var line:String? = br.readLine()
                while (line != null) {
                    val split = line.split(' ')
                    if (split != null && split.size == 2) {
                        if (split[1].contains("$")) {
                            hm.put(split[0],split[1])
                        } else {
                            hm.put(split[1],split[0])
                        }
                    }
                    line = br.readLine()
                }
                return hm
            }
            return hm
        }

        fun getTotalChar(hm: HashMap<String, String>):CharArray {
            val treeSet = TreeSet<Char>(object : Comparator<Char> {
                override fun compare(p0: Char, p1: Char): Int {
                    return  p0 - p1
                }

            })
            hm.forEach { key, value ->
                if (value.length == 11) {
                    val sequence = value.subSequence(3, 8)
                    sequence.forEach {
                        treeSet.add(it)
                    }
                }
            }

            return  treeSet.toCharArray()
        }
    }
}
