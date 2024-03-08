package com.tauros.cp.structure


/**
 * @author tauros
 */
class IntList(private var cap: Int = MIN_CAP) : MutableList<Int> {
    companion object {
        val MIN_CAP = 10
        val MAX_CAP = 1e8.toInt()
    }
    var array: IntArray = IntArray(cap)
    var idx = 0
    private fun newCapacity(old: Int) = minOf(MAX_CAP, maxOf(MIN_CAP, old + (old shr 1)))
    private fun ensureCapacity(new: Int) {
        while (cap < new) {
            val newCap = newCapacity(cap)
            array = array.copyOf(newCap)
            cap = newCap
        }
    }
    override val size: Int
        get() = idx
    override fun contains(element: Int) = (0 until idx).any { array[it] == element }
    override fun containsAll(elements: Collection<Int>) = elements.all { it in this }
    override fun indexOf(element: Int) = (0 until idx).firstOrNull { array[it] == element } ?: -1
    override fun isEmpty() = idx == 0
    override fun iterator() = object : MutableIterator<Int> {
        private var iter = -1
        override fun hasNext() = (iter + 1 < idx).also { if (it) iter += 1 }
        override fun next() = array[iter]
        override fun remove() {
            if (iter != idx - 1) array = array.copyOfRange(0, iter) + array.copyOfRange(iter + 1, idx)
            idx -= 1
        }
    }
    override fun lastIndexOf(element: Int) = (idx - 1 downTo 0).firstOrNull { array[it] == element } ?: -1
    override fun add(element: Int): Boolean {
        ensureCapacity(idx + 1)
        array[idx++] = element
        return true
    }
    override fun add(index: Int, element: Int) {
        ensureCapacity(idx + 1)
        System.arraycopy(array, index, array, index + 1, idx - index)
        array[index] = element
        idx += 1
    }
    override fun addAll(index: Int, elements: Collection<Int>): Boolean {
        ensureCapacity(idx + elements.size)
        System.arraycopy(array, index, array, index + elements.size, idx - index)
        var pos = index; for (element in elements) array[pos++] = element
        idx += elements.size
        return true
    }
    override fun addAll(elements: Collection<Int>): Boolean {
        elements.forEach { add(it) }
        return true
    }
    override fun clear() {
        idx = 0
    }
    override fun listIterator(): MutableListIterator<Int> {
        TODO("Not yet implemented")
    }
    override fun listIterator(index: Int): MutableListIterator<Int> {
        TODO("Not yet implemented")
    }
    override fun remove(element: Int): Boolean {
        TODO("Not yet implemented")
    }
    override fun removeAll(elements: Collection<Int>): Boolean {
        TODO("Not yet implemented")
    }
    override fun removeAt(index: Int): Int {
        TODO("Not yet implemented")
    }
    override fun retainAll(elements: Collection<Int>): Boolean {
        TODO("Not yet implemented")
    }
    private fun checkIndex(index: Int) {
        if (index < 0 || index >= idx) throw IndexOutOfBoundsException(index.toString())
    }
    override fun get(index: Int): Int {
        checkIndex(index)
        return array[index]
    }
    override fun set(index: Int, element: Int): Int {
        checkIndex(index)
        val old = array[index]
        array[index] = element
        return old
    }
    override fun subList(fromIndex: Int, toIndex: Int) = array.copyOfRange(fromIndex, toIndex).toIntList()
    fun toIntArray() = array.copyOfRange(0, idx)
    fun sort() = array.sort(0, idx)
}

fun IntArray.toIntList(): IntList {
    val res = IntList(this.size)
    this.copyInto(res.array, 0, 0, this.size)
    res.idx = this.size
    return res
}

fun List<Int>.toIntList(): IntList {
    val res = IntList(this.size)
    for (num in this) res.add(num)
    return res
}