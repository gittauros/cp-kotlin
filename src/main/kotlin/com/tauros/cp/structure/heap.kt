package com.tauros.cp.structure

import com.tauros.cp.common.swap


/**
 * @author tauros
 * 2023/9/25
 */
inline fun IntArray.maxHeapDown(pos: Int, end: Int, idMapping: (Int) -> Int = { it }, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var i = pos
    while (i < end) {
        val l = (i shl 1) + 1
        val r = (i shl 1) + 2
        if (l >= end) {
            break
        }
        val next = if (r >= end) l else {
            if (comparator(this[idMapping(l)], this[idMapping(r)]) > 0) l else r
        }
        val mappingI = idMapping(i)
        val mappingNext = idMapping(next)
        if (comparator(this[mappingI], this[mappingNext]) >= 0) {
            break
        }
        this.swap(mappingI, mappingNext)
        i = next
    }
}

inline fun IntArray.maxHeapUp(pos: Int, idMapping: (Int) -> Int = { it }, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    var mappingIter = idMapping(pos)
    val value = this[mappingIter]
    var mappingParent: Int
    var parent: Int
    var parentValue: Int
    while (iter > 0) {
        parent = iter - 1 shr 1
        mappingParent = idMapping(parent)
        parentValue = this[mappingParent]
        if (comparator(value, parentValue) <= 0) break
        this[mappingIter] = parentValue
        iter = parent
        mappingIter = idMapping(iter)
    }
    this[mappingIter] = value
}

inline fun IntArray.maxHeapDown(pos: Int, end: Int, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    while (iter < end) {
        val l = (iter shl 1) + 1
        val r = (iter shl 1) + 2
        if (l >= end) break
        val next = if (r >= end || comparator(this[l], this[r]) >= 0) l else r
        if (comparator(this[iter], this[next]) >= 0) break
        this.swap(iter, next)
        iter = next
    }
}

inline fun IntArray.maxHeapUp(pos: Int, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    val value = this[pos]
    var parent: Int
    var parentValue: Int
    while (iter > 0) {
        parent = iter - 1 shr 1
        parentValue = this[parent]
        if (comparator(value, parentValue) <= 0) break
        this[iter] = parentValue
        iter = parent
    }
    this[iter] = value
}

inline fun IntArray.minHeapDown(pos: Int, end: Int, idMapping: (Int) -> Int = { it }, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var i = pos
    while (i < end) {
        val l = (i shl 1) + 1
        val r = (i shl 1) + 2
        if (l >= end) {
            break
        }
        val next = if (r >= end) l else {
            if (comparator(this[idMapping(l)], this[idMapping(r)]) < 0) l else r
        }
        val mappingI = idMapping(i)
        val mappingNext = idMapping(next)
        if (comparator(this[mappingI], this[mappingNext]) <= 0) {
            break
        }
        this.swap(mappingI, mappingNext)
        i = next
    }
}

inline fun IntArray.minHeapUp(pos: Int, idMapping: (Int) -> Int = { it }, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    var mappingIter = idMapping(pos)
    val value = this[mappingIter]
    var mappingParent: Int
    var parent: Int
    var parentValue: Int
    while (iter > 0) {
        parent = iter - 1 shr 1
        mappingParent = idMapping(parent)
        parentValue = this[mappingParent]
        if (comparator(value, parentValue) >= 0) break
        this[mappingIter] = parentValue
        iter = parent
        mappingIter = idMapping(iter)
    }
    this[mappingIter] = value
}

inline fun IntArray.minHeapDown(pos: Int, end: Int, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    while (iter < end) {
        val l = (iter shl 1) + 1
        val r = (iter shl 1) + 2
        if (l >= end) break
        val next = if (r >= end || comparator(this[l], this[r]) <= 0) l else r
        if (comparator(this[iter], this[next]) <= 0) break
        this.swap(iter, next)
        iter = next
    }
}

