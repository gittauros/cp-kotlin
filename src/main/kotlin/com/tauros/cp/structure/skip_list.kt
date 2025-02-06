package com.tauros.cp.structure

import java.util.Random
import kotlin.math.abs


/**
 * @author tauros
 */
class SkipList<K>(val maxHeight: Int = DEFAULT_MAX_HEIGHT, val growPercentile: Int = DEFAULT_MAX_HEIGHT, private val comparator: Comparator<K>) {
    companion object {
        val DEFAULT_MAX_HEIGHT = 48
        val DEFAULT_GROW_PERCENTILE = 25
    }

    val height: Int
        get() = top
    val size: Int
        get() = len

    private var head: SkipListNode<K> = SkipListNode(null, maxHeight)
    private var tail: SkipListNode<K> = SkipListNode(null, maxHeight)
    private var len: Int = 0
    private var top: Int = 0
    private val random = Random()

    init {
        tail.backward = head
    }

    fun clear() {
        for (level in 0 until maxHeight) {
            head.levelForward[level] = null
            tail.levelForward[level] = null
        }
        tail.backward = head
        len = 0; top = 0
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
            add(Pair(next.value, height))
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

    private fun lowerNode(value: K): SkipListNode<K> {
        if (size == 0) return head
        var find = head
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) < 0) {
                find = find.levelForward[level]!!.node
            }
        }
        return find
    }

    private fun ceilingNode(value: K): SkipListNode<K> {
        if (size == 0) return tail
        var find = head
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) < 0) {
                find = find.levelForward[level]!!.node
            }
        }
        return if (find == tail) tail else find.levelForward[0]!!.node
    }

    private fun floorNode(value: K): SkipListNode<K> {
        if (size == 0) return head
        var find = head
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) <= 0) {
                find = find.levelForward[level]!!.node
            }
        }
        return find
    }

    private fun higherNode(value: K): SkipListNode<K> {
        if (size == 0) return tail
        var find = head
        for (level in top - 1 downTo 0) {
            while (find.levelForward[level]!!.node != tail &&
                comparator.compare(find.levelForward[level]!!.node.value, value) <= 0) {
                find = find.levelForward[level]!!.node
            }
        }
        return if (find == tail) tail else find.levelForward[0]!!.node
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
        return true
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
        val find = lowerNode(value)
        return find.levelForward[0]?.node != null && find.levelForward[0]?.node != tail &&
                comparator.compare(find.levelForward[0]!!.node.value, value) == 0
    }

    fun lower(value: K): K? {
        val find = lowerNode(value)
        return if (find == head) null else find.value
    }

    fun ceiling(value: K): K? {
        val find = ceilingNode(value)
        return if (find == tail) null else find.value
    }

    fun floor(value: K): K? {
        val find = floorNode(value)
        return if (find == head) null else find.value
    }

    fun higher(value: K): K? {
        val find = higherNode(value)
        return if (find == tail) null else find.value
    }
}

private class SkipListNode<V>(val value: V?, maxHeight: Int) {
    val levelForward: Array<SkipListNodeWithSpan<V>?> = Array(maxHeight) { null }
    var backward: SkipListNode<V>? = null
}

private data class SkipListNodeWithSpan<V>(var node: SkipListNode<V>, var span: Int = 0)

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