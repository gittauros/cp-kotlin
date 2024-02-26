package com.tauros.cp.math

import com.tauros.cp.structure.IntList
import com.tauros.cp.structure.toIntList


/**
 * @author tauros
 * 2023/9/24
 */
class Prime(val max: Int) : Iterable<Int> {
    private val minPrime = IntArray(max + 1)
    private val primes: IntArray

    init {
        val primeList = mutableListOf<Int>()
        for (i in 2..max) {
            if (minPrime[i] == 0) {
                minPrime[i] = i
                primeList.add(i)
            }
            var idx = 0
            while (true) {
                val prime = primeList[idx++]
                val num = prime * i
                if (num > max) {
                    break
                }
                minPrime[num] = prime
                if (i % prime == 0) {
                    break
                }
            }
        }
        primes = primeList.toIntArray()
    }

    fun factors(num: Int, sorted: Boolean = false): IntList {
        // 素因子分解求num的所有约数
        if (num > max) return IntList()
        if (num == 1) return intArrayOf(1).toIntList()
        if (num in this) return intArrayOf(1, num).toIntList()
        val pFacts = buildList {
            var iter = num
            while (iter > 1) {
                var cnt = 0
                val p = minPrime[iter]
                while (iter % p == 0) {
                    iter /= p
                    cnt += 1
                }
                add(p to cnt)
            }
        }
        val res = IntList()
        res.add(1)
        for ((p, cnt) in pFacts) repeat(cnt * res.size) { res.add(res[it] * p) }
        if (sorted) res.sort()
        return res
    }

    val size = primes.size
    operator fun get(idx: Int) = primes[idx]
    operator fun contains(num: Int) = num != 1 && num != 0 && min(num) == num
    override fun iterator() = primes.iterator()
    fun min(num: Int) = minPrime[num]
}

class Phi(val max: Int) : Iterable<Int> {
    private val phi = IntArray(max + 1)
    private val primes: IntArray

    init {
        val primeList = mutableListOf<Int>()
        for (i in 1..max) {
            phi[i] = i
        }
        for (i in 2..max) {
            if (phi[i] == i) {
                primeList.add(i)
                phi[i] = i - 1
            }
            var idx = 0
            while (true) {
                val prime: Int = primeList[idx++]
                val num = prime * i
                if (num > max) {
                    break
                }
                if (i % prime == 0) {
                    phi[num] = prime * phi[i]
                    break
                } else {
                    phi[num] = phi[prime] * phi[i]
                }
            }
        }
        primes = primeList.toIntArray()
    }

    fun isPrime(num: Int) = phi[num] == num - 1
    operator fun get(idx: Int) = phi[idx]
    override fun iterator() = phi.iterator()
}