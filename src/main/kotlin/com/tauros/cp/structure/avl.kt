package com.tauros.cp.structure

import com.tauros.cp.ar
import com.tauros.cp.iao
import java.util.TreeMap
import java.util.TreeSet
import kotlin.math.abs
import kotlin.random.Random
import kotlin.system.measureTimeMillis


/**
 * @author tauros
 */
data class AVLNode<K, V>(var key: K, var value: V) {
    internal var left: AVLNode<K, V>? = null
    internal var right: AVLNode<K, V>? = null
    internal var height = 1

    internal fun min(): AVLNode<K, V> {
        var iter: AVLNode<K, V> = this
        while (iter.left != null) iter = iter.left!!
        return iter
    }

    internal fun max(): AVLNode<K, V> {
        var iter: AVLNode<K, V> = this
        while (iter.right != null) iter = iter.right!!
        return iter
    }

    inline internal fun lRotate(): AVLNode<K, V> {
        val new = this.right!!
        this.right = new.left
        this.update()
        new.left = this
        new.update()
        return new
    }

    inline internal fun rRotate(): AVLNode<K, V> {
        val new = this.left!!
        this.left = new.right
        this.update()
        new.right = this
        new.update()
        return new
    }

    internal fun update(): AVLNode<K, V> {
        this.height = maxOf((this.left?.height ?: 0), this.right?.height ?: 0) + 1
        return this
    }

    private fun balanceFactor() = (this.left?.height ?: 0) - (this.right?.height ?: 0)

    internal fun check(): AVLNode<K, V> {
        val bf = balanceFactor()
        if (bf < -1) {
            val innerBf: Int = right?.balanceFactor() ?: 0
            if (innerBf > 0) {
                this.right = right?.rRotate()
            }
            return lRotate()
        } else if (bf > 1) {
            val innerBf: Int = left?.balanceFactor() ?: 0
            if (innerBf < 0) {
                this.left = left?.lRotate()
            }
            return rRotate()
        }
        return this
    }
}


class AVLMap<K, V>(private val comparator: Comparator<K>) : MutableMap<K, V>, Iterable<AVLNode<K, V>> {
    private val stack = ArrayDeque<AVLNode<K, V>>()
    private var root: AVLNode<K, V>? = null
    private var transient: V? = null
    private var cnt = 0

    override val size: Int
        get() = cnt

    fun toList(): List<AVLNode<K, V>> {
        return buildList {
            fun AVLNode<K, V>?.dfs() {
                if (this == null) return
                this.left.dfs()
                add(this)
                this.right.dfs()
            }
            root.dfs()
        }
    }

    override fun iterator() = toList().iterator()


    private fun AVLNode<K, V>?.insert(k: K, v: V): AVLNode<K, V> {
        if (this == null) {
            transient = null
            return AVLNode(k, v)
        }
        val cmp = comparator.compare(k, this.key)
        if (cmp == 0) {
            transient = this.value
            this.value = v
        } else if (cmp < 0) {
            this.left = this.left.insert(k, v)
        } else {
            this.right = this.right.insert(k, v)
        }
        this.update()
        return this.check()
    }

    private fun AVLNode<K, V>.deleteMin(target: AVLNode<K, V>): AVLNode<K, V>? {
        return if (this === target) this.right
        else {
            this.left = this.left?.deleteMin(target)
            this.update()
            this.check()
        }
    }

    private fun AVLNode<K, V>?.delete(k: K): AVLNode<K, V>? {
        if (this == null) return null
        val cmp = comparator.compare(k, this.key)
        if (cmp < 0) {
            this.left = this.left.delete(k)
        } else if (cmp > 0) {
            this.right = this.right.delete(k)
        } else {
            transient = this.value
            if (this.left == null && this.right == null) return null
            if (this.left == null) return this.right
            if (this.right == null) return this.left
            var iter = this.right!!
            while (iter.left != null) iter = iter.left!!
            this.key = iter.key
            this.value = iter.value
            this.right = this.right!!.deleteMin(iter)
        }
        this.update()
        return this.check()
    }

