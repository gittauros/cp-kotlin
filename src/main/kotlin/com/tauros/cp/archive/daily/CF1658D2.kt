package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iao
import com.tauros.cp.iar
import kotlin.math.abs
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * @author tauros
 * 2023/12/12
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    val cap = 16
    repeat(cases) {
        val (l, r) = rd.ni() to rd.ni()
        val n = r - l + 1
        val nums = rd.na(n)
        if (n % 2 == 1) {
            val sum = (l..r).reduce(Int::xor)
            val res = nums.reduce(Int::xor)
            val ans = sum xor res
            wt.println(ans)
            return@repeat
        }

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
        for (num in nums) insert(num)

        fun min(num: int): int {
            var (ans, iter) = 0 to 0
            for (b in cap downTo 0) {
                val choose = num shr b and 1
                val next = if (trie[choose][iter] == 0) {
                    ans = 1 shl b xor ans
                    1 - choose
                } else choose
                iter = trie[next][iter]
            }
            return ans
        }
        fun max(num: int): int {
            var (ans, iter) = 0 to 0
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

        for (num in nums) {
            val find = l xor num
            val (cl, cr) = min(find) to max(find)
            if (cl == l && cr == r) {
                wt.println(find)
                return@repeat
            }
        }
    }
}


fun main(args: Array<String>) {
    solve()
    wt.flush()
}