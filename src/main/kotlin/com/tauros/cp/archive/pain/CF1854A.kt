package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.dq
import com.tauros.cp.mlo
import kotlin.math.abs

/**
 * @author tauros
 * 2023/12/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1854/A2
    // 提示 1
    // 如果没有负数，可以通过 a[2]+=a[1], a[3]+=a[2], ..., a[n]+=a[n-1] 使数组非递减。
    // 如果没有正数，可以通过 a[n-1]+=a[n], a[n-2]+=a[n-1], ..., a[1]+=a[2] 使数组非递减。
    //
    // 提示 2
    // 不失一般性，假设 max(a) >= -min(a)。
    // 如果有负数，我们可以把负数都加上 max(a) 变成非负数，然后就可以按照提示 1 操作了。
    // 这种做法可以通过简单版本。
    //
    // 提示 3
    // 仍然假设 max(a) >= -min(a)。
    // 由于提示 1 的操作至多执行 n-1<=19 次，这意味着我们只剩下 31-19=12 次操作，能否全变成非负数或者非正数呢？
    // 大方向是：如果正数多，那么就把负数都变成非负数；如果负数多，那么就把正数都变成非正数。
    // 具体如何判断呢？
    //
    // 提示 4
    // 设有 x 个负数。
    // 如果 x <= 12，那么把负数全变成非负数即可。
    // 否则 x >= 13，只能把正数变成非正数。
    // 首先得弄出个很小的负数，使得 -min(a) >= max(a)。由于 2^5 = 32 > 20，对同一个负数至多翻倍 5 次即可做到。
    // 此时还剩下 12-5=7 次操作，刚好 n-x <= 20-13 = 7，我们可以把所有正数都变成非正数。
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        var (max, min) = -1 to -1
        var (neg, pos) = 0 to 0
        for ((i, num) in nums.withIndex()) {
            if (max == -1 || num > nums[max]) max = i
            if (min == -1 || num < nums[min]) min = i
            if (num < 0) neg++ else if (num > 0) pos++
        }
        val check = nums.clone()

        val ans = dq<IIP>()
        var allPos = true
        if (nums[max] >= -nums[min]) {
            if (neg <= 12) {
                for (i in 0 until n) if (nums[i] < 0) ans.addLast(i + 1 to max + 1)
            } else{
                allPos = false
                val idx = nums.indexOfFirst { it < 0 }
                repeat(5) { ans.addLast(idx + 1 to idx + 1) }
                for (i in 0 until n) if (nums[i] > 0) ans.addLast(i + 1 to idx + 1)
            }
        } else {
            if (pos <= 12) {
                allPos = false
                for (i in 0 until n) if (nums[i] > 0) ans.addLast(i + 1 to min + 1)
            } else {
                val idx = nums.indexOfFirst { it > 0 }
                repeat(5) { ans.addLast(idx + 1 to idx + 1) }
                for (i in 0 until n) if (nums[i] < 0) ans.addLast(i + 1 to idx + 1)
            }
        }

        if (allPos) {
            for (i in 2 .. n) ans.addLast(i to i - 1)
        } else {
            for (i in n downTo 2) ans.addLast(i - 1 to i)
        }

        for ((i, j) in ans) check[i - 1] += check[j - 1]
        for (i in 1 until n) if (check[i] < check[i - 1]) {
            throw IllegalStateException("fail")
        }

        wt.println(ans.size)
        for ((i, j) in ans) {
            wt.println("$i $j")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}