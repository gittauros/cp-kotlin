package com.tauros.cp.structure

import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.mergeSort
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mmo
import kotlin.math.abs
import kotlin.random.Random


/**
 * @author tauros
 */
// SortedList定义
abstract class SortedList<K>(protected val comparator: Comparator<K>) : MutableList<K> {
    abstract override fun iterator(): SortedListIterator<K>
    abstract override fun listIterator(): SortedListIterator<K>
    abstract override fun listIterator(index: Int): SortedListIterator<K>
    override fun addAll(index: Int, elements: Collection<K>) = TODO("Not yet implemented")
    override fun set(index: Int, element: K) = TODO("Not yet implemented")
    override fun add(index: Int, element: K) = TODO("Not yet implemented")
    override fun subList(fromIndex: Int, toIndex: Int) = TODO("Not yet implemented")
    interface SortedListIterator<K> : MutableListIterator<K> {
        override fun set(element: K) = TODO("Not yet implemented")
        override fun add(element: K) = TODO("Not yet implemented")
    }
    abstract fun ceilingIndex(element: K): Int
    abstract fun higherIndex(element: K): Int
    abstract fun floorIndex(element: K): Int
    abstract fun lowerIndex(element: K): Int
    abstract fun count(element: K): Int
    abstract operator fun plusAssign(elements: Collection<K>)
    abstract operator fun plus(elements: Collection<K>): SortedList<K>
}

// https://github.com/grantjenks/python-sortedcontainers/blob/master/src/sortedcontainers/sortedlist.py
// 与python sortedList基本相同的分块实现
abstract class BlockSortedList<K>(val blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: Comparator<K>) : SortedList<K>(comparator) {
    companion object {
        val DEFAULT_BLOCK_SIZE = 512
    }
    override val size: Int
        get() = len
    protected var len = 0
    abstract class ValueList<K>(protected val blockSize: Int, protected val comparator: Comparator<K>) : Iterable<K> {
        abstract val size: Int
        fun newCapacity(blockSize: Int) = minOf(1e8.toInt(), maxOf(5, size + minOf((size shr 1), blockSize)))
        abstract fun add(element: K)
        abstract fun add(index: Int, element: K)
        abstract fun addAll(other: ValueList<K>)
        abstract fun insort(element: K)
        abstract fun remove(from: Int, to: Int)
        abstract operator fun get(index: Int): K
        abstract operator fun set(index: Int, element: K)
        abstract fun appended(vararg others: ValueList<K>): ValueList<K>
        abstract fun copyOfRange(from: Int, to: Int): ValueList<K>
        abstract fun sort()
        abstract fun clear()
        override fun toString() = "[" + this.toList().joinToString(", ") + "]"
    }
    protected val lists = ArrayList<ValueList<K>>(2)
    protected val maxes: ValueList<K> = valueListOf(emptyList())
    protected var treeIndex: IntArray? = null
    protected var leafOffset = 0
    abstract protected fun valueListOf(elements: Collection<K>): ValueList<K>
    override fun add(element: K): Boolean {
        if (maxes.size > 0) {
            val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) > 0 }