    internal fun search(k: K): AVLNode<K, V>? {
        if (root == null || root?.key == k) return root
        var iter = root
        while (iter != null) {
            val cmp = comparator.compare(k, iter.key)
            iter = if (cmp < 0) iter.left
            else if (cmp > 0) iter.right
            else return iter
        }
        return null
    }

    private fun AVLNode<K, V>?.containsValue(v: V): Boolean {
        if (this == null) return false
        if (this.value == v) return true
        return this.left.containsValue(v) || this.right.containsValue(v)
    }

    override fun containsKey(key: K) = search(key) != null

    override fun containsValue(value: V) = root.containsValue(value)

    override fun get(key: K) = search(key)?.value

    override fun isEmpty() = size == 0

    override fun clear() {
        root = null
        cnt = 0
    }

    override fun put(key: K, value: V): V? {
        root = root.insert(key, value)
        val old = transient
        transient = null
        if (old == null) cnt++
        return old
    }

    override fun putAll(from: Map<out K, V>) {
        for ((k, v) in from) put(k, v)
    }

    override fun remove(key: K): V? {
        root = root.delete(key)
        val old = transient
        transient = null
        if (old != null) cnt--
        return old
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get(): MutableSet<MutableMap.MutableEntry<K, V>> {
            if (entrySet == null) {
                entrySet = object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
                    override fun add(element: MutableMap.MutableEntry<K, V>) = throw UnsupportedOperationException("add not supported")
                    override val size: Int
                        get() = this@AVLMap.size

                    override fun iterator() = object : MutableIterator<MutableMap.MutableEntry<K, V>> {
                        private val iter = this@AVLMap.iterator()
                        override fun hasNext() = iter.hasNext()
                        override fun remove() = throw UnsupportedOperationException("remove not supported")
                        override fun next() = object : MutableMap.MutableEntry<K, V> {
                            private val node = iter.next()
                            override val key: K
                                get() = node.key
                            override val value: V
                                get() = node.value

                            override fun setValue(newValue: V): V {
                                val old = node.value
                                node.value = newValue
                                return old
                            }
                        }
                    }

                    override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
                        val node = search(element.key)
                        return node != null && node.value == element.value
                    }
                }
            }
            return entrySet!!
        }
    override val keys: MutableSet<K>
        get(): MutableSet<K> {
            if (keySet == null) {
                keySet = object : AbstractMutableSet<K>() {
                    override fun add(element: K) = throw UnsupportedOperationException("add not supported")
                    override fun contains(element: K) = containsKey(element)
                    override val size: Int
                        get() = this@AVLMap.size

                    override fun iterator() = object : MutableIterator<K> {
                        private val iter = this@AVLMap.iterator()
                        override fun hasNext() = iter.hasNext()
                        override fun next() = iter.next().key
                        override fun remove() = throw UnsupportedOperationException("remove not supported")
                    }
                }
            }
            return keySet!!
        }
    override val values: MutableCollection<V>
        get(): MutableCollection<V> {
            if (valueCollection == null) {
                valueCollection = object : AbstractMutableCollection<V>() {
                    override fun add(element: V) = throw UnsupportedOperationException("add not supported")
                    override fun contains(element: V) = containsValue(element)
                    override val size: Int
                        get() = this@AVLMap.size

                    override fun iterator() = object : MutableIterator<V> {
                        private val iter = this@AVLMap.iterator()
                        override fun hasNext() = iter.hasNext()
                        override fun next() = iter.next().value
                        override fun remove() = throw UnsupportedOperationException("remove not supported")
                    }
                }
            }
            return valueCollection!!
        }

    private var entrySet: MutableSet<MutableMap.MutableEntry<K, V>>? = null
    private var keySet: MutableSet<K>? = null
    private var valueCollection: MutableCollection<V>? = null

    fun ceiling(key: K): AVLNode<K, V>? {
        var iter = root
        stack.clear()
        while (iter != null) {
            val cmp = comparator.compare(key, iter.key)
            if (cmp < 0) {
                iter = if (iter.left != null) {
                    stack.addLast(iter)
                    iter.left
                } else return iter
            } else if (cmp > 0) {
                if (iter.right != null) {
                    stack.addLast(iter)
                    iter = iter.right
                } else {
                    if (stack.isEmpty()) return null
                    var parent = stack.removeLast()
                    var chd = iter
                    while (chd === parent.right) {
                        chd = parent
                        if (stack.isEmpty()) return null
                        parent = stack.removeLast()
                    }
                    return parent
                }
            } else return iter
        }
        return null
    }

    fun floor(key: K): AVLNode<K, V>? {
        var iter = root
        stack.clear()
        while (iter != null) {
            val cmp = comparator.compare(key, iter.key)
            if (cmp > 0) {
                iter = if (iter.right != null) {
                    stack.addLast(iter)
                    iter.right
                } else return iter
            } else if (cmp < 0) {
                if (iter.left != null) {
                    stack.addLast(iter)
                    iter = iter.left
                } else {
                    if (stack.isEmpty()) return null
                    var parent = stack.removeLast()
                    var chd = iter
                    while (chd === parent.left) {
                        chd = parent
                        if (stack.isEmpty()) return null
                        parent = stack.removeLast()
                    }
                    return parent
                }
            } else return iter
        }
        return null
    }

    fun higher(key: K): AVLNode<K, V>? {
        var iter = root
        stack.clear()
        while (iter != null) {
            val cmp = comparator.compare(key, iter.key)
            if (cmp < 0) {
                iter = if (iter.left != null) {
                    stack.addLast(iter)
                    iter.left
                } else return iter
            } else {
                if (iter.right != null) {
                    stack.addLast(iter)
                    iter = iter.right
                } else {
                    if (stack.isEmpty()) return null
                    var parent = stack.removeLast()
                    var chd = iter
                    while (chd === parent.right) {
                        chd = parent
                        if (stack.isEmpty()) return null
                        parent = stack.removeLast()
                    }
                    return parent
                }
            }
        }
        return null
    }

    fun lower(key: K): AVLNode<K, V>? {
        var iter = root
        stack.clear()
        while (iter != null) {
            val cmp = comparator.compare(key, iter.key)
            if (cmp > 0) {
                iter = if (iter.right != null) {
                    stack.addLast(iter)
                    iter.right
                } else return iter
            } else {
                if (iter.left != null) {
                    stack.addLast(iter)
                    iter = iter.left
                } else {
                    if (stack.isEmpty()) return null
                    var parent = stack.removeLast()
                    var chd = iter
                    while (chd === parent.left) {
                        chd = parent
                        if (stack.isEmpty()) return null
                        parent = stack.removeLast()
                    }
                    return parent
                }
            }
        }
        return null
    }

    fun min() = root?.min()
    fun max() = root?.max()
    fun first() = root?.min()
    fun last() = root?.max()
}

