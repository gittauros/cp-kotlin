package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val nums = rd.na(n)
        if (k == 1 || k == n) {
            wt.println("YES")
            return@repeat
        }
        var (l, r) = k - 1 to k + 1
        var sum = nums[k - 1].toLong()
        var (lSum, rSum) = 0L to 0L
        while (l > 0 && r <= n) {
            val pre = sum
            while (l > 0 && sum + lSum + nums[l - 1] >= 0) {
                lSum += nums[l-- - 1]
                if (lSum >= 0) {
                    sum += lSum
                    lSum = 0
                }
            }
            while (r <= n && sum + rSum + nums[r - 1] >= 0) {
                rSum += nums[r++ - 1]
                if (rSum >= 0) {
                    sum += rSum
                    rSum = 0
                }
            }
            if (sum == pre && l > 0 && r <= n) {
                break
            }
        }
        wt.println(if (l == 0 && lSum + sum >= 0 || r == n + 1 && rSum + sum >= 0) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}