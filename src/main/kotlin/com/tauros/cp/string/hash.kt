package com.tauros.cp.string

import kotlin.math.abs
import kotlin.random.Random


/**
 * @author tauros
 */
class SeqHash(val len: Int, val mod: Int = abs(Random.nextInt()) % Int.MAX_VALUE / 2 + 1, base: Int = 233, seqVal: (Int) -> Int) {
    private val pow = IntArray(len + 1)
    val hash = IntArray(len + 1)

    init {
        pow[0] = 1
        for (j in 1..len) {
            pow[j] = (pow[j - 1].toLong() * base % mod).toInt()
        }
        hash[0] = 0
        for (j in 1..len) {
            hash[j] = ((hash[j - 1].toLong() * base + seqVal(j - 1)) % mod).toInt()
        }
    }

    fun subHash(l: Int, r: Int): Int {
        val sub = if (l == 1) hash[r]
        else {
            hash[r] - (hash[l - 1].toLong() * pow[r - l + 1] % mod).toInt()
        }
        return if (sub < 0) sub + mod else sub
    }
}