class AVLSet<K>(cmp: Comparator<K>) : MutableSet<K> {
    private val avl = AVLMap<K, Unit>(cmp)
    private val present = Unit
    override fun add(element: K) = avl.put(element, present) == null

    override fun addAll(elements: Collection<K>): Boolean {
        var res = false
        for (v in elements) {
            val added = add(v)
            res = res or added
        }
        return res
    }

    override fun clear() {
        avl.clear()
    }

    override fun iterator() = object : MutableIterator<K> {
        private val iter = avl.entries.iterator()
        override fun hasNext() = iter.hasNext()
        override fun next() = iter.next().key
        override fun remove() = iter.remove()
    }

    override fun remove(element: K) = avl.remove(element) === present

    override fun removeAll(elements: Collection<K>): Boolean {
        var res = false
        for (v in elements) {
            val removed = remove(v)
            res = res or removed
        }
        return res
    }

    override fun retainAll(elements: Collection<K>) = throw UnsupportedOperationException("retainAll not supported")

    override val size: Int
        get() = avl.size

    override fun contains(element: K) = element in avl.keys

    override fun containsAll(elements: Collection<K>): Boolean {
        for (v in elements) if (v !in this) return false
        return true
    }

    override fun isEmpty() = size == 0

    fun ceiling(key: K) = avl.ceiling(key)?.key
    fun floor(key: K) = avl.floor(key)?.key
    fun higher(key: K) = avl.higher(key)?.key
    fun lower(key: K) = avl.lower(key)?.key
    fun min() = avl.min()?.key
    fun max() = avl.max()?.key
    fun first() = avl.first()?.key
    fun last() = avl.last()?.key
}