            if (headIdx == maxes.size) {
                val pos = headIdx - 1
                lists[pos].add(element)
                maxes[pos] = element
                expand(pos)
            } else {
                lists[headIdx].insort(element)
                expand(headIdx)
            }
        } else {
            lists.add(valueListOf(listOf(element)))
            maxes.add(element)
        }
        len += 1
        return true
    }
    override fun addAll(elements: Collection<K>): Boolean {
        val new =
            if (maxes.size > 0) {
                if (elements.size shl 2 < len) {
                    val sorted = elements.sortedWith(comparator)
                    sorted.forEach(::add)
                    return true
                } else {
                    valueListOf(elements).appended(*lists.toTypedArray()).also { clear() }
                }
            } else valueListOf(elements)
        new.sort()

        var (from, to) = 0 to minOf(blockSize, new.size)
        do {
            val subList = new.copyOfRange(from, to)
            lists.add(subList)
            maxes.add(subList[subList.size - 1])
            to = minOf(new.size, to + blockSize)
            from = minOf(to, from + blockSize)
        } while (from < new.size)

        len = new.size
        treeIndex = null
        return true
    }
    override operator fun plusAssign(elements: Collection<K>) {
        this.addAll(elements)
    }
    protected fun expand(headIdx: Int) {
        if (lists[headIdx].size > blockSize shl 1) {
            val curList = lists[headIdx]
            val half = curList.copyOfRange(blockSize, curList.size)
            curList.remove(blockSize, curList.size)
            maxes[headIdx] = curList[curList.size - 1]

            lists.add(headIdx + 1, half)
            maxes.add(headIdx + 1, half[half.size - 1])
            treeIndex = null
        } else if (treeIndex != null) {
            val tree = treeIndex!!
            var iter = headIdx + leafOffset
            while (iter > 0) {
                tree[iter] += 1
                iter = iter - 1 shr 1
            }
            tree[iter] += 1
        }
    }
    private fun buildIndex() {
        val leafSizes = lists.map { it.size }.toIntArray()
        val highSize = leafSizes.size.takeHighestOneBit() shl 1
        val level0 = if (highSize - leafSizes.size == leafSizes.size) leafSizes
        else leafSizes + IntArray(highSize - leafSizes.size)
        leafOffset = 0
        treeIndex = buildList {
            add(level0)
            var iter = level0
            while (iter.size > 1) {
                val new = buildList {
                    for (i in iter.indices step 2) add(iter[i] + iter[i + 1])
                }.toIntArray()
                add(new); iter = new;
                leafOffset += iter.size
            }
        }.reversed().toTypedArray().reduce(IntArray::plus)
    }
    protected fun position(index: Int): Pair<Int, Int> {
        indexCheck(index)
        if (index < lists[0].size) return 0 to index
        if (treeIndex == null) buildIndex()

        val tree = treeIndex!!
        var (iter, lchd) = 0 to 1
        var idx = index
        while (lchd < tree.size) {
            val leftSize = tree[lchd]
            if (idx < leftSize) {
                iter = lchd
            } else {
                idx -= leftSize
                iter = lchd + 1
            }
            lchd = iter shl 1 or 1
        }
        return iter - leafOffset to idx
    }
    protected fun index(position: Pair<Int, Int>): Int {
        positionCheck(position)
        val (headIdx, subIdx) = position
        if (treeIndex == null) buildIndex()

        val tree = treeIndex!!
        var (iter, pre) = headIdx + leafOffset to 0
        while (iter > 0) {
            if (iter and 1 == 0) pre += tree[iter - 1]
            iter = iter - 1 shr 1
        }
        return pre + subIdx
    }
    override fun contains(element: K): Boolean {
        if (maxes.size == 0) return false
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) >= 0 }
        if (headIdx == maxes.size) return false
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) >= 0 }
        return comparator.compare(lists[headIdx][subIdx], element) == 0
    }
    override fun containsAll(elements: Collection<K>) = elements.all { it in this }
    override fun get(index: Int): K {
        val (headIdx, subIdx) = position(index)
        return lists[headIdx][subIdx]
    }
    override fun indexOf(element: K): Int {
        if (maxes.size == 0) return -1
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) >= 0 }
        if (headIdx == maxes.size) return -1
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) >= 0 }
        return if (comparator.compare(lists[headIdx][subIdx], element) == 0) index(headIdx to subIdx) else -1
    }
    override fun isEmpty() = size == 0
    override fun lastIndexOf(element: K): Int {
        if (maxes.size == 0) return -1
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) >= 0 }
        if (headIdx == maxes.size) return -1
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) > 0 } - 1
        return if (subIdx != -1 && comparator.compare(lists[headIdx][subIdx], element) == 0) index(headIdx to subIdx) else -1
    }
    override fun clear() {
        len = 0
        leafOffset = 0
        treeIndex = null
        maxes.clear()
        lists.clear()
    }
    override fun remove(element: K): Boolean {
        if (maxes.size == 0) return false
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) >= 0 }
        if (headIdx == maxes.size) return false
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) >= 0 }
        if (comparator.compare(lists[headIdx][subIdx], element) == 0) {
            delete(headIdx, subIdx)
            return true
        }
        return false
    }
    override fun removeAll(elements: Collection<K>) = elements.all { remove(it) }
    override fun removeAt(index: Int): K {
        indexCheck(index)
        val (headIdx, subIdx) = position(index)
        return delete(headIdx, subIdx)
    }
    protected fun delete(headIdx: Int, subIdx: Int): K {
        val element = lists[headIdx][subIdx]
        lists[headIdx].remove(subIdx, subIdx + 1)
        len -= 1
        if (lists[headIdx].size > blockSize shr 1) {
            maxes[headIdx] = lists[headIdx][lists[headIdx].size - 1]
            if (treeIndex != null) {
                val tree = treeIndex!!
                var iter = headIdx + leafOffset
                while (iter > 0) {
                    tree[iter] -= 1
                    iter = iter - 1 shr 1
                }
                tree[iter] -= 1
            }
        } else if (lists.size > 1) {
            val next = maxOf(1, headIdx)
            val cur = next - 1
            lists[cur].addAll(lists[next])
            maxes[cur] = lists[cur][lists[cur].size - 1]

            lists.removeAt(next)
            maxes.remove(next, next + 1)
            treeIndex = null

            expand(cur)
        } else if (lists[headIdx].size > 0) {
            maxes[headIdx] = lists[headIdx][lists[headIdx].size - 1]
            if (treeIndex != null) treeIndex!![0] -= 1
        } else {
            lists.removeAt(headIdx)
            maxes.remove(headIdx, headIdx + 1)
            treeIndex = null
        }
        return element
    }
    override fun retainAll(elements: Collection<K>): Boolean {
        var removed = false
        val iter = listIterator()
        while (iter.hasNext()) {
            val cur = iter.next()
            if (cur !in elements) {
                iter.remove()
                removed = true
            }
        }
        return removed
    }
    override fun ceilingIndex(element: K): Int {
        if (maxes.size == 0) return 0
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) >= 0 }
        if (headIdx == maxes.size) return size
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) >= 0 }
        return index(headIdx to subIdx)
    }
    override fun higherIndex(element: K): Int {
        if (maxes.size == 0) return 0
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) > 0 }
        if (headIdx == maxes.size) return size
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) > 0 }
        return index(headIdx to subIdx)
    }
    override fun floorIndex(element: K): Int {
        if (maxes.size == 0) return -1
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) > 0 }
        if (headIdx == maxes.size) return size - 1
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) > 0 } - 1
        return if (subIdx == -1 && headIdx - 1 < 0) -1
        else if (subIdx == -1) index(headIdx - 1 to lists[headIdx - 1].size - 1)
        else index(headIdx to subIdx)
    }
    override fun lowerIndex(element: K): Int {
        if (maxes.size == 0) return -1
        val headIdx = findFirst(maxes.size) { comparator.compare(maxes[it], element) >= 0 }
        if (headIdx == maxes.size) return size - 1
        val subIdx = findFirst(lists[headIdx].size) { comparator.compare(lists[headIdx][it], element) >= 0 } - 1
        return if (subIdx == -1 && headIdx - 1 < 0) -1
        else if (subIdx == -1) index(headIdx - 1 to lists[headIdx - 1].size - 1)
        else index(headIdx to subIdx)
    }
    override fun count(element: K) = higherIndex(element) - ceilingIndex(element)
    override fun iterator() = BlockSortedListIterator(this)
    override fun listIterator() = BlockSortedListIterator(this)
    override fun listIterator(index: Int) = BlockSortedListIterator(index, this)
    class BlockSortedListIterator<K>(private val sortedList: BlockSortedList<K>) : SortedListIterator<K> {
        constructor(index: Int, sortedList: BlockSortedList<K>) : this(sortedList) {
            idx = index
        }
        private var idx = -1
        override fun hasPrevious() = idx - 1 >= 0
        override fun nextIndex() = idx + 1
        override fun previous(): K {
            sortedList.indexCheck(idx - 1)
            return sortedList[--idx]
        }
        override fun previousIndex() = idx - 1
        override fun hasNext() = idx + 1 < sortedList.size
        override fun next(): K {
            sortedList.indexCheck(idx + 1)
            return sortedList[++idx]
        }
        override fun remove() {
            sortedList.indexCheck(idx)
            sortedList.removeAt(idx)
        }
    }
    private fun indexCheck(index: Int) {
        if (index !in 0 until len) throw IndexOutOfBoundsException("$index out of range 0 until $len")
    }
    private fun positionCheck(position: Pair<Int, Int>) {
        val (headIdx, subIdx) = position
        if (headIdx !in 0 until lists.size) throw IndexOutOfBoundsException("headIdx: $headIdx out of range 0 until ${lists.size}")
        if (subIdx !in 0 until lists[headIdx].size) throw IndexOutOfBoundsException("subIdx: $subIdx out of range 0 until ${lists[headIdx].size}")
    }
    fun toList(): List<K> {
        return buildList {
            var (headIdx, subIdx) = intArrayOf(0, 0)
            while (this.size < len) {
                add(lists[headIdx][subIdx++])
                if (subIdx == lists[headIdx].size) {
                    headIdx += 1
                    subIdx = 0
                }
            }
        }
    }
}

