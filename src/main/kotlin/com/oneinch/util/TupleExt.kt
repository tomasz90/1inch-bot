package com.oneinch.util

import com.esaulpaugh.headlong.abi.Tuple
import java.math.BigInteger

fun Tuple.setMinReturnAmount(minReturnAmount: BigInteger): Tuple {
    val originSubTuple = this[1] as Tuple
    val subTuple = Tuple.of(
        originSubTuple[0],
        originSubTuple[1],
        originSubTuple[2],
        originSubTuple[3],
        originSubTuple[4],
        minReturnAmount,
        originSubTuple[6],
        originSubTuple[7]
    )
    return Tuple.of(this[0], subTuple, this[2])
}