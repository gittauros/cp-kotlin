package com.tauros.cp.structure

import java.util.Random
import kotlin.math.abs


/**
 * @author tauros
 */
open class SkipList<K>(val maxHeight: Int = DEFAULT_MAX_HEIGHT, val growPercentile: Int = DEFAULT_GROW_PERCENTILE, comparator: Comparator<K>)
    : SortedList<K>(comparator) {
    companion object {
        val DEFAULT_MAX_HEIGHT = 48
        val DEFAULT_GROW_PERCENTILE = 25
    }

    val height: Int
        get() = top
    override val size: Int
        get() = len

    val head: SkipListNode<K> = SkipListNode(null, maxHeight)
    val tail: SkipListNode<K> = SkipListNode(null, maxHeight)
    private var len: Int = 0
    private var top: Int = 0
    private val random = Random()

    init {
        tail.backward = head
    }
    override fun clear() {
        for (level in 0 until maxHeight) {
            head.levelForward[level] = null
            tail.levelForward[level] = null
        }
        tail.backward = head
        len = 0; top = 0
    }
    fun indexCheck(index: Int) {
        if (index !in 0 until len) throw IndexOutOfBoundsException("$index out of range 0 until $len")
    }
    private fun randomHeight(): Int {
        for (level in 1 until maxHeight) {
            val percentile = abs(random.nextInt()) % 100
            if (percentile >= growPercentile) return level
        }
        return maxHeight
    }
    fun view() = buildList {
        var iter = head
        while (true) {
            val next = iter.levelForward[0]!!.node
            if (next == tail) break
            var height = 0
            while (height < next.levelForward.size) height += 1
            add(next.value to height)
            iter = next
        }
    }
    private fun find(update: Array<SkipListNode<K>?>?, rank: IntArray?, value: K) {
        var find = head
        for (level in top - 1 downTo 0) {
            if (rank != null) {
                rank[level] = if (level == top - 1) 0 else rank[level + 1]
            }
            var forward = find.levelForward[level]!!
            while (forward.node != tail && comparator.compare(forward.node.value, value) < 0) {
                find = forward.node
                if (rank != null) {
                    rank[level] += forward.span
                }
                forward = find.levelForward[level]!!
            }
            if (update != null) {
                update[level] = find
            }
        }
    }
    private fun findAt(update: Array<SkipListNode<K>?>, rank: Int) {
        var find = head
        var preRank = 0
        for (level in top - 1 downTo 0) {
            var forward = find.levelForward[level]!!
            while (forward.node != tail && preRank + forward.span < rank) {
                preRank += forward.span
                find = forward.node
                forward = find.levelForward[level]!!
            }
            update[level] = find
        }
    }
    private fun lowerNode(value: K): Pair<SkipListNode<K>, Int> {
        if (size == 0) return head to 0
        var (find, rank) = head to 0
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) < 0) {
                rank += find.levelForward[level]!!.span
                find = find.levelForward[level]!!.node
            }
        }
        return find to rank
    }
    private fun ceilingNode(value: K): Pair<SkipListNode<K>, Int> {
        if (size == 0) return tail to 0
        var (find, rank) = head to 0
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) < 0) {
                rank += find.levelForward[level]!!.span
                find = find.levelForward[level]!!.node
            }
        }
        return if (find == tail) tail to rank else find.levelForward[0]!!.node to rank + 1
    }
    private fun floorNode(value: K): Pair<SkipListNode<K>, Int> {
        if (size == 0) return head to 0
        var (find, rank) = head to 0
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) <= 0) {
                rank += find.levelForward[level]!!.span
                find = find.levelForward[level]!!.node
            }
        }
        return find to rank
    }
    private fun higherNode(value: K): Pair<SkipListNode<K>, Int> {
        if (size == 0) return tail to 0
        var (find, rank) = head to 0
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) <= 0) {
                find = find.levelForward[level]!!.node
            }
        }
        return if (find == tail) tail to rank else find.levelForward[0]!!.node to rank + 1
    }
    fun insert(value: K): Int {
        val update = Array<SkipListNode<K>?>(maxHeight) { null }
        val rank = IntArray(maxHeight)
        find(update, rank, value)

        val height = randomHeight()
        if (height > top) {
            for (level in top until height) {
                head.levelForward[level] = SkipListNodeWithSpan(tail, len + 1)
                update[level] = head
            }
            top = height
        }

        val cur = SkipListNode(value, height)
        for (level in 0 until top) {
            if (level < height) {
                val rankDiff = rank[0] - rank[level]
                cur.levelForward[level] = SkipListNodeWithSpan(
                    update[level]!!.levelForward[level]!!.node,
                    update[level]!!.levelForward[level]!!.span - rankDiff
                )

                update[level]!!.levelForward[level]!!.node = cur
                update[level]!!.levelForward[level]!!.span = rankDiff + 1
            } else {
                update[level]!!.levelForward[level]!!.span += 1
            }
        }
        cur.backward = update[0]
        cur.levelForward[0]!!.node.backward = cur
        len += 1
        return rank[0]
    }
    fun delete(value: K): Boolean {
        if (len <= 0) return false
        val update = Array<SkipListNode<K>?>(top) { null }
        find(update, null, value)

        val find = update[0]!!.levelForward[0]!!.node
        if (find == tail || comparator.compare(value, find.value) != 0) return false
        doDelete(find, update)
        return true
    }
    fun deleteAt(index: Int): K {
        indexCheck(index)
        val update = Array<SkipListNode<K>?>(top) { null }
        findAt(update, index + 1)

        val find = update[0]!!.levelForward[0]!!.node
        return doDelete(find, update)
    }
    private fun doDelete(find: SkipListNode<K>, update: Array<SkipListNode<K>?>): K {
        val deleted = update[0]!!.levelForward[0]!!.node.value!!
        for (level in 0 until top) {
            if (level < find.levelForward.size) {
                update[level]!!.levelForward[level]!!.node = find.levelForward[level]!!.node
                update[level]!!.levelForward[level]!!.span += find.levelForward[level]!!.span - 1
            } else {
                update[level]!!.levelForward[level]!!.span -= 1
            }
        }

        for (level in top - 1 downTo 0) {
            if (head.levelForward[level]?.node == tail) {
                head.levelForward[level] = null
                top -= 1
            }
        }
        len -= 1
        if (len > 0) {
            update[0]!!.levelForward[0]!!.node.backward = update[0]!!
        } else {
            tail.backward = head
        }
        return deleted
    }
    fun kth(rank: Int): K? {
        if (rank < 1 || rank > size) return null
        var curRank = 0
        var find = head
        for (level in top - 1 downTo 0) {
            while (curRank + find.levelForward[level]!!.span < rank) {
                curRank += find.levelForward[level]!!.span
                find = find.levelForward[level]!!.node
            }
        }
        return find.levelForward[0]!!.node.value
    }
    fun rank(value: K): Int {
        var curRank = 0
        var find = head
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) < 0) {
                curRank += find.levelForward[level]!!.span
                find = find.levelForward[level]!!.node
            }
        }
        return curRank + 1
    }
    fun exist(value: K): Boolean {
        val (find, _) = lowerNode(value)
        return find.levelForward[0]?.node != null && find.levelForward[0]?.node != tail &&
                comparator.compare(find.levelForward[0]!!.node.value, value) == 0
    }
    fun lower(value: K): K? {
        val (find, _) = lowerNode(value)
        return if (find == head) null else find.value
    }
    fun ceiling(value: K): K? {
        val (find, _) = ceilingNode(value)
        return if (find == tail) null else find.value
    }
    fun floor(value: K): K? {
        val (find, _) = floorNode(value)
        return if (find == head) null else find.value
    }
    fun higher(value: K): K? {
        val (find, _) = higherNode(value)
        return if (find == tail) null else find.value
    }
    override fun iterator() = SkipListIterator(this)
    override fun listIterator() = SkipListIterator(this)
    override fun listIterator(index: Int) = SkipListIterator(index, this)
    override fun addAll(elements: Collection<K>): Boolean {
        elements.forEach(::add)
        return true
    }
    override fun add(element: K): Boolean {
        insert(element)
        return true
    }
    override fun plus(elements: Collection<K>): SkipList<K> {
        val newList = SkipList(maxHeight, growPercentile, comparator)
        newList.addAll(this)
        newList.addAll(elements)
        return newList
    }
    override fun plusAssign(elements: Collection<K>) {
        this.addAll(elements)
    }
    override fun count(element: K) = higherIndex(element) - ceilingIndex(element)
    override fun lowerIndex(element: K): Int {
        val (_, rank) = lowerNode(element)
        return rank - 1
    }
    override fun floorIndex(element: K): Int {
        val (_, rank) = floorNode(element)
        return rank - 1
    }
    override fun higherIndex(element: K): Int {
        val (_, rank) = higherNode(element)
        return rank - 1
    }
    override fun ceilingIndex(element: K): Int {
        val (_, rank) = ceilingNode(element)
        return rank - 1
    }
    override fun get(index: Int): K {
        indexCheck(index)
        return kth(index +1)!!
    }
    override fun isEmpty() = size == 0
    override fun removeAt(index: Int) = deleteAt(index)
    override fun remove(element: K) = delete(element)
    override fun lastIndexOf(element: K): Int {
        val (find, rank) = floorNode(element)
        return if (find != head && comparator.compare(find.value, element) == 0) rank - 1 else -1
    }
    override fun indexOf(element: K): Int {
        val (find, rank) = ceilingNode(element)
        return if (find != tail && comparator.compare(find.value, element) == 0) rank - 1 else -1
    }
    override fun containsAll(elements: Collection<K>) = elements.all { it in this }
    override fun contains(element: K) = exist(element)
}