// IntBlockSortedList实现
class IntBlockSortedList(blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: (Int, Int) -> Int = Int::compareTo) : BlockSortedList<Int>(blockSize, comparator) {
    constructor(elements: IntArray, blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: (Int, Int) -> Int = Int::compareTo): this(blockSize, comparator) {
        for (num in elements) add(num)
    }
    constructor(elements: Collection<Int>, blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: (Int, Int) -> Int = Int::compareTo): this(blockSize, comparator) {
        addAll(elements)
    }
    private class IntValueList(blockSize: Int, comparator: Comparator<Int>) : ValueList<Int>(blockSize, comparator) {
        constructor(initArray: IntArray, blockSize: Int, comparator: Comparator<Int>) : this(blockSize, comparator) {
            array = initArray
            len = array.size
        }
        override val size: Int
            get() = len
        private var array = IntArray(5)
        private var len = 0
        override fun add(element: Int) {
            ensureCapacity(len + 1)
            array[len++] = element
        }
        override fun add(index: Int, element: Int) {
            ensureCapacity(len + 1)
            System.arraycopy(array, index, array, index + 1, len - index)
            array[index] = element
            len += 1
        }
        override fun addAll(other: ValueList<Int>) {
            ensureCapacity(len + other.size)
            for (num in other) array[len++] = num
        }
        override fun insort(element: Int) {
            ensureCapacity(len + 1)
            val idx = findFirst(len) { comparator.compare(array[it], element) >= 0 }
            System.arraycopy(array, idx, array, idx + 1, len - idx)
            array[idx] = element
            len += 1
        }
        override fun remove(from: Int, to: Int) {
            var tail = minOf(to, len)
            var head = minOf(tail, from)
            if (head >= tail) return
            while (tail < len) {
                array[head++] = array[tail++]
            }
            len = head
        }
        override fun get(index: Int): Int {
            indexCheck(index)
            return array[index]
        }
        override fun set(index: Int, element: Int) {
            indexCheck(index)
            array[index] = element
        }
        override fun iterator() = object : Iterator<Int> {
            var idx = -1
            override fun hasNext() = idx + 1 < len
            override fun next(): Int {
                indexCheck(idx + 1)
                idx += 1
                return array[idx]
            }
        }
        override fun appended(vararg others: ValueList<Int>): IntValueList {
            val new = IntArray(len + others.sumOf { it.size })
            array.copyInto(new, 0, 0, len)
            var newLen = len
            for (other in others) for (num in other) {
                new[newLen++] = num
            }
            return IntValueList(new, blockSize, comparator)
        }
        override fun copyOfRange(from: Int, to: Int): IntValueList {
            val tail = minOf(to, len)
            val head = minOf(tail, from)
            return IntValueList(array.copyOfRange(head, tail), blockSize, comparator)
        }
        override fun sort() { array.mergeSort(0, len) { a, b -> comparator.compare(a, b) } }
        override fun clear() { len = 0 }
        private fun indexCheck(index: Int) {
            if (index !in 0 until len) throw IndexOutOfBoundsException("$index out of range 0 until $len")
        }
        private fun ensureCapacity(newSize: Int) {
            if (newSize <= array.size) return
            val new = newCapacity(blockSize)
            array += IntArray(new - array.size)
        }
    }
    override fun plus(elements: Collection<Int>): SortedList<Int> {
        val all = lists.map { it.toList() }.reduceOrNull(List<Int>::plus).orEmpty()
        return IntBlockSortedList(all + elements.toList(), blockSize ) { a, b -> comparator.compare(a, b) }
    }
    override fun valueListOf(elements: Collection<Int>): ValueList<Int> {
        return IntValueList(elements.toIntArray(), blockSize, comparator)
    }
    fun toIntArray(): IntArray {
        val new = IntArray(len)
        var (headIdx, subIdx, idx) = intArrayOf(0, 0, 0)
        while (idx < len) {
            new[idx++] = lists[headIdx][subIdx++]
            if (subIdx == lists[headIdx].size) {
                headIdx += 1
                subIdx = 0
            }
        }
        return new
    }
}