inline fun IntArray.minHeapUp(pos: Int, comparator: (Int, Int) -> Int = Int::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    val value = this[pos]
    var parent: Int
    while (iter > 0) {
        parent = iter - 1 shr 1
        if (comparator(value, this[parent]) >= 0) break
        this[iter] = this[parent]
        iter = parent
    }
    this[iter] = value
}

class IntHeap(var idxes: IntArray, private val comparator: (Int, Int) -> Int = Int::compareTo) : Collection<Int> {
    companion object {
        val MIN_CAP = 10
        val MAX_CAP = 1e8.toInt()
    }

    var cap = idxes.size
    var idx = 0

    constructor(cap: Int = MIN_CAP, comparator: (Int, Int) -> Int = Int::compareTo) : this(IntArray(maxOf(MIN_CAP, cap)), comparator)

    fun newCapacity(old: Int) = minOf(MAX_CAP, maxOf(MIN_CAP, old + (old shr 1)))

    fun build() {
        for (i in (cap - 1) / 2 downTo 0) {
            idxes.minHeapDown(i, cap, comparator = comparator)
        }
        idx = cap
    }

    inline fun build(comparator: (Int, Int) -> Int) {
        for (i in (cap - 1) / 2 downTo 0) {
            idxes.minHeapDown(i, cap, comparator = comparator)
        }
        idx = cap
    }

    fun poll(): Int = idxes[0].also {
        idxes[0] = idxes[--idx]
        idxes.minHeapDown(0, idx, comparator = comparator)
    }

    inline fun poll(comparator: (Int, Int) -> Int): Int = idxes[0].also {
        idxes[0] = idxes[--idx]
        idxes.minHeapDown(0, idx, comparator = comparator)
    }

    fun offer(num: Int) {
        if (idx >= cap) {
            val newCap = newCapacity(cap)
            idxes = idxes.copyOf(newCap)
            cap = newCap
        }
        idxes[idx++] = num
        idxes.minHeapUp(idx - 1, comparator = comparator)
    }

    inline fun offer(num: Int, comparator: (Int, Int) -> Int) {
        if (idx >= cap) {
            val newCap = newCapacity(cap)
            idxes += IntArray(newCap - cap)
        }
        idxes[idx++] = num
        idxes.minHeapUp(idx - 1, comparator = comparator)
    }

    fun peek(): Int = idxes[0]

    fun clear() {
        idx = 0
    }

    override val size: Int get() = idx
    override fun contains(element: Int) = (0 until idx).any { element == idxes[it] }
    override fun containsAll(elements: Collection<Int>) = elements.all { contains(it) }
    override fun isEmpty() = size <= 0
    override fun iterator() = object : Iterator<Int> {
        var cur = 0
        override fun hasNext() = cur < size
        override fun next() = idxes[cur++]
    }
}

inline fun LongArray.maxHeapDown(pos: Int, end: Int, comparator: (Long, Long) -> Int = Long::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    while (iter < end) {
        val l = (iter shl 1) + 1
        val r = (iter shl 1) + 2
        if (l >= end) break
        val next = if (r >= end || comparator(this[l], this[r]) >= 0) l else r
        if (comparator(this[iter], this[next]) >= 0) break
        this.swap(iter, next)
        iter = next
    }
}

inline fun LongArray.maxHeapUp(pos: Int, comparator: (Long, Long) -> Int = Long::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    val value = this[pos]
    var parent: Int
    var parentValue: Long
    while (iter > 0) {
        parent = iter - 1 shr 1
        parentValue = this[parent]
        if (comparator(value, parentValue) <= 0) break
        this[iter] = parentValue
        iter = parent
    }
    this[iter] = value
}

inline fun LongArray.minHeapDown(pos: Int, end: Int, comparator: (Long, Long) -> Int = Long::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    while (iter < end) {
        val l = (iter shl 1) + 1
        val r = (iter shl 1) + 2
        if (l >= end) break
        val next = if (r >= end || comparator(this[l], this[r]) <= 0) l else r
        if (comparator(this[iter], this[next]) <= 0) break
        this.swap(iter, next)
        iter = next
    }
}