class SkipListNode<V>(val value: V?, maxHeight: Int) {
    val levelForward: Array<SkipListNodeWithSpan<V>?> = Array(maxHeight) { null }
    var backward: SkipListNode<V>? = null
}

data class SkipListNodeWithSpan<V>(var node: SkipListNode<V>, var span: Int = 0)

class SkipListIterator<K>(
    private val skipList: SkipList<K>
) : SortedListIterator<K> {
    constructor(index: Int, skipList: SkipList<K>) : this(skipList) {
        idx = index
    }
    private var iter: SkipListNode<K> = skipList.head
    private var idx: Int = -1
    override fun hasNext() = iter.levelForward[0] != null && iter.levelForward[0]!!.node != skipList.tail
    override fun hasPrevious() = iter.backward != null && iter.backward != skipList.head
    override fun next(): K {
        skipList.indexCheck(idx + 1)
        iter = iter.levelForward[0]!!.node
        return iter.value!!
    }
    override fun nextIndex() = idx + 1
    override fun previous(): K {
        skipList.indexCheck(idx - 1)
        iter = iter.backward!!
        return iter.value!!
    }
    override fun previousIndex() = idx - 1
    override fun remove() {
        skipList.deleteAt(idx)
    }
}

class IntSkipList(maxHeight: Int = DEFAULT_MAX_HEIGHT, growPercentile: Int = DEFAULT_GROW_PERCENTILE, comparator: (Int, Int) -> Int = { a, b -> a.compareTo(b) })
    : SkipList<Int>(maxHeight, growPercentile, comparator)
class LongSkipList(maxHeight: Int = DEFAULT_MAX_HEIGHT, growPercentile: Int = DEFAULT_GROW_PERCENTILE, comparator: (Long, Long) -> Int = { a, b -> a.compareTo(b) })
    : SkipList<Long>(maxHeight, growPercentile, comparator)

fun main(args: Array<String>) {
    val list = SkipList<Int> { a, b -> a.compareTo(b) }
    repeat(5) {
        list.clear()
        val added = buildList {
            repeat(1000) {
                val num = com.tauros.cp.randomInt() % 100000
                list.insert(num)
                add(num)
            }
        }
        for (num in added) {
            list.delete(num)
        }
        repeat(1000) {
            val num = com.tauros.cp.randomInt() % 100000
            list.delete(num)
        }
    }
    println("out")
}