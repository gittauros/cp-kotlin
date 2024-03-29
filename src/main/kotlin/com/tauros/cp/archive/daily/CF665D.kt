package com.tauros.cp.archive.daily

import com.tauros.cp.bar
import com.tauros.cp.common.string
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/16
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/665/D
    // 除了2以外所有的质数都是奇数，分解成两个数的和一定一奇一偶
    // 这个奇数如果不是1的话就只能最多放一个，且一定不能放两个偶数
    // 各种判断乱搞下，题面也没说非得选出一个，很奇怪，一顿WA
    val n = readln().toInt()
    val nums = readln().split(" ").map(string::toInt).toIntArray()

    val cnt = nums.count { it == 1 }
    val cap = 1e6.toInt() shl 1
    val notPrime = bar(cap + 1)
    notPrime[1] = true
    for (i in 2 .. cap) {
        if (!notPrime[i]) {
            for (j in i + i .. cap step i) {
                notPrime[j] = true
            }
        }
    }
    var (max, p, pc, q, qc) = iar(5)
    for (i in 0 until n) for (j in 0 until i) {
        val (a, b) = minOf(nums[i], nums[j]) to maxOf(nums[i], nums[j])
        val sum = a + b
        if (!notPrime[sum]) {
            val tot = if (a != 1 && b != 1) 2 else if (b == 1) cnt else cnt + 1
            if (tot > max) {
                max = tot
                p = a; q = b
                if (a != 1 && b != 1) {
                    pc = 1; qc = 1
                } else if (b == 1) {
                    pc = cnt
                } else {
                    pc = cnt; qc = 1
                }
            }
        }
        if (!notPrime[a] && max == 0) {
            max = 1; p = a
        }
        if (!notPrime[b] && max == 0) {
            max = 1; p = b
        }
    }
    if (max == 0) {
        println(1)
        println(nums[0])
    } else {
        println(max)
        println(buildString {
            if (max == 1) append("$p ")
            else if (p == q) repeat(pc) { append("$p ") }
            else {
                repeat(pc) { append("$p ") }
                repeat(qc) { append("$q ") }
            }
        })
    }
}