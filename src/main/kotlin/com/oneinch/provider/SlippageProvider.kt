package com.oneinch.provider

import com.esaulpaugh.headlong.abi.Function
import com.esaulpaugh.headlong.abi.Tuple
import com.esaulpaugh.headlong.util.FastHex
import com.esaulpaugh.headlong.util.Strings
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class SlippageProvider(val function: Function) {

    fun modify(inputData: String, newMinReturnAmount: BigInteger): String {
        val origin = decode(function, inputData.substring(2))
        val modified = origin.setMinReturnAmount(newMinReturnAmount)
        return encode(function, modified)
    }

    private fun decode(function: Function, inputData: String): Tuple {
        return function.decodeCall(FastHex.decode(inputData))
    }

    private fun encode(function: Function, modifiedData: Tuple): String {
        return "0x${Strings.encode(function.encodeCall(modifiedData).array())}"
    }

    private fun Tuple.setMinReturnAmount(minReturnAmount: BigInteger): Tuple {
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
}