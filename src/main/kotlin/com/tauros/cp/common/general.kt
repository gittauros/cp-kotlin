package com.tauros.cp.common

import com.tauros.cp.structure.maxHeapDown


/**
 * @author tauros
 * 2023/8/22
 */
inline fun IntArray.mergeSort(fromIndex: Int = 0, toIndex: Int = size, temp: IntArray = IntArray(toIndex - fromIndex), comparator: (Int, Int) -> Int) {
    val (base, len) = fromIndex to toIndex - fromIndex
    var (range, half) = 2 to 1
    while (half < len) {
        for (i in 0 until len step range) {
            val (st, mid, ed) = intArrayOf(i, minOf(i + half, len), minOf(i + range, len))
            var (l, r, iter) = intArrayOf(st, mid, st)
            while (l < mid || r < ed) {
                temp[iter++] =
                    if (r >= ed || l < mid && comparator(this[base + l], this[base + r]) <= 0) this[base + l++]
                    else this[base + r++]
            }
            for (j in st until ed) this[base + j] = temp[j]
        }
        half = range
        range = range shl 1
    }
}

inline fun LongArray.mergeSort(fromIndex: Int = 0, toIndex: Int = size, temp: LongArray = LongArray(toIndex - fromIndex), comparator: (Long, Long) -> Int) {
    val (base, len) = fromIndex to toIndex - fromIndex
    var (range, half) = 2 to 1
    while (half < len) {
        for (i in 0 until len step range) {
            val (st, mid, ed) = intArrayOf(i, minOf(i + half, len), minOf(i + range, len))
            var (l, r, iter) = intArrayOf(st, mid, st)
            while (l < mid || r < ed) {
                temp[iter++] =
                    if (r >= ed || l < mid && comparator(this[base + l], this[base + r]) <= 0) this[base + l++]
                    else this[base + r++]
            }
            for (j in st until ed) this[base + j] = temp[j]
        }
        half = range
        range = range shl 1
    }
}

inline fun IntArray.heapSort(from: Int, to: Int = size, comparator: (Int, Int) -> Int = Int::compareTo) {
    val len = to - from
    if (len <= 1) return
    for (i in (len - 1) / 2 downTo 0) {
        this.maxHeapDown(i, len, { it + from }, comparator)
    }
    for (i in len - 1 downTo 1) {
        this.swap(from, i + from)
        this.maxHeapDown(0, i, { it + from }, comparator)
    }
}

inline fun IntArray.heapSort(to: Int = size, comparator: (Int, Int) -> Int = Int::compareTo) {
    val len = to
    if (len <= 1) return
    for (i in (len - 1) / 2 downTo 0) {
        this.maxHeapDown(i, len, comparator)
    }
    for (i in len - 1 downTo 1) {
        this.swap(0, i)
        this.maxHeapDown(0, i, comparator)
    }
}

inline fun findFirst(l: Int, r: Int, judge: (Int) -> Boolean) = findFirst(r - l) { judge(it + l) } + l
inline fun findFirst(n: Int, judge: (Int) -> Boolean): Int {
    var l = 0
    var r = n
    while (l < r) {
        val mid = l + (r - l) / 2
        if (judge(mid)) r = mid
        else l = mid + 1
    }
    return l
}

inline fun findFirst(l: Long, r: Long, judge: (Long) -> Boolean) = findFirst(r - l) { judge(it + l) } + l
inline fun findFirst(n: Long, judge: (Long) -> Boolean): Long {
    var l: Long = 0
    var r = n
    while (l < r) {
        val mid = l + (r - l shr 1)
        if (judge(mid)) r = mid
        else l = mid + 1
    }
    return l
}

fun sqrt(n: Int) = sqrt(n.toLong()).toInt()
fun sqrt(n: Long): Long {
    if (n == 0L) return 0
    var x: Long = 1
    var decreased = false
    while (true) {
        val nx = x + n / x shr 1
        if (x == nx || nx > x && decreased) break
        decreased = nx < x
        x = nx
    }
    return x
}

// pow p int
fun pow(a: Double, p: Int): Double {
    var res = 1.0
    var base = a
    var rest = p
    while (rest > 0) {
        if (rest and 1 == 1) res *= base
        base *= base
        rest = rest shr 1
    }
    return res
}

fun pow(a: Int, p: Int) = pow(a.toLong(), p).toInt()
fun pow(a: Long, p: Int): Long {
    var res = 1L
    var base = a
    var rest = p
    while (rest > 0) {
        if (rest and 1 == 1) res *= base
        base *= base
        rest = rest shr 1
    }
    return res
}

fun pow(a: Int, p: Int, mod: Int) = pow(a.toLong(), p, mod)
fun pow(a: Long, p: Int, mod: Int): Int {
    var res = 1L
    var base = a
    var rest = p
    while (rest > 0) {
        if (rest and 1 == 1) res = res * base % mod
        base = base * base % mod
        rest = rest shr 1
    }
    return res.toInt()
}

// pow p long
fun pow(a: Double, p: Long): Double {
    var res = 1.0
    var base = a
    var rest = p
    while (rest > 0) {
        if (rest and 1L == 1L) res *= base
        base *= base
        rest = rest shr 1
    }
    return res
}

fun pow(a: Int, p: Long) = pow(a.toLong(), p).toInt()
fun pow(a: Long, p: Long): Long {
    var res = 1L
    var base = a
    var rest = p
    while (rest > 0) {
        if (rest and 1L == 1L) res *= base
        base *= base
        rest = rest shr 1
    }
    return res
}

fun pow(a: Int, p: Long, mod: Int) = pow(a.toLong(), p, mod)
fun pow(a: Long, p: Long, mod: Int): Int {
    var res = 1L
    var base = a
    var rest = p
    while (rest > 0) {
        if (rest and 1L == 1L) res = res * base % mod
        base = base * base % mod
        rest = rest shr 1
    }
    return res.toInt()
}

tailrec fun gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

tailrec fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Int, b: Int): Int = (a.toLong() / gcd(a, b) * b).toInt()

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

/**
 * calc ax + by = gcd(a, b)
 * x = x0 + k * (b/gcd(a,b)); y = y0 - k * (a/gcd(a,b))
 * calc ax + by = o * gcd(a, b) -> calc a * o * x' + b * o * y' = o * gcd(a, b)
 * x = x0 * o + k * (b/gcd(a,b)); y = y0 * o - k * (a/gcd(a,b))
 */
fun exgcd(a: Int, b: Int): IntArray {
    // gcd, x, y
    if (b == 0) return intArrayOf(a, 1, 0)
    // x = y0; y = x0 - (a / b) * y0;
    val (gcd, x0, y0) = exgcd(b, a % b)
    return intArrayOf(gcd, y0, x0 - a / b * y0)
}

/**
 * calc ax + by = gcd(a, b)
 * x = x0 + k * (b/gcd(a,b)); y = y0 - k * (a/gcd(a,b))
 * calc ax + by = o * gcd(a, b) -> calc a * o * x' + b * o * y' = o * gcd(a, b)
 * x = x0 * o + k * (b/gcd(a,b)); y = y0 * o - k * (a/gcd(a,b))
 */
fun exgcd(a: Long, b: Long): LongArray {
    // gcd, x, y
    if (b == 0L) return longArrayOf(a, 1, 0)
    // x = y0; y = x0 - (a / b) * y0;
    val (gcd, x0, y0) = exgcd(b, a % b)
    return longArrayOf(gcd, y0, x0 - a / b * y0)
}

fun inv(a: Int, p: Int): Int {
    val (gcd, inv, _) = exgcd(a, p)
    return if (gcd != 1) -1 else (inv + p) % p
}

fun inv(a: Long, p: Long): Long {
    val (gcd, inv, _) = exgcd(a, p)
    return if (gcd != 1L) -1 else (inv + p) % p
}

fun inv(a: Int, p: Int, invPModA: Int) = inv(a.toLong(), p.toLong(), invPModA.toLong()).toInt()
fun inv(a: Long, p: Long, invPModA: Long): Long {
    val ans = -p / a * invPModA % p
    return if (ans < 0) ans + p else ans
}

@Suppress("UNCHECKED_CAST")
operator fun <T : Number> T.plus(other: T): T {
    return when (this) {
        is Int  -> ((this as Int) + (other as Int)) as T
        is Long -> ((this as Long) + (other as Long)) as T
        else    -> throw IllegalStateException("unsupported")
    }
}

@Suppress("UNCHECKED_CAST")
operator fun <T : Number> T.minus(other: T): T {
    return when (this) {
        is Int  -> ((this as Int) - (other as Int)) as T
        is Long -> ((this as Long) - (other as Long)) as T
        else    -> throw IllegalStateException("unsupported")
    }
}

@Suppress("UNCHECKED_CAST")
operator fun <T : Number> T.times(other: T): T {
    return when (this) {
        is Int  -> ((this as Int) * (other as Int)) as T
        is Long -> ((this as Long) * (other as Long)) as T
        else    -> throw IllegalStateException("unsupported")
    }
}

@Suppress("UNCHECKED_CAST")
operator fun <T : Number> T.div(other: T): T {
    return when (this) {
        is Int  -> ((this as Int) / (other as Int)) as T
        is Long -> ((this as Long) / (other as Long)) as T
        else    -> throw IllegalStateException("unsupported")
    }
}

operator fun <T : Number> T.compareTo(other: T): Int {
    return when (this) {
        is Int  -> (this as Int).compareTo(other as Int)
        is Long -> (this as Long).compareTo(other as Long)
        else    -> throw IllegalStateException("unsupported")
    }
}

operator fun <T : Number> T.compareTo(other: Int): Int {
    return when (this) {
        is Int  -> (this as Int).compareTo(other)
        is Long -> (this as Long).compareTo(other)
        else    -> throw IllegalStateException("unsupported")
    }
}

fun <T : Number> minOf(a: T, b: T): T {
    return if (a <= b) a else b
}

typealias TP<T> = Pair<T, T>
typealias IIP = Pair<Int, Int>
typealias ILP = Pair<Int, Long>
typealias LLP = Pair<Long, Long>
typealias LIP = Pair<Long, Int>
typealias boolean = Boolean
typealias int = Int
typealias long = Long
typealias double = Double
typealias char = Char
typealias string = String
typealias mint = MInt
typealias MList<T> = MutableList<T>
typealias MSet<T> = MutableSet<T>
typealias MMap<K, V> = MutableMap<K, V>
typealias DQ<T> = ArrayDeque<T>

operator fun <T : Number> TP<T>.plus(other: TP<T>): TP<T> = this.first + other.first to this.second + other.second