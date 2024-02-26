package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.gcd
import com.tauros.cp.iao

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
    // https://codeforces.com/problemset/problem/1848/C
    // Solution 2:
    // We will build the sequence from the end. Let's find the first moment when we obtain 0
    // Before this zero, there is some number d. It can be easily proven that d is exactly equal to gcd(ai,bi)
    // Now, let's divide each number in the sequence by d, and obtain a new sequence of numbers, where the last number is zero and the penultimate number is 1
    // Then, let's denote even numbers as 0. and odd numbers as 1.
    // In this way, the sequence can be uniquely reconstructed from the end: (0,1,1,0,1,1,0,1,1,...).
    // Thus, we can determine the remainder cnti modulo 3 by looking at the pair (ai/d,bi/d) modulo 2.
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val a = rd.na(n)
        val b = rd.na(n)

        var success = true
        val parity = iao(-1, -1)
        for (i in 0 until n) {
            if (a[i] == b[i] && a[i] == 0) continue
            val gcd = gcd(a[i], b[i])
            val (parityA, parityB) = a[i] / gcd % 2 to b[i] / gcd % 2
            // 0 1 -> 0 1 1 0 1 1 0 ...
            // 1 0 -> 1 0 1 1 0 1 1 0 ...
            // 1 1 -> 1 1 0 1 1 0 ...
            if (parity[0] != -1 && (parityA != parity[0] || parityB != parity[1])) {
                success = false
                break
            }
            parity[0] = parityA
            parity[1] = parityB
        }

        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}