private fun tstSet(rep: Int = 30) {
    var diff = 0L
    repeat(rep) {
        val n = 1000000 // abs(Random.nextInt()) % 500000 + 1
        val ops = ar(n) {
            iao(if (it < n / 2) 0 else
                abs(Random.nextInt()) % 7, Random.nextInt()
            )
        }
        val set1 = AVLSet<Int>(Int::compareTo)
        val time1 = measureTimeMillis {
            for ((op, x) in ops) {
                if (op == 0) set1.add(x)
                else if (op == 1) set1.remove(x)
                else if (op == 2) set1.contains(x)
                else if (op == 3) set1.ceiling(x)
                else if (op == 4) set1.floor(x)
                else if (op == 5) set1.higher(x)
                else if (op == 6) set1.lower(x)
            }
        }
        val set2 = TreeSet<Int>()
        val time2 = measureTimeMillis {
            for ((op, x) in ops) {
                if (op == 0) set2.add(x)
                else if (op == 1) set2.remove(x)
                else if (op == 2) set2.contains(x)
                else if (op == 3) set2.ceiling(x)
                else if (op == 4) set2.floor(x)
                else if (op == 5) set2.higher(x)
                else if (op == 6) set2.lower(x)
            }
        }
        println("insert:${ops.count { it[0] == 0 }} query:${ops.count { it[0] == 1 }} ceiling:${ops.count { it[0] == 2 }}")
        println("avlSet: $time1 treeSet: $time2 diff:${time1 - time2}")
        println("===")
        diff += time1 - time2
        set1.clear()
        set2.clear()
        var success = true
        for ((op, x) in ops) {
            if (op == 0) {
                set1.add(x)
                set2.add(x)
                if (set1.size != set2.size) {
                    success = false
                    break
                }
            }
            else if (op == 1) {
                set1.remove(x)
                set2.remove(x)
                if (set1.size != set2.size) {
                    success = false
                    break
                }
            }
            else if (op == 2) {
                val a = set1.contains(x)
                val b = set2.contains(x)
                if (a != b) {
                    success = false
                    break
                }
            }
            else if (op == 3) {
                val a = set1.ceiling(x)
                val b = set2.ceiling(x)
                if (a != b) {
                    success = false
                    break
                }
            }
            else if (op == 4) {
                val a = set1.floor(x)
                val b = set2.floor(x)
                if (a != b) {
                    success = false
                    break
                }
            }
            else if (op == 5) {
                val a = set1.higher(x)
                val b = set2.higher(x)
                if (a != b) {
                    success = false
                    break
                }
            }
            else if (op == 6) {
                val a = set1.lower(x)
                val b = set2.lower(x)
                if (a != b) {
                    success = false
                    break
                }
            }
        }
        if (!success) {
            println("xxx")
            return
        }
    }
    println(diff / rep.toDouble())
}

