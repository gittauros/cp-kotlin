package com.tauros.cp.structure


/**
 * @author tauros
 */
// https://github.com/atcoder/ac-library/blob/master/document_en/lazysegtree.md
// ↑ atcoder的模板声明
// https://zhuanlan.zhihu.com/p/459579152
// https://zhuanlan.zhihu.com/p/459679512
// ↑ C++实现与中文说明
// https://codeforces.com/blog/entry/18051
// ↑ 线段树循环版，非递归线段树
interface SegmentOpArray<D> {
    operator fun set(pos: Int, data: D)
    operator fun get(pos: Int): D
}
fun ceilingLog(num: Int) = num.takeHighestOneBit().let { if (it == num) it else it shl 1 }.countTrailingZeroBits()
class LazySegmentTree<S, F>(val n: Int, init: ((Int) -> S)? = null,
                            private val op: (S, S) -> S, private val e: () -> S, private val mapping: S.(tag: F) -> S,
                            // f是标记，合并后相当于先用this，再用传入的
                            private val composition: F.(F) -> F, private val id: () -> F,
                            private val data: SegmentOpArray<S>, private val lazy: SegmentOpArray<F>) {
    private val log = ceilingLog(n)
    private val size = 1 shl log
    init {
        if (init != null) for (i in 0 until n) data[i + size] = init(i)
        for (i in size - 1 downTo 1) pushUp(i)
    }
    private fun accept(p: Int, f: F) {
        data[p] = data[p].mapping(f)
        if (p < size) lazy[p] = lazy[p].composition(f)
    }
    private fun pushDown(p: Int) {
        accept(p * 2, lazy[p])
        accept(p * 2 + 1, lazy[p])
        lazy[p] = id()
    }
    private fun pushUp(p: Int) {
        data[p] = op(data[p * 2], data[p * 2 + 1])
    }
    operator fun set(pos: Int, x: S) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        data[p] = x
        for (i in 1 .. log) pushUp(p shr i)
    }
    operator fun get(pos: Int): S {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        return data[p]
    }
    fun prod(st: Int, ed: Int): S {
        if (st == ed) return e()
        var (cl, cr) = st + size to ed + size
        val (clo, cro) = cl.countTrailingZeroBits() to cr.countTrailingZeroBits()
        for (i in log downTo 1) {
            if (clo < i) pushDown(cl shr i)
            if (cro < i) pushDown(cr - 1 shr i)
        }
        var (l, r) = e() to e()
        while (cl < cr) {
            if (cl and 1 == 1) l = op(l, data[cl++])
            if (cr and 1 == 1) r = op(data[--cr], r)
            cl = cl shr 1; cr = cr shr 1
        }
        return op(l, r)
    }
    fun apply(pos: Int, f: F) {
        val p = pos + size
        for (i in log downTo 1) pushDown(p shr i)
        data[p] = data[p].mapping(f)
        for (i in 1 .. log) pushUp(p shr i)
    }
    fun apply(st: Int, ed: Int, f: F) {
        if (st == ed) return
        val (cl, cr) = st + size to ed + size
        val (clo, cro) = cl.countTrailingZeroBits() to cr.countTrailingZeroBits()
        for (i in log downTo 1) {
            if (clo < i) pushDown(cl shr i)
            if (cro < i) pushDown(cr - 1 shr i)
        }
        run {
            var (l, r) = cl to cr
            while (l < r) {
                if (l and 1 == 1) accept(l++, f)
                if (r and 1 == 1) accept(--r, f)
                l = l shr 1; r = r shr 1
            }
        }
        for (i in 1 .. log) {
            if (clo < i) pushUp(cl shr i)
            if (cro < i) pushUp(cr - 1 shr i)
        }
    }
}