// LongBlockSortedList实现
class LongBlockSortedList(blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: (Long, Long) -> Int = Long::compareTo) : BlockSortedList<Long>(blockSize, comparator) {
    constructor(elements: LongArray, blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: (Long, Long) -> Int = Long::compareTo): this(blockSize, comparator) {
        for (num in elements) add(num)
    }
    constructor(elements: Collection<Long>, blockSize: Int = DEFAULT_BLOCK_SIZE, comparator: (Long, Long) -> Int = Long::compareTo): this(blockSize, comparator) {
        addAll(elements)
    }
    private class LongValueList(blockSize: Int, comparator: Comparator<Long>) : ValueList<Long>(blockSize, comparator) {
        constructor(initArray: LongArray, blockSize: Int, comparator: Comparator<Long>) : this(blockSize, comparator) {
            array = initArray
            len = array.size
        }
        override val size: Int
            get() = len
        private var array = LongArray(5)
        private var len = 0
        override fun add(element: Long) {
            ensureCapacity(len + 1)
            array[len++] = element
        }
        override fun add(index: Int, element: Long) {
            ensureCapacity(len + 1)
            System.arraycopy(array, index, array, index + 1, len - index)
            array[index] = element
            len += 1
        }
        override fun addAll(other: ValueList<Long>) {
            ensureCapacity(len + other.size)
            for (num in other) array[len++] = num
        }
        override fun insort(element: Long) {
            ensureCapacity(len + 1)
            val idx = findFirst(len) { comparator.compare(array[it], element) >= 0 }
            System.arraycopy(array, idx, array, idx + 1, len - idx)
            array[idx] = element
            len += 1
        }
        override fun remove(from: Int, to: Int) {
            var tail = minOf(to, len)
            var head = minOf(tail, from)
            if (head >= tail) return
            while (tail < len) {
                array[head++] = array[tail++]
            }
            len = head
        }
        override fun get(index: Int): Long {
            indexCheck(index)
            return array[index]
        }
        override fun set(index: Int, element: Long) {
            indexCheck(index)
            array[index] = element
        }
        override fun iterator() = object : Iterator<Long> {
            var idx = -1
            override fun hasNext() = idx + 1 < len
            override fun next(): Long {
                indexCheck(idx + 1)
                idx += 1
                return array[idx]
            }
        }
        override fun appended(vararg others: ValueList<Long>): LongValueList {
            val new = LongArray(len + others.sumOf { it.size })
            array.copyInto(new, 0, 0, len)
            var newLen = len
            for (other in others) for (num in other) {
                new[newLen++] = num
            }
            return LongValueList(new, blockSize, comparator)
        }
        override fun copyOfRange(from: Int, to: Int): LongValueList {
            val tail = minOf(to, len)
            val head = minOf(tail, from)
            return LongValueList(array.copyOfRange(head, tail), blockSize, comparator)
        }
        override fun sort() { array.mergeSort(0, len) { a, b -> comparator.compare(a, b) } }
        override fun clear() { len = 0 }
        private fun indexCheck(index: Int) {
            if (index !in 0 until len) throw IndexOutOfBoundsException("$index out of range 0 until $len")
        }
        private fun ensureCapacity(newSize: Int) {
            if (newSize <= array.size) return
            val new = newCapacity(blockSize)
            array += LongArray(new - array.size)
        }
    }
    override fun plus(elements: Collection<Long>): SortedList<Long> {
        val all = lists.map { it.toList() }.reduceOrNull(List<Long>::plus).orEmpty()
        return LongBlockSortedList(all + elements.toList(), blockSize ) { a, b -> comparator.compare(a, b) }
    }
    override fun valueListOf(elements: Collection<Long>): ValueList<Long> {
        return LongValueList(elements.toLongArray(), blockSize, comparator)
    }
    fun toLongArray(): LongArray {
        val new = LongArray(len)
        var (headIdx, subIdx, idx) = intArrayOf(0, 0, 0)
        while (idx < len) {
            new[idx++] = lists[headIdx][subIdx++]
            if (subIdx == lists[headIdx].size) {
                headIdx += 1
                subIdx = 0
            }
        }
        return new
    }
}


