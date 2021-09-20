package com.oneinch.config

import com.esaulpaugh.headlong.abi.Tuple
import java.math.BigInteger

data class Abi(
    val caller: Any,
    val components: Tuple,
    val desc: Any
) {

    fun setMinReturnAmount(minReturnAmount: BigInteger) {
        this.components[5] = minReturnAmount
    }
}

fun Abi.toTuple(): Tuple {
    val tuple = Tuple()
    tuple.add(caller)
    tuple.add(components)
    tuple.add(desc)
    return tuple
}

fun Tuple.toAbi(): Abi {
    return Abi(
        caller = this[0],
        components = this[1] as Tuple,
        desc = this[2]
    )
}