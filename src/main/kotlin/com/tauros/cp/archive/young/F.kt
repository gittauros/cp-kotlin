package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.nextPermutation
import com.tauros.cp.common.string
import com.tauros.cp.iar
import com.tauros.cp.lar
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Random
import java.util.regex.Pattern
import kotlin.math.abs

/**
 * @author tauros
 * 2025/3/2
 */
private val bufCap = 65536
//private val rd = FastReader(System.`in`, bufCap)
//private val wt = FastWriter(System.out, bufCap)
private val mod = 1e9.toInt() + 7

private fun inv(a: Long, p: Long, invPModA: Long): Long {
    val ans = -p / a * invPModA % p
    return if (ans < 0) ans + p else ans
}
private fun inv(a: Int, p: Int, invPModA: Int) = inv(a.toLong(), p.toLong(), invPModA.toLong()).toInt()

private val cap = 1e5.toInt() + 1
private val fac = IntArray(cap + 1)
private val inv = IntArray(cap + 1)
private val facInv = IntArray(cap + 1)
private fun init() {
    fac[0] = 1; facInv[0] = 1
    for (i in 1..cap) {
        inv[i] = if (i == 1) 1 else inv(i, mod, inv[mod % i])
        fac[i] = (fac[i - 1].toLong() * i % mod).toInt()
        facInv[i] = (facInv[i - 1].toLong() * inv[i] % mod).toInt()
    }
}
private fun c(n: Int, m: Int): Int {
    return if (n == m) 1 else if (n !in 0..cap || m !in 0..n) 0 else {
        // C(n, m) = n! / m! / (n - m)!
        (fac[n].toLong() * facInv[m] % mod * facInv[n - m] % mod).toInt()
    }
}
private fun a(n: Int, m: Int): Int {
    return if (n !in 0..cap || m !in 0..n) 0 else {
        // A(n, m) = n! / (n - m)!
        (fac[n].toLong() * facInv[n - m] % mod).toInt()
    }
}

private fun mod(num: long): long {
    return (num % mod + mod) % mod
}

private fun solve(rd: FastReader, wt: FastWriter) {
    val (n, t) = rd.ni() to rd.ni()
    val cnts = rd.na(n)

    val pairSum = lar(n + 1)
    fun pairs(cnt: long) = mod((cnt - 1L) * cnt * inv[4])
    for (i in 1 .. n) {
        pairSum[i] = pairSum[i - 1]
        val pairs = pairs(cnts[i - 1].toLong())
        pairSum[i] = mod(pairs + pairSum[i])
    }
    val preSum = lar(n + 1)
    for (i in 1 .. n) {
        preSum[i] = preSum[i - 1]
        preSum[i] = preSum[i] + cnts[i - 1].toLong()
    }

    repeat(t) {
        val (l, r) = rd.nl() to rd.nl()
        if (l == r) {
            wt.println(0)
        } else {
            val len = (r - l + 1).toInt()
            val li = findFirst(n) { preSum[it] >= l }
            val ri = findFirst(n) { preSum[it] >= r }
            if (li == ri) {
                wt.println(0)
            } else {
                val lInCnt = mod(cnts[li - 1] - mod(l - 1 - preSum[li - 1]))
                val rInCnt = mod(r - preSum[ri - 1])
                val total = pairs(len.toLong())
                val inner = mod(pairSum[ri - 1] - pairSum[li])
                val exp = mod(mod(total - inner) - mod(pairs(lInCnt) + pairs(rInCnt)))
                val ans = mod(exp * fac[len])
                wt.println(ans)
            }
        }
    }
}