/**
 * 0 add
 * 1 n x x x addAll
 * 2 x remove
 * 3 i removeAt
 * 4 i get
 * 5 x count
 * 6 x ceilingIndex
 * 7 x higherIndex
 * 8 x floorIndex
 * 9 x lowerIndex
**/
private fun randomTestCases(opCount: Int, valCap: Int) = buildList {
    val cnt = mmo<int, int>().default { 0 }
    val sorted = mutableListOf<Int>()
    var size = 0
    fun randomVal() = randomInt(valCap) + 1
    repeat(opCount) {
        while (true) {
            val op = randomInt(10)
            if (op == 0) {
                val num = randomVal()
                add(iao(op, num))
                cnt[num] += 1
                sorted.add(num)
                size += 1
            } else if (op == 1) {
                val n = randomInt(3) + 1
                val new = iar(n) { randomVal() }
                add(iao(op, n) + new)
                for (num in new) {
                    cnt[num] += 1
                    sorted.add(num)
                }
                size += n
            } else if (op == 2) {
                if (size == 0) continue
                while (true) {
                    val num = randomVal()
                    if (cnt[num] == 0) continue
                    add(iao(op, num))
                    cnt[num] -= 1
                    sorted.remove(num)
                    break
                }
                size -= 1
            } else if (op == 3) {
                if (size == 0) continue
                val i = randomInt(size)
                add(iao(op, i))
                sorted.sort()
                cnt[sorted.removeAt(i)] -= 1
                size -= 1
            } else if (op == 4) {
                if (size == 0) continue
                val i = randomInt(size)
                add(iao(op, i))
            } else {
                add(iao(op, randomVal()))
            }
            break
        }
    }
}
private fun testCasesFromString(cases: String) = buildList {
    val lines = cases.split("\n")
    for (line in lines) {
        add(line.split(" ").map { it.toInt() }.toIntArray())
    }
}
private fun executeCasesByBit(cases: List<IntArray>, valCap: Int) = buildString {
    fun IntArray.update(pos: Int, add: Int) = this.bitUpdateWithIndex(pos) { this[it] += add }
    fun IntArray.query(pos: Int) = this.bitQuery(pos, 0, int::plus)
    fun IntArray.add(num: Int) { this.update(num, 1) }
    fun IntArray.addAll(nums: Collection<Int>) { for (num in nums) add(num) }
    fun IntArray.remove(num: Int) { this.update(num, -1) }
    fun IntArray.at(i: Int) = findFirst(1, valCap) { this.query(it) > i }
    fun IntArray.removeAt(i: Int) = this.update(at(i), -1)
    fun IntArray.count(num: Int) = this.query(num) - this.query(num - 1)
    fun IntArray.ceilingIndex(num: Int) = this.query(num - 1)
    fun IntArray.higherIndex(num: Int) = this.query(num)
    fun IntArray.floorIndex(num: Int) = this.query(num) - 1
    fun IntArray.lowerIndex(num: Int) = this.query(num - 1) - 1

    val bit = iar(valCap + 1)
    for (case in cases) {
        val op = case[0]
        if (op == 0) bit.add(case[1])
        else if (op == 1) bit.addAll(case.copyOfRange(2, case[1] + 2).toList())
        else if (op == 2) bit.remove(case[1])
        else if (op == 3) bit.removeAt(case[1])
        else if (op == 4) {
            append(bit.at(case[1]))
            append('\n')
        } else if (op == 5) {
            append(bit.count(case[1]))
            append('\n')
        } else if (op == 6) {
            append(bit.ceilingIndex(case[1]))
            append('\n')
        } else if (op == 7) {
            append(bit.higherIndex(case[1]))
            append('\n')
        } else if (op == 8) {
            append(bit.floorIndex(case[1]))
            append('\n')
        } else if (op == 9) {
            append(bit.lowerIndex(case[1]))
            append('\n')
        }
    }
}

