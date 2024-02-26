package com.tauros.cp.structure

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function


/**
 * @author tauros
 */
class DefaultMap<K, V, D : Map<K, V>>(protected val delegate: D, protected val default: (K) -> V) : Map<K, V> {
    override val entries: Set<Map.Entry<K, V>>
        get() = delegate.entries
    override val keys: Set<K>
        get() = delegate.keys
    override val size: Int
        get() = delegate.size
    override val values: Collection<V>
        get() = delegate.values

    override fun containsKey(key: K) = delegate.containsKey(key)
    override fun containsValue(value: V) = delegate.containsValue(value)
    override fun forEach(action: BiConsumer<in K, in V>) = delegate.forEach(action)
    override fun getOrDefault(key: K, defaultValue: V) = delegate.getOrDefault(key, defaultValue)
    override fun isEmpty() = delegate.isEmpty()
    override operator fun get(key: K) = delegate[key] ?: default(key)
}

open class DefaultMutableMap<K, V, D : MutableMap<K, V>>(protected val delegate: D, protected val default: (K) -> V) : MutableMap<K, V> {
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = delegate.entries
    override val keys: MutableSet<K>
        get() = delegate.keys
    override val size: Int
        get() = delegate.size
    override val values: MutableCollection<V>
        get() = delegate.values

    override fun clear() = delegate.clear()
    override fun compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>) = delegate.compute(key, remappingFunction)
    override fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>) = delegate.computeIfAbsent(key, mappingFunction)
    override fun computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V & Any, out V?>) = delegate.computeIfPresent(key, remappingFunction)
    override fun merge(key: K, value: V & Any, remappingFunction: BiFunction<in V & Any, in V & Any, out V?>) = delegate.merge(key, value, remappingFunction)
    override fun put(key: K, value: V) = delegate.put(key, value)
    override fun putAll(from: Map<out K, V>) = delegate.putAll(from)
    override fun putIfAbsent(key: K, value: V) = delegate.putIfAbsent(key, value)
    override fun remove(key: K) = delegate.remove(key)
    override fun remove(key: K, value: V) = delegate.remove(key, value)
    override fun replace(key: K, oldValue: V, newValue: V) = delegate.replace(key, oldValue, newValue)
    override fun replace(key: K, value: V) = delegate.replace(key, value)
    override fun replaceAll(function: BiFunction<in K, in V, out V>) = delegate.replaceAll(function)
    override fun containsKey(key: K) = delegate.containsKey(key)
    override fun containsValue(value: V) = delegate.containsValue(value)
    override fun forEach(action: BiConsumer<in K, in V>) = delegate.forEach(action)
    override fun getOrDefault(key: K, defaultValue: V) = delegate.getOrDefault(key, defaultValue)
    override fun isEmpty() = delegate.isEmpty()
    override operator fun get(key: K) = delegate[key] ?: default(key)
}

fun <K, V> Map<K, V>.default(default: (K) -> V) = DefaultMap(this, default)
fun <K, V> MutableMap<K, V>.default(default: (K) -> V) = DefaultMutableMap(this, default)


class DefaultAVLMap<K, V>(delegate: AVLMap<K, V>, default: (K) -> V) : DefaultMutableMap<K, V, AVLMap<K, V>>(delegate, default) {
    fun ceiling(key: K) = delegate.ceiling(key)
    fun floor(key: K) = delegate.floor(key)
    fun higher(key: K) = delegate.higher(key)
    fun lower(key: K) = delegate.lower(key)
    fun min() = delegate.min()
    fun max() = delegate.max()
    fun first() = delegate.first()
    fun last() = delegate.last()
}

fun <K, V> AVLMap<K, V>.default(default: (K) -> V) = DefaultAVLMap(this, default)
