package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iao
import kotlin.math.abs

/**
 * @author tauros
 * 2024/2/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1685/B
    // 相邻为AA或BB时，至少选一个A或者一个B，所以根据相邻相同时分成若干段
    // 长度为2k时，AB...AB可以解决k个AB，BA...BA可以解决k个BA，或者解决AB + BA共k-1个
    // 长度为2k + 1时，AB...BA可以解决AB + BA共k个
    // 所以长度为偶数时浪费较少，先匹配长度更短的偶数个，然后匹配长度长的偶数个，然后再匹配奇数个
    // 只要把AB和BA匹配完了，剩下的自然就能选出够数的单个A或B
    val cases = rd.ni()
    repeat(cases) {
        val (a, b, c, d) = rd.na(4)
        val str = rd.ns(a + b + 2 * c + 2 * d)

        val cntA = str.count { it == 'A' }
        if (cntA != a + c + d) {
            wt.println("NO")
            return@repeat
        }

        val segments = buildList {
            var i = 0
            while (i < str.size) {
                add(buildString {
                    var j = i
                    while (j < str.size && (j == i || str[j] != str[j - 1])) {
                        append(str[j++])
                    }
                    i = j
                }.toCharArray())
            }
        }.sortedWith { x, y ->
            val (dx, dy) = abs(x.first() - x.last()) to abs(y.first() - y.last())
            if (dx != dy) return@sortedWith -dx.compareTo(dy)
            if (x.first() != y.first()) return@sortedWith x.first().compareTo(y.first())
            x.size.compareTo(y.size)
        }

        val rest = iao(c, d)
        for (seg in segments) {
            if (rest.sum() == 0) break
            if (seg.first() != seg.last()) {
                val idx = seg.first() - 'A'
                val cutIdx = minOf(rest[idx], seg.size / 2)
                rest[idx] -= cutIdx
                if (rest[idx] == 0) {
                    val cutOther = maxOf(0, (seg.size - (cutIdx + 1) * 2) / 2)
                    rest[1 - idx] -= minOf(rest[1 - idx], cutOther)
                }
            } else {
                val cnt = seg.size / 2
                val cutIdx = minOf(rest[0], cnt)
                rest[0] -= cutIdx
                val cutOther = maxOf(0, cnt - cutIdx)
                rest[1] -= minOf(rest[1], cutOther)
            }
        }
        wt.println(if (rest.sum() == 0) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}