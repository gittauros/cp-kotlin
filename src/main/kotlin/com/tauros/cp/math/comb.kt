package com.tauros.cp.math

import com.tauros.cp.common.MOD
import com.tauros.cp.common.inv
import com.tauros.cp.common.toMInt


/**
 * @author tauros
 */
open class Comb(val cap: Int, val mod: Int) {
    val fac = IntArray(cap + 1)
    val inv = IntArray(cap + 1)
    val facInv = IntArray(cap + 1)

    init {
        fac[0] = 1
        facInv[0] = 1
        for (i in 1..cap) {
            inv[i] = if (i == 1) 1 else inv(i, mod, inv[mod % i])
            fac[i] = (fac[i - 1].toLong() * i % mod).toInt()
            facInv[i] = (facInv[i - 1].toLong() * inv[i] % mod).toInt()
        }
    }

    fun c(n: Int, m: Int): Int {
        return if (n == m) 1 else if (n !in 0..cap || m !in 0..n) 0 else {
            // C(n, m) = n! / m! / (n - m)!
            (fac[n].toLong() * facInv[m] % mod * facInv[n - m] % mod).toInt()
        }
    }

    fun a(n: Int, m: Int): Int {
        return if (n !in 0..cap || m !in 0..n) 0 else {
            // A(n, m) = n! / (n - m)!
            (fac[n].toLong() * facInv[n - m] % mod).toInt()
        }
    }
}

class ModComb(val cap: Int) {
    private val comb = Comb(cap, MOD.globalMod)
    fun c(n: Int, m: Int) = comb.c(n, m).toMInt()
    fun a(n: Int, m: Int) = comb.a(n, m).toMInt()
}

/**
 * n个物品中固定选m个的子集mask
 */
inline fun gospersHack(n: Int, m: Int, calc: (Int) -> Unit) {
    val maxState = 1 shl n
    var iter = (1 shl m) - 1
    while (iter < maxState) {
        calc(iter)

        val lb = iter.takeLowestOneBit()
        val r = iter + lb
        iter = r or (r xor iter shr 2) / lb
    }
}