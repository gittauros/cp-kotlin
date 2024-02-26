package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val a = rd.na(n - 1)

    val cap = n.takeHighestOneBit().countTrailingZeroBits() + 1
    val nodeCap = (cap + 1) * n + 1
    val trie = ar(2) { iar(nodeCap) }
    var idx = 1
    fun insert(num: int) {
        var iter = 0
        for (b in cap downTo 0) {
            val p = num shr b and 1
            if (trie[p][iter] == 0) {
                trie[p][iter] = idx++
            }
            iter = trie[p][iter]
        }
    }
    fun max(num: int): int {
        var ans = 0
        var iter = 0
        for (b in cap downTo 0) {
            val choose = num shr b and 1 xor 1
            val next = if (trie[choose][iter] != 0) {
                ans = 1 shl b xor ans
                choose
            } else 1 - choose
            iter = trie[next][iter]
        }
        return ans
    }

    val b = iar(n)
    for (i in 1 until n) {
        b[i] = b[i - 1] xor a[i - 1]
        insert(b[i])
    }
    for (i in 0 until n) {
        if (max(i) < n) {
            b[0] = i
            break
        }
    }

    for (i in 1 until n) b[i] = b[i] xor b[0]
    for (i in 0 until n) wt.print("${b[i]} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}