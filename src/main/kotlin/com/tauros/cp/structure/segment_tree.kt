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

// 使用时是半闭半开区间，区间端点必须从0开始
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
        accept(p shl 1, lazy[p])
        accept(p shl 1 or 1, lazy[p])
        lazy[p] = id()
    }
    private fun pushUp(p: Int) {
        data[p] = op(data[p shl 1], data[p shl 1 or 1])
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
    fun prodAll() = data[1]
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
        var (l, r) = cl to cr
        while (l < r) {
            if (l and 1 == 1) accept(l++, f)
            if (r and 1 == 1) accept(--r, f)
            l = l shr 1; r = r shr 1
        }
        for (i in 1 .. log) {
            if (clo < i) pushUp(cl shr i)
            if (cro < i) pushUp(cr - 1 shr i)
        }
    }
}

fun <S, F> lazySeg(
    n: Int, init: (Int) -> S, op: (S, S) -> S, e: () -> S,
    mapping: S.(tag: F) -> S, composition: F.(F) -> F, id: () -> F,
    newDataArray: (Int) -> SegmentOpArray<S>, newLazyArray: (Int) -> SegmentOpArray<F>
): LazySegmentTree<S, F> {
    val size = 1 shl ceilingLog(n)
    val dataArray = newDataArray(size shl 1)
    val lazyArray = newLazyArray(size)
    return LazySegmentTree(n, init, op, e, mapping, composition, id, dataArray, lazyArray)
}

// 自己的版本，比上面的时间快些，内存大些
// 使用时是闭区间
interface SegmentTag<T : SegmentTag<T>> {
    fun accept(other: T)
    fun available(): Boolean
    fun clear()
}
interface SegmentData<D : SegmentData<D, T>, T : SegmentTag<T>> {
    fun accept(tag: T)
    fun calc(l: D, r: D)
}
class SegmentTree<D : SegmentData<D, T>, T : SegmentTag<T>>(
    start: Int, end: Int, private val defaultData: () -> D, private val defaultTag: () -> T) {
    data class SegmentNode<D, T>(val cl: Int, val cr: Int, val info: D, val tag: T?) {
        val mid: Int
            get() = cl + cr shr 1
        var l: SegmentNode<D, T>? = null
        var r: SegmentNode<D, T>? = null
    }
    private fun SegmentNode<D, T>.build(init: D.(Int) -> Unit) {
        if (cl == cr) { info.init(cl); return }
        l = SegmentNode(cl, mid, defaultData(), if (cl == mid) null else defaultTag())
        r = SegmentNode(mid + 1, cr, defaultData(), if (mid + 1 == cr) null else defaultTag())
        l?.build(init); r?.build(init)
        pushUp()
    }
    private fun SegmentNode<D, T>.pushDown() {
        if (cl == cr) return
        if (l == null) l = SegmentNode(cl, mid, defaultData(), if (cl == mid) null else defaultTag())
        if (r == null) r = SegmentNode(mid + 1, cr, defaultData(), if (mid + 1 == cr) null else defaultTag())
        if (tag != null && tag.available()) {
            l?.tag?.accept(tag); l?.info?.accept(tag)
            r?.tag?.accept(tag); r?.info?.accept(tag)
            tag.clear()
        }
    }
    private fun SegmentNode<D, T>.pushUp() { info.calc(l!!.info, r!!.info) }
    private fun SegmentNode<D, T>.update(st: Int, ed: Int, upd: T) {
        if (cl > ed || cr < st) return
        if (cl >= st && cr <= ed) { tag?.accept(upd); info.accept(upd); return }
        pushDown()
        l?.update(st, ed, upd); r?.update(st, ed, upd)
        pushUp()
    }
    private fun <R> SegmentNode<D, T>.query(st: Int, ed: Int, query: D.() -> R, merge: (R?, R?) -> R): R? {
        if (cl > ed || cr < st) return null
        if (cl >= st && cr <= ed) return info.query()
        pushDown()
        return merge(l?.query(st, ed, query, merge), r?.query(st, ed, query, merge))
    }
    val seg = SegmentNode(start, end, defaultData(), defaultTag())
    fun build(init: D.(Int) -> Unit) = seg.build(init)
    fun update(st: Int, ed: Int, upd: T) = seg.update(st, ed, upd)
    fun <R> query(st: Int, ed: Int, query: D.() -> R, merge: (R?, R?) -> R) = seg.query(st, ed, query, merge)!!
    fun <R> queryAll(query: D.() -> R) = seg.info.query()
}