//private fun solve1(n: int, cnts: IntArray, queries: Array<IIP>): List<Long> {
//    var sufSum = cnts.last().toLong()
//    val sufPairSum = lar(n + 1)
//    for (i in n - 2 downTo 0) {
//        sufPairSum[i] = sufPairSum[i + 1]
//        sufPairSum[i] += mod(cnts[i].toLong() * sufSum)
//        sufPairSum[i] = mod(sufPairSum[i])
//        sufSum = mod(sufSum + cnts[i])
//    }
//    val preSum = lar(n + 1)
//    for (i in 1 .. n) {
//        preSum[i] = preSum[i - 1]
//        preSum[i] = mod(preSum[i] + cnts[i - 1])
//    }
//    return buildList {
//        for ((l, r) in queries) {
//            if (l == r) {
//                add(0)
//            } else {
//                val len = (r - l + 1).toInt()
//                val li = findFirst(n) { preSum[it] >= l }
//                val ri = findFirst(n) { preSum[it] >= r }
//                if (li == ri) {
//                   add(0)
//                } else {
//                    val lInCnt = mod(cnts[li - 1] - mod(l - 1 - preSum[li - 1]))
//                    val rInCnt = mod(r - preSum[ri - 1])
//                    val rOutCnt = mod(cnts[ri - 1] - rInCnt)
//                    val sufLPairs = mod(sufPairSum[li] + mod(lInCnt * (preSum[n] - preSum[li])))
//                    val outRInnerPairs = mod(sufPairSum[ri] + mod(rOutCnt * (preSum[n] - preSum[ri])))
//                    val outRToRangePairs = mod(mod((preSum[n] - preSum[ri]) * len) + mod(rOutCnt * (len - rInCnt)))
//                    val pairs = mod(sufLPairs - outRInnerPairs - outRToRangePairs)
//                    var ans = pairs
//                    ans = mod(ans * mod((len - 1L) * len / 2))
//                    ans = mod(ans * a(len - 2, len - 2))
//                    add(ans)
//                }
//            }
//        }
//    }
//}
//
//private fun solve2(n: int, cnts: IntArray, queries: Array<IIP>): List<Long> {
//    val sum = cnts.sum()
//    var (cur, j) = cnts[0] to 0
//    val realArray = iar(sum)
//    for (i in 0 until sum) {
//        if (i + 1 > cur) {
//            j++; cur += cnts[j]
//        }
//        realArray[i] = j + 1
//    }
//    fun IntArray.calc(): long {
//        var ans = 0L
//        val pos = iar(size) { it }
//        do {
//            val num = iar(size) { this[pos[it]] }
//            for (i in 0 until size) {
//                for (k in i + 1 until size) {
//                    if (num[i] > num[k]) {
//                        ans++
//                    }
//                }
//            }
//        } while (pos.nextPermutation())
//        return ans
//    }
//    return buildList {
//        for ((l, r) in queries) {
//            val sub = realArray.copyOfRange(l - 1, r)
//            var ans = sub.calc()
//            add(ans)
//        }
//    }
//}
//
//private fun solve3(n: int, cnts: IntArray, queries: Array<IIP>): List<Long> {
//    val pairSum = lar(n + 1)
//    fun pairs(cnt: long) = mod((cnt - 1L) * cnt * inv[4])
//    for (i in 1 .. n) {
//        pairSum[i] = pairSum[i - 1]
//        val pairs = pairs(cnts[i - 1].toLong())
//        pairSum[i] = mod(pairs + pairSum[i])
//    }
//    val preSum = lar(n + 1)
//    for (i in 1 .. n) {
//        preSum[i] = preSum[i - 1]
//        preSum[i] = mod(preSum[i] + cnts[i - 1])
//    }
//
//    return buildList {
//        for ((l, r) in queries) {
//            if (l == r) {
//                add(0)
//            } else {
//                val len = (r - l + 1).toInt()
//                val li = findFirst(n) { preSum[it] >= l }
//                val ri = findFirst(n) { preSum[it] >= r }
//                if (li == ri) {
//                    add(0)
//                } else {
//                    val lInCnt = mod(cnts[li - 1] - mod(l - 1 - preSum[li - 1]))
//                    val rInCnt = mod(r - preSum[ri - 1])
//                    val total = pairs(len.toLong())
//                    val inner = mod(pairSum[ri - 1] - pairSum[li])
//                    val exp = mod(mod(total - inner) - mod(pairs(lInCnt) + pairs(rInCnt)))
//                    val ans = mod(exp * fac[len])
//                    add(ans)
//                }
//            }
//        }
//    }
//}

fun main(args: Array<String>) {
    out@for (t in 1 .. 15) {
        init()
        val rd = FastReader(FileInputStream("/Users/tauros/Downloads/mess/mess" + t + ".in"), bufCap)
        val ansOut = ByteArrayOutputStream()
        val wt = FastWriter(ansOut, bufCap)
        solve(rd, wt)
        wt.flush()

        val ansBytes = ansOut.toByteArray()
        val ansIn = BufferedReader(InputStreamReader(ByteArrayInputStream(ansBytes)))
        val expIn = BufferedReader(InputStreamReader(FileInputStream("/Users/tauros/Downloads/mess/mess" + t + ".out")))
        val e = expIn.readText().split(Pattern.compile("\\s")).filter { it.isNotBlank() }
        val a = ansIn.readText().split(Pattern.compile("\\s")).filter { it.isNotBlank() }
        if (e.indices.any { e[it] != a[it] }) {
            println(t)
        }
    }
//    val random = Random()
//    while (true) {
//        val tot = abs(random.nextInt()) % 10 + 1
//        var rest = tot
//        val cnts = buildList {
//            while (rest > 0) {
//                val cnt = abs(random.nextInt()) % rest + 1
//                add(cnt)
//                rest -= cnt
//            }
//        }.toIntArray()
//        val n = cnts.size
//        val t = abs(random.nextInt()) % 5 + 1
//        val m = abs(random.nextInt()) % minOf(9, tot) + 1
//        val queries = buildList {
//            repeat(t) {
//                val x = abs(random.nextInt()) % tot + 1
//                val y = minOf(tot, x + abs(random.nextInt()) % m + 1)
//                add(minOf(x, y) to maxOf(x, y))
//            }
//        }.toTypedArray()
//        val ans1 = solve3(n, cnts, queries).toLongArray()
//        val ans2 = solve2(n, cnts, queries).toLongArray()
//        if ((0 until t).any { ans1[it] != ans2[it] }) {
//            wt.printf("%d %d\n", n, t)
//            wt.println(buildString {
//                for (cnt in cnts) {
//                    append(cnt); append(' ')
//                }
//            })
//            wt.print(buildString {
//                for ((l, r) in queries) {
//                    append(l)
//                    append(' ')
//                    append(r)
//                    append('\n')
//                }
//            })
//            break
//        } else {
////            println(ans1.joinToString())
//        }
//    }
//    wt.flush()
}