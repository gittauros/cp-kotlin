package com.tauros.cp.archive.daily

import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/15
 */
fun main(args: Array<String>) {
    val cases = readln().toInt()
    repeat(cases) {
        val n = readln().toInt()
        val nums = readln().split(" ").map(string::toInt).toIntArray()

        var iter = nums.last()
        var success = true
        out@ for (i in n - 2 downTo 0) {
            if (nums[i] <= iter) {
                iter = nums[i]
                continue
            }
            var num = nums[i]
            while (num > 0) {
                val digit = num % 10
                if (digit > iter) {
                    success = false
                    break@out
                }
                iter = digit
                num /= 10
            }
        }
        println(if (success) "YES" else "NO")
    }
}