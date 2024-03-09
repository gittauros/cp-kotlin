package com.tauros.cp.archive.bits

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.boolean
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/3/9
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/1934/problem/D1
    // 考虑 (n第i位, m第i位)
    // ① 遇到(1, 0)后，那么 x^y < x
    // ② 遇到(1, 1)后，那么 y < x
    // 遇到两次①或两次②，或者各一次，就可以制造出答案
    // 在达成条件前如果遇到一次(0, 1)，那么一定没有答案
    // 只要遇到了②，并且有答案，那么一定可以直接一次操作达成
    // 否则一定是遇到了两次①，记录两次遇到①的位置
    // 可以一次操作达成从第二次遇到①的位置开始到低位全部都是1
    // 然后就可以一个一个位置消除直到m
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.nal(2)

        var (y, xy, xy2) = iao(-1, -1, -1)
        fun judge(): boolean {
            for (b in 63 downTo 0) {
                val nb = 1L shl b and n
                val mb = 1L shl b and m
                if (nb != 0L && mb == 0L) {
                    if (xy != -1) {
                        xy2 = b
                        return true
                    }
                    xy = b
                }
                if (nb != 0L && mb != 0L) {
                    if (y != -1) return true
                    y = b
                }
                if (y != -1 && xy != -1) return true
                if (nb == 0L && mb != 0L) return false
            }
            return true
        }
        if (!judge()) {
            wt.println(-1)
            return@repeat
        }

        if (y != -1) {
            wt.println(1)
            wt.println("$n $m")
            return@repeat
        }

        var iter = (1L shl xy2 + 1) - 1
        val ans = buildList {
            while (iter != m) {
                add(iter)
                val diff = iter xor m
                iter = iter xor diff.takeHighestOneBit()
            }
            add(iter)
        }

        wt.println(ans.size)
        wt.print("$n ")
        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}