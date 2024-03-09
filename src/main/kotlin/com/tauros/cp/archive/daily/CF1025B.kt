package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.sqrt

/**
 * @author tauros
 * 2024/3/9
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1025/B
    // 如果某个数能满足wcd的条件，那么它的所有因数也能满足wcd的条件
    // 一个数最多有logn个质因数
    // 所以拿第一对的第一个数分解下质因数，判断下其它数对是否满足条件
    // 不满足的话换第一对的第二个数再试下
    // 都没找到就没有答案
    // 注意质因数分解完了后剩下的数还要处理下
    // 只对质因数做judge，虽然分解质因数是sqrtA的，但是只有其质因数会做n的判断，因此是nlogA的复杂度
    val n = rd.ni()
    val pairs = ar(n) { rd.na(2) }

    fun judge(wcd: int) = (1 until n).all { pairs[it][0] % wcd == 0 || pairs[it][1] % wcd == 0 }
    var ans = -1
    out@ for (num in pairs[0]) {
        var iter = num
        for (i in 2 .. sqrt(num)) {
            if (iter % i == 0) {
                if (judge(i)) {
                    ans = i
                    break@out
                }
            }
            while (iter % i == 0) {
                iter /= i
            }
        }
        if (iter > 1 && judge(iter)) {
            ans = iter
            break@out
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}