private fun tstMap(rep: Int = 30) {
    var diff = 0L
    repeat(rep) {
        val n = 1000000 // abs(Random.nextInt()) % 500000 + 1
        val ops = ar(n) {
            iao(if (it < n / 2) 0 else
                abs(Random.nextInt()) % 7, Random.nextInt()
            )
        }
        val map1 = AVLMap<Int, Int>(Int::compareTo)
        val time1 = measureTimeMillis {
            for ((op, x) in ops) {
                if (op == 0) map1[x] = map1.getOrDefault(x, 0) + 1
                else if (op == 1) {
                    val c = map1.getOrDefault(x, 0) - 1
                    if (c <= 0) map1.remove(x)
                    else map1[x] = c
                }
                else if (op == 2) map1.containsKey(x)
                else if (op == 3 && map1.isNotEmpty() && map1.last()!!.key >= x) map1.ceiling(x)
                else if (op == 4 && map1.isNotEmpty() && map1.first()!!.key <= x) map1.floor(x)
                else if (op == 5 && map1.isNotEmpty() && map1.last()!!.key > x) map1.higher(x)
                else if (op == 6 && map1.isNotEmpty() && map1.first()!!.key < x) map1.lower(x)
            }
        }
        val map2 = TreeMap<Int, Int>()
        val time2 = measureTimeMillis {
            for ((op, x) in ops) {
                if (op == 0) map2[x] = map2.getOrDefault(x, 0) + 1
                else if (op == 1) {
                    val c = map2.getOrDefault(x, 0) - 1
                    if (c <= 0) map2.remove(x)
                    else map2[x] = c
                }
                else if (op == 2) map2.containsKey(x)
                else if (op == 3 && map2.isNotEmpty() && map2.lastKey() >= x) map2.ceilingEntry(x)
                else if (op == 4 && map2.isNotEmpty() && map2.firstKey() <= x) map2.floorEntry(x)
                else if (op == 5 && map2.isNotEmpty() && map2.lastKey() > x) map2.higherEntry(x)
                else if (op == 6 && map2.isNotEmpty() && map2.firstKey() < x) map2.lowerEntry(x)
            }
        }
        println("insert:${ops.count { it[0] == 0 }} query:${ops.count { it[0] == 1 }} ceiling:${ops.count { it[0] == 2 }}")
        println("avlMap: $time1 treeMap: $time2 diff:${time1 - time2}")
        println("===")
        diff += time1 - time2
        map1.clear()
        map2.clear()
        var success = true
        for ((op, x) in ops) {
            if (op == 0) {
                map1[x] = map1.getOrDefault(x, 0) + 1
                map2[x] = map2.getOrDefault(x, 0) + 1
                if (map1[x] != map2[x] || map1.size != map2.size) {
                    success = false
                    break
                }
            }
            else if (op == 1) {
                val c1 = map1.getOrDefault(x, 0) - 1
                if (c1 <= 0) map1.remove(x)
                else map1[x] = c1
                val c2 = map2.getOrDefault(x, 0) - 1
                if (c2 <= 0) map2.remove(x)
                else map2[x] = c2
                if (map1[x] != map2[x] || map1.size != map2.size) {
                    success = false
                    break
                }
            }
            else if (op == 2) {
                val a = map1.containsKey(x)
                val b = map2.containsKey(x)
                if (a != b) {
                    success = false
                    break
                }
            }
            else if (op == 3) {
                val a = map1.isNotEmpty() && map1.last()!!.key >= x
                val b = map2.isNotEmpty() && map2.lastKey() >= x
                if (a != b) {
                    success = false
                    break
                }
                if (a) {
                    val c = map1.ceiling(x)!!
                    val d = map2.ceilingEntry(x)
                    if (c.key != d.key) {
                        success = false
                        break
                    }
                }
            }
            else if (op == 4) {
                val a = map1.isNotEmpty() && map1.first()!!.key <= x
                val b = map2.isNotEmpty() && map2.firstKey() <= x
                if (a != b) {
                    success = false
                    break
                }
                if (a) {
                    val c = map1.floor(x)!!
                    val d = map2.floorEntry(x)
                    if (c.key != d.key) {
                        success = false
                        break
                    }
                }
            }
            else if (op == 5) {
                val a = map1.isNotEmpty() && map1.last()!!.key > x
                val b = map2.isNotEmpty() && map2.lastKey() > x
                if (a != b) {
                    success = false
                    break
                }
                if (a) {
                    val c = map1.higher(x)!!
                    val d = map2.higherEntry(x)
                    if (c.key != d.key) {
                        success = false
                        break
                    }
                }
            }
            else if (op == 6) {
                val a = map1.isNotEmpty() && map1.first()!!.key < x
                val b = map2.isNotEmpty() && map2.firstKey() < x
                if (a != b) {
                    success = false
                    break
                }
                if (a) {
                    val c = map1.lower(x)!!
                    val d = map2.lowerEntry(x)
                    if (c.key != d.key) {
                        success = false
                        break
                    }
                }
            }
        }
        if (!success) {
            println("xxx")
            return
        }
    }
    println(diff / rep.toDouble())
}

fun main() {
    tstSet(30)
//    tstMap(30)
}