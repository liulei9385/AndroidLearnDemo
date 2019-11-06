package com.leige.tool

import java.lang.StringBuilder

object ConvertJson {

    public fun formatGradleDependencies(s: String): StringBuilder {
        val split = s.split(",")
        val replaceArray = arrayOf("compile group: ", "name: ", "version: ")
        var count = 0
        val sb = StringBuilder()
        sb.append("implementation '")

        for (s1 in split) {
            val str = s1.trim()
            for (temp in replaceArray) {
                var newstr = replaceStr(str, temp, "")
                if (newstr != null) {
                    count++
                    newstr = newstr.replace("'", "", false)
                    if (count != 3)
                        sb.append("$newstr:")
                    else sb.append("$newstr")
                }
            }
        }
        sb.append("'")
        return sb
    }

    private fun replaceStr(str: String, old: String, replace: String): String? {
        if (str.contains(old)) {
            return str.replace(old, replace, false)
        }
        return null
    }


}