inline fun LongArray.minHeapUp(pos: Int, comparator: (Long, Long) -> Int = Long::compareTo) {
    if (this.isEmpty()) return
    var iter = pos
    val value = this[pos]
    var parent: Int
    while (iter > 0) {
        parent = iter - 1 shr 1
        if (comparator(value, this[parent]) >= 0) break
        this[iter] = this[parent]
        iter = parent
    }
    this[iter] = value
}

class LongHeap(var nums: LongArray, private val comparator: (Long, Long) -> Int = Long::compareTo) : Collection<Long> {
    companion object {
        val MIN_CAP = 10
        val MAX_CAP = 1e8.toInt()
    }

    var cap = nums.size
    var idx = 0

    constructor(cap: Int = MIN_CAP, comparator: (Long, Long) -> Int = Long::compareTo) : this(LongArray(maxOf(MIN_CAP, cap)), comparator)

    fun newCapacity(old: Int) = minOf(MAX_CAP, maxOf(MIN_CAP, old + (old shr 1)))

    fun build() {
        for (i in (cap - 1) / 2 downTo 0) {
            nums.minHeapDown(i, cap, comparator = comparator)
        }
        idx = cap
    }

    inline fun build(comparator: (Long, Long) -> Int) {
        for (i in (cap - 1) / 2 downTo 0) {
            nums.minHeapDown(i, cap, comparator = comparator)
        }
        idx = cap
    }

    fun poll(): Long = nums[0].also {
        nums[0] = nums[--idx]
        nums.minHeapDown(0, idx, comparator = comparator)
    }

    inline fun poll(comparator: (Long, Long) -> Int): Long = nums[0].also {
        nums[0] = nums[--idx]
        nums.minHeapDown(0, idx, comparator = comparator)
    }

    fun offer(num: Long) {
        if (idx >= cap) {
            val newCap = newCapacity(cap)
            nums = nums.copyOf(newCap)
            cap = newCap
        }
        nums[idx++] = num
        nums.minHeapUp(idx - 1, comparator = comparator)
    }

    inline fun offer(num: Long, comparator: (Long, Long) -> Int) {
        if (idx >= cap) {
            val newCap = newCapacity(cap)
            nums += LongArray(newCap - cap)
        }
        nums[idx++] = num
        nums.minHeapUp(idx - 1, comparator = comparator)
    }

    fun peek(): Long = nums[0]

    fun clear() {
        idx = 0
    }

    override val size: Int get() = idx
    override fun contains(element: Long) = (0 until idx).any { element == nums[it] }
    override fun containsAll(elements: Collection<Long>) = elements.all { contains(it) }
    override fun isEmpty() = size <= 0
    override fun iterator() = object : Iterator<Long> {
        var cur = 0
        override fun hasNext() = cur < size
        override fun next() = nums[cur++]
    }
}

// general heap (min heap)

abstract class Heap<T>(var cap: Int = MIN_CAP) {
    companion object {
        val MIN_CAP = 10
        val MAX_CAP = 1e8.toInt()
    }

    protected var idx: Int = 0

    protected fun newCapacity(old: Int) = minOf(MAX_CAP, maxOf(MIN_CAP, old + (old shr 1)))

    fun offer(value: T) {
        if (idx >= cap) {
            val newCap = newCapacity(cap)
            expand(newCap - cap)
            cap = newCap
        }
        doOffer(value)
        afterOffer()
    }

    fun poll() = peek().also { afterPoll() }

    protected fun afterPoll() {
        swap(0, --idx)
        if (isEmpty()) return
        var iter = 0
        while (iter < idx) {
            val l = (iter shl 1) + 1
            val r = (iter shl 1) + 2
            if (l >= idx) break
            val next = if (r >= idx || compare(l, r) <= 0) l else r
            if (compare(iter, next) <= 0) break
            this.swap(iter, next)
            iter = next
        }
    }

