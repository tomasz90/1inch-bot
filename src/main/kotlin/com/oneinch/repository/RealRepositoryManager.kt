package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.repository.crud.IRealTxRepository
import com.oneinch.repository.crud.ITokenEntityRepository
import com.oneinch.repository.dao.RealTxEntity
import com.oneinch.repository.dao.Status
import com.oneinch.repository.dao.TokenEntity
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class RealRepositoryManager(
    val iTokenEntityRepository: ITokenEntityRepository,
    val iRealTxRepository: IRealTxRepository,
    val chain: Chain
) {

    init {
        if (iTokenEntityRepository.findByChainId(chain.id).isEmpty()) {
            chain.tokens.forEach {
                val tokenEntity = TokenEntity(it.symbol, it.address, chain.id)
                iTokenEntityRepository.save(tokenEntity)
            }
        }
    }

    fun saveTransaction(
        txHash: String?,
        tx: Transaction,
        requestTimeS: Double,
        txTimeS: Double,
        sendTxTimeStamp: Date,
        from: TokenQuote,
        to: TokenQuote,
        returnAmount: BigInteger,
        status: Status
    ) {
        if (txHash == null) {
            getLogger().error("TxHash is null, did not receive proper response")
            return
        }
        val rtx = RealTxEntity(
            sendTxTime = sendTxTimeStamp,
            requestTimeS = requestTimeS,
            txTimeS = txTimeS,
            chainId = chain.id,
            hash = txHash,
            fromSymbol = from.token.symbol,
            fromReadable = from.usdValue.round(),
            fromAmount = from.origin.toString(),
            toSymbol = to.token.symbol,
            toReadable = to.usdValue.round(),
            toAmount = to.origin.toString(),
            gasPrice = (tx.gasPrice.toDouble()/1000000000).round(),
            returnAmount = returnAmount.toString(),
            minReturnAmount = tx.minReturnAmount.toString(),
            advantage = tx.advantage.round(),
            status = status
        )
        iRealTxRepository.save(rtx)
    }
}

fun Double.round() = Math.round(this * 100.0) / 100.0