private fun executeCasesByBlockSortedList(cases: List<IntArray>) = buildString {
    val sortedList = IntBlockSortedList()
    for (case in cases) {
        val op = case[0]
        if (op == 0) sortedList.add(case[1])
        else if (op == 1) sortedList.addAll(case.copyOfRange(2, case[1] + 2).toList())
        else if (op == 2) sortedList.remove(case[1])
        else if (op == 3) sortedList.removeAt(case[1])
        else if (op == 4) {
            append(sortedList[case[1]])
            append('\n')
        } else if (op == 5) {
            append(sortedList.count(case[1]))
            append('\n')
        } else if (op == 6) {
            append(sortedList.ceilingIndex(case[1]))
            append('\n')
        } else if (op == 7) {
            append(sortedList.higherIndex(case[1]))
            append('\n')
        } else if (op == 8) {
            append(sortedList.floorIndex(case[1]))
            append('\n')
        } else if (op == 9) {
            append(sortedList.lowerIndex(case[1]))
            append('\n')
        }
    }
}
private fun testIntBlockSortedList(opCount: Int, valCap: Int): String {
    val cases = randomTestCases(opCount, valCap)

    var bitDuration = -System.currentTimeMillis()
    val expectAns = executeCasesByBit(cases, valCap)
    bitDuration += System.currentTimeMillis()

    var sortedListDuration = -System.currentTimeMillis()
    val actualAns = executeCasesByBlockSortedList(cases)
    sortedListDuration += System.currentTimeMillis()

    if (expectAns == actualAns) return "bitDuration: $bitDuration ms, sortedListDuration: $sortedListDuration ms"
    val casesStr = cases.joinToString("") { it.joinToString(" ") + "\n" }
    return buildString {
        append("---cases:\n")
        append(casesStr)
        append("===expect\n")
        append(expectAns)
        append("===actual\n")
        append(actualAns)
    }.trim('\n')
}

private fun randomInt(cap: Int) = abs(Random.nextInt()) % cap
fun main() {
    repeat(100) {
        val res = testIntBlockSortedList(500000, randomInt(1000000) + 1)
        println(res)
    }
}