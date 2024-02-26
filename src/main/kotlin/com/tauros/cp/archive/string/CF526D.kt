package com.tauros.cp.archive.string

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.string.zfunc

/**
 * @author tauros
 * 2024/1/31
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/526/D
    // 用zfunc来nlogn跳前缀了
    // 其实可以用kmp找border，border超过一半时，s.len - 最大border = 最小s周期，这样可以On
    val (n, k) = rd.ni() to rd.ni()
    val str = rd.ns(n)

    val z = str.zfunc()
    val ans = iar(n)
    for (len in 1 .. n / k) {
        var (iter, cnt) = len to 1
        while (iter < n && z[iter] >= len && cnt < k) {
            cnt += 1
            iter += len
        }
        if (cnt < k) continue
        val st = k * len
        val range = minOf(if (st >= n) 0 else z[st], len)
        ans[st - 1] += 1
        if (st + range < n) ans[st + range] -= 1
    }
    for (i in 1 until n) ans[i] += ans[i - 1]
    for (i in ans.indices) ans[i] = if (ans[i] > 0) 1 else 0
    wt.println(ans.joinToString(""))
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}