inline fun <reified D> newSegOpArray(size: Int, init: (Int) -> D) = object : SegmentOpArray<D> {
    val array = Array<Any?>(size) { null }
    override fun set(pos: Int, data: D) { array[pos] = data }
    override fun get(pos: Int) = array[pos]!! as D
}.apply { for (i in 0 until size) this[i] = init(i) }
fun newIntSegOpArray(size: Int, init: (Int) -> Int = { 0 }) = object : SegmentOpArray<Int> {
    val array = IntArray(size)
    override fun set(pos: Int, data: Int) { array[pos] = data }
    override fun get(pos: Int) = array[pos]
}.apply { for (i in 0 until size) this[i] = init(i) }
fun newLongSegOpArray(size: Int, init: (Int) -> Long = { 0 }) = object : SegmentOpArray<Long> {
    val array = LongArray(size)
    override fun set(pos: Int, data: Long) { array[pos] = data }
    override fun get(pos: Int) = array[pos]
}.apply { for (i in 0 until size) this[i] = init(i) }
fun newBooleanSegOpArray(size: Int, init: (Int) -> Boolean = { false }) = object : SegmentOpArray<Boolean> {
    val array = BooleanArray(size)
    override fun set(pos: Int, data: Boolean) { array[pos] = data }
    override fun get(pos: Int) = array[pos]
}.apply { for (i in 0 until size) this[i] = init(i) }
inline fun <reified S, reified F> lazySeg(
    n: Int, noinline init: (Int) -> S, noinline op: (S, S) -> S, noinline e: () -> S,
    noinline mapping: S.(tag: F) -> S, noinline composition: F.(F) -> F, noinline id: () -> F
): LazySegmentTree<S, F> {
    val size = 1 shl ceilingLog(n)
    val dataArray = newSegOpArray<S>(size * 2) { e() }
    val lazyArray = newSegOpArray<F>(size) { id() }
    return LazySegmentTree(n, init, op, e, mapping, composition, id, dataArray, lazyArray)
}
inline fun <reified S> lazySBSeg(
    n: Int, noinline init: (Int) -> S, noinline op: (S, S) -> S, noinline e: () -> S,
    noinline mapping: S.(tag: Boolean) -> S, noinline composition: Boolean.(Boolean) -> Boolean, noinline id: () -> Boolean
): LazySegmentTree<S, Boolean> {
    val size = 1 shl ceilingLog(n)
    val dataArray = newSegOpArray<S>(size * 2) { e() }
    val lazyArray = newBooleanSegOpArray(size) { id() }
    return LazySegmentTree(n, init, op, e, mapping, composition, id, dataArray, lazyArray)
}
inline fun <reified F> lazyIFSeg(
    n: Int, noinline init: (Int) -> Int, noinline op: (Int, Int) -> Int, noinline e: () -> Int,
    noinline mapping: Int.(tag: F) -> Int, noinline composition: F.(F) -> F, noinline id: () -> F
): LazySegmentTree<Int, F> {
    val size = 1 shl ceilingLog(n)
    val dataArray = newIntSegOpArray(size * 2) { e() }
    val lazyArray = newSegOpArray<F>(size) { id() }
    return LazySegmentTree(n, init, op, e, mapping, composition, id, dataArray, lazyArray)
}

// 自己的版本
data class SegmentNode<Info>(val cl: Int, val cr: Int, val info: Info) {
    val mid: Int
        get() = cl + cr shr 1
    var l: SegmentNode<Info>? = null
    var r: SegmentNode<Info>? = null
}
class SegmentTree<Info>(start: Int, end: Int,
                        private val defaultInfo: () -> Info, private val pushUp: Info.(Info, Info) -> Unit = { _, _ -> },
                        private val needPushDown: Info.() -> Boolean = { false }, private val clearPushDown: Info.() -> Unit = { },
                        private val accept: Info.(Info) -> Unit = { }) {
    data class SegmentNode<Info>(val cl: Int, val cr: Int, val info: Info) {
        val mid: Int
            get() = cl + cr shr 1
        var l: SegmentNode<Info>? = null
        var r: SegmentNode<Info>? = null
    }
    private fun SegmentNode<Info>.build(init: Info.(Int) -> Unit) {
        if (cl == cr) { info.init(cl); return }
        l = SegmentNode(cl, mid, defaultInfo())
        r = SegmentNode(mid + 1, cr, defaultInfo())
        l?.build(init); r?.build(init)
        pushUp()
    }
    private fun SegmentNode<Info>.pushDown() {
        if (cl == cr) return
        if (l == null) l = SegmentNode(cl, mid, defaultInfo())
        if (r == null) r = SegmentNode(mid + 1, cr, defaultInfo())
        if (info.needPushDown()) {
            l?.info?.accept(info)
            r?.info?.accept(info)
            info.clearPushDown()
        }
    }
    private fun SegmentNode<Info>.pushUp() { info.pushUp(l!!.info, r!!.info) }
    private fun SegmentNode<Info>.update(st: Int, ed: Int, upd: Info.() -> Unit) {
        if (cl > ed || cr < st) return
        if (cl >= st && cr <= ed) { info.upd(); return }
        pushDown()
        l?.update(st, ed, upd); r?.update(st, ed, upd)
        pushUp()
    }
    private fun <T> SegmentNode<Info>.query(st: Int, ed: Int, query: Info.() -> T, merge: (T?, T?) -> T): T? {
        if (cl > ed || cr < st) return null
        if (cl >= st && cr <= ed) return info.query()
        pushDown()
        return merge(l?.query(st, ed, query, merge), r?.query(st, ed, query, merge))
    }
    val seg = SegmentNode(start, end, defaultInfo())
    fun build(init: Info.(Int) -> Unit) = seg.build(init)
    fun update(st: Int, ed: Int, upd: Info.() -> Unit) = seg.update(st, ed, upd)
    fun <T> query(st: Int, ed: Int, query: Info.() -> T, merge: (T?, T?) -> T) = seg.query(st, ed, query, merge)!!
}