    protected fun afterOffer() {
        var iter = idx++
        while (iter > 0) {
            val parent = (iter - 1) / 2
            if (compare(iter, parent) > 0) {
                break
            }
            swap(iter, parent)
            iter = parent
        }
    }

    abstract fun peek(): T
    protected abstract fun expand(increment: Int)
    protected abstract fun swap(p: Int, q: Int)
    protected abstract fun compare(indexA: Int, indexB: Int): Int
    protected abstract fun doOffer(new: T)
    val size: Int get() = idx
    fun isEmpty() = size <= 0
    fun isNotEmpty() = !isEmpty()
    fun clear() {
        idx = 0
    }
}

class KHeap<K>(initCap: Int = MIN_CAP, val keyComparator: (K, K) -> Int)
    : Heap<K>(initCap) {
    private var key = Array<Any?>(cap) { null }

    @Suppress("UNCHECKED_CAST")
    override fun peek() = key[0] as K

    override fun expand(increment: Int) {
        val newKeyArray = arrayOfNulls<Any?>(key.size + increment)
        key.copyInto(newKeyArray, 0, 0, key.size)
        key = newKeyArray
    }

    override fun swap(p: Int, q: Int) {
        key.swap(p, q)
    }

    @Suppress("UNCHECKED_CAST")
    override fun compare(indexA: Int, indexB: Int) = keyComparator(key[indexA] as K, key[indexB] as K)

    override fun doOffer(new: K) {
        key[idx] = new
    }
}

class KIHeap<K>(initCap: Int = MIN_CAP, val keyComparator: (K, K) -> Int)
    : Heap<Pair<K, Int>>(initCap) {
    private var key = Array<Any?>(cap) { null }
    private var value = IntArray(cap)

    @Suppress("UNCHECKED_CAST")
    override fun peek() = key[0] as K to value[0]

    override fun expand(increment: Int) {
        val newKeyArray = arrayOfNulls<Any?>(key.size + increment)
        key.copyInto(newKeyArray, 0, 0, key.size)
        key = newKeyArray
        value += IntArray(increment)
    }

    override fun swap(p: Int, q: Int) {
        key.swap(p, q)
        value.swap(p, q)
    }

    @Suppress("UNCHECKED_CAST")
    override fun compare(indexA: Int, indexB: Int) = keyComparator(key[indexA] as K, key[indexB] as K)

    override fun doOffer(new: Pair<K, Int>) {
        key[idx] = new.first
        value[idx] = new.second
    }
}

class IIHeap(initCap: Int = MIN_CAP, val keyComparator: (Int, Int) -> Int = { a, b -> a.compareTo(b) })
    : Heap<Pair<Int, Int>>(initCap) {
    private var key = IntArray(cap)
    private var value = IntArray(cap)

    override fun peek() = key[0] to value[0]

    override fun expand(increment: Int) {
        key += IntArray(increment)
        value += IntArray(increment)
    }

    override fun swap(p: Int, q: Int) {
        key.swap(p, q)
        value.swap(p, q)
    }

    override fun compare(indexA: Int, indexB: Int) = keyComparator(key[indexA], key[indexB])

    override fun doOffer(new: Pair<Int, Int>) {
        key[idx] = new.first
        value[idx] = new.second
    }
}

class LIHeap(initCap: Int = MIN_CAP, val keyComparator: (Long, Long) -> Int = { a, b -> a.compareTo(b) })
    : Heap<Pair<Long, Int>>(initCap) {
    private var key = LongArray(cap)
    private var value = IntArray(cap)

    override fun peek() = key[0] to value[0]

    override fun expand(increment: Int) {
        key += LongArray(increment)
        value += IntArray(increment)
    }

    override fun swap(p: Int, q: Int) {
        key.swap(p, q)
        value.swap(p, q)
    }

    override fun compare(indexA: Int, indexB: Int) = keyComparator(key[indexA], key[indexB])

    override fun doOffer(new: Pair<Long, Int>) {
        key[idx] = new.first
        value[idx] = new.second
    }
}