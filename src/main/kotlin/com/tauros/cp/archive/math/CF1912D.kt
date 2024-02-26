package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.mo

/**
 * @author tauros
 * 2023/12/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    // kind1 :
    // a[n] * pow[n] + a[n - 1] * pow[n - 1] ... a[1] * pow[1] + a[0] * pow[0]
    //
    // a[n] * pow[n] mod m + ... + a[0] * pow[0] mod m
    // a[k - 1]      mod m + ... + a[0]          mod m
    //
    // pow[k] mod m == 0
    //
    // kind2 :
    // a[n] * pow[n] + a[n - 1] * pow[n - 1] ... a[1] * pow[1] + a[0] * pow[0]
    //
    // a[n] * pow[n] mod m + ... + a[0] * pow[0] mod m
    // a[n]          mod m + ... + a[0]          mod m
    //
    // mod[i] 以k为周期
    //
    // kind3 :
    // a[n] * pow[n] + a[n - 1] * pow[n - 1] ... a[1] * pow[1] + a[0] * pow[0]
    //
    // a[n] * pow[n] mod m + ... + a[0] * pow[0] mod m
    //       ... -sum[2k-1 k] mod m + sum[k-1 0] mod m
    // example: b = 10 m = 7
    // pow[0] = 1 % 7 = 1
    // pow[1] = 10 % 7 = 3
    // pow[2] = 100 % 7 = 2
    // pow[3] = 1000 % 7 = 6
    // pow[4] = 10000 % 7 = 4
    // pow[5] = 100000 % 7 = 5
    // pow[6] = 1000000 % 7 = 1
    // mod[i] 以k为周期 若k为偶数 half = k / 2 那么有mods[i] + mods[i + half] = m
    repeat(cases) {
        val (b, m) = rd.ni() to rd.ni()
        val mods = iar(2 * m + 1)
        mods[0] = 1
        var t = -1
        for (i in 1 .. 2 * m) {
            mods[i] = (mods[i - 1] * b.toLong() % m).toInt()
            if (mods[i] == 0 && t == -1) {
                wt.println("1 $i")
                return@repeat
            }
            if (mods[i] == 1 && t == -1) t = i
        }
        if (t == -1) {
            wt.println(0)
            return@repeat
        }
        if (t % 2 == 0) {
            val half = t / 2
            var success = true
            for (i in half .. half + half) if (mods[i] + mods[i - half] != m) {
                success = false
                break
            }
            if (success) {
                wt.println("3 $half")
                return@repeat
            }
        }
        wt.println("2 $t")
    }
    // kind1: b pow k = 0 (mod m)
    // kind2: b pow k = 1 (mod m)
    // kind3: b pow k = -1 (mod m)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}