package com.tauros.cp.common


/**
 * @author tauros
 * 2023/8/28
 */
// MOD
class MOD {
    companion object {
        private val INV_MAP = mutableMapOf<Int, MutableMap<Int, Int>>()
        private val INV_LONG_MAP = mutableMapOf<Int, MutableMap<Long, Int>>()
        var globalMod: Int = 0
        fun inv(num: Int): Int = INV_MAP.computeIfAbsent(globalMod) { mutableMapOf() }.computeIfAbsent(num) { inv(num, globalMod) }
        fun inv(num: Long): Int = INV_LONG_MAP.computeIfAbsent(globalMod) { mutableMapOf() }.computeIfAbsent(num) { inv(num, globalMod.toLong()).toInt() }
        fun pow(num: Long, p: Long): Int {
            var (pow, a) = p to num
            if (a >= globalMod) a %= globalMod.toLong()
            var res: Long = 1
            while (pow > 0) {
                if (pow and 1 == 1L) res = res * a % globalMod
                a = a * a % globalMod
                pow = pow shr 1
            }
            return res.toInt()
        }
        fun regular(num: Int): Int = if (num in 0 until globalMod) num else {
            val r = num % globalMod
            if (r < 0) r + globalMod else r
        }
        fun regular(num: Long): Int = if (num in 0 until globalMod) num.toInt() else {
            val r = (num % globalMod).toInt()
            if (r < 0) r + globalMod else r
        }
        fun add(a: Int, b: Int) = regular(a + b)
        fun sub(a: Int, b: Int) = regular(a - b)
        fun mul(a: Int, b: Int) = regular(a.toLong() * b)
        fun div(a: Int, b: Int) = regular(a.toLong() * inv(regular(b)))
        inline fun <T> decorate(mod: Int, process: Companion.() -> T): T {
            val preGlobal = globalMod
            globalMod = mod
            val res = process(Companion)
            globalMod = preGlobal
            return res
        }
    }
}

inline fun <T> withMod(mod: Int, process: MOD.Companion.() -> T) = MOD.decorate(mod, process)

infix fun Int.ma(other: Int): Int = MOD.add(this, other)
infix fun Int.ms(other: Int): Int = MOD.sub(this, other)
infix fun Int.md(other: Int): Int = MOD.div(this, other)
infix fun Int.mm(other: Int): Int = MOD.mul(this, other)

infix fun Int.ma(other: Long): Int = MOD.add(this, MOD.regular(other))
infix fun Int.ms(other: Long): Int = MOD.sub(this, MOD.regular(other))
infix fun Int.md(other: Long): Int = MOD.div(this, MOD.regular(other))
infix fun Int.mm(other: Long): Int = MOD.mul(this, MOD.regular(other))

infix fun Long.ma(other: Int): Int = MOD.add(MOD.regular(this), other)
infix fun Long.ms(other: Int): Int = MOD.sub(MOD.regular(this), other)
infix fun Long.md(other: Int): Int = MOD.div(MOD.regular(this), other)
infix fun Long.mm(other: Int): Int = MOD.mul(MOD.regular(this), other)

infix fun Long.ma(other: Long): Int = MOD.add(MOD.regular(this), MOD.regular(other))
infix fun Long.ms(other: Long): Int = MOD.sub(MOD.regular(this), MOD.regular(other))
infix fun Long.md(other: Long): Int = MOD.div(MOD.regular(this), MOD.regular(other))
infix fun Long.mm(other: Long): Int = MOD.mul(MOD.regular(this), MOD.regular(other))


// MInt
class MInt(raw: Int) {
    companion object {
        val ZERO = MInt(0)
        val ONE = MInt(1)
    }
    val num = MOD.regular(raw)
    operator fun plus(other: MInt): MInt = MInt(num ma other.num)
    operator fun minus(other: MInt): MInt = MInt(num ms other.num)
    operator fun times(other: MInt): MInt = MInt(num mm other.num)
    operator fun div(other: MInt): MInt = MInt(num md other.num)
    operator fun plus(other: Int): MInt = MInt(num ma other)
    operator fun minus(other: Int): MInt = MInt(num ms other)
    operator fun times(other: Int): MInt = MInt(num mm other)
    operator fun div(other: Int): MInt = MInt(num md other)
    operator fun plus(other: Long): MInt = MInt(num ma other)
    operator fun minus(other: Long): MInt = MInt(num ms other)
    operator fun times(other: Long): MInt = MInt(num mm other)
    operator fun div(other: Long): MInt = MInt(num md other)
    fun pow(exp: Int) = MOD.pow(num.toLong(), exp.toLong()).toMInt()
    fun pow(exp: Long) = MOD.pow(num.toLong(), exp).toMInt()
    fun toInt() = num
    override fun toString() = num.toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MInt
        return num == other.num
    }
    override fun hashCode() = num.hashCode()
    operator fun compareTo(other: MInt): Int = num.compareTo(other.num)
    operator fun compareTo(other: Int): Int = num.compareTo(other)
    operator fun compareTo(other: Long): Int = num.compareTo(other)
}

fun Int.toMInt(): MInt = MInt(this)
fun Long.toMInt(): MInt = MInt(MOD.regular(this))
operator fun Int.compareTo(other: MInt): Int = this.compareTo(other.num)
operator fun Long.compareTo(other: MInt): Int = this.compareTo(other.num)

operator fun Int.plus(other: MInt): MInt = MInt(this ma other.num)
operator fun Int.minus(other: MInt): MInt = MInt(this ms other.num)
operator fun Int.times(other: MInt): MInt = MInt(this mm other.num)
operator fun Int.div(other: MInt): MInt = MInt(this md other.num)

operator fun Long.plus(other: MInt): MInt = MInt(this ma other.num)
operator fun Long.minus(other: MInt): MInt = MInt(this ms other.num)
operator fun Long.times(other: MInt): MInt = MInt(this mm other.num)
operator fun Long.div(other: MInt): MInt = MInt(this md other.num)


// MIntArray
class MIntArray(size: Int, init: (Int) -> Int = { 0 }) : Iterable<MInt>, Cloneable {
    val raw = IntArray(size, init)
    val size = raw.size

    constructor(array: IntArray) : this(array.size, { array[it] })

    operator fun get(index: Int): MInt = raw[index].toMInt()

    operator fun set(index: Int, value: MInt) {
        raw[index] = value.num
    }

    override fun iterator(): Iterator<MInt> {
        return object : Iterator<MInt> {
            private var idx = 0

            override fun hasNext(): Boolean {
                return idx < size
            }

            override fun next(): MInt {
                return raw[idx++].toMInt()
            }
        }
    }

    public override fun clone(): MIntArray {
        return MIntArray(raw.clone())
    }
}

val MIntArray.lastIndex: Int
    get() = size - 1

val MIntArray.indices: IntRange
    get() = IntRange(0, lastIndex)

fun MIntArray.fill(element: Int, fromIndex: Int = 0, toIndex: Int = size) {
    java.util.Arrays.fill(this.raw, fromIndex, toIndex, element)
}

fun MIntArray.sum(): MInt {
    var res = 0
    for (num in this.raw) res = res ma num
    return res.toMInt()
}

fun IntArray.copyInto(destination: MIntArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): MIntArray {
    System.arraycopy(this, startIndex, destination.raw, destinationOffset, endIndex - startIndex)
    return destination
}

fun MIntArray.copyInto(destination: MIntArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): MIntArray {
    System.arraycopy(this.raw, startIndex, destination.raw, destinationOffset, endIndex - startIndex)
    return destination
}

fun MIntArray.copyInto(destination: IntArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): IntArray {
    System.arraycopy(this.raw, startIndex, destination, destinationOffset, endIndex - startIndex)
    return destination
}
