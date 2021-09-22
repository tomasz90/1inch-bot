package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.repository.dao.Status
import com.oneinch.repository.dao.RealTxEntity
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
        from: TokenQuote,
        to: TokenQuote,
        status: Status
    ) {
        if (txHash == null) {
            getLogger().error("TxHash is null, did not receive proper response")
            return
        }
        val rtx = RealTxEntity(
            requestTimeStamp = tx.requestTimestamp,
            chainId = chain.id,
            hash = txHash,
            fromSymbol = from.token.symbol,
            fromAddress = from.token.address,
            fromReadable = from.calcReadable().round(),
            fromAmount = from.origin.toString(),
            toSymbol = to.token.symbol,
            toAddress = to.token.address,
            toReadable = to.calcReadable().round(),
            toAmount = to.origin.toString(),
            gasPrice = tx.gasPrice.toString(),
            minReturnAmount = tx.minReturnAmount.toString(),
            advantage = tx.advantage,
            status = status
        )
        iRealTxRepository.save(rtx)
    }
}

fun Double.round() = Math.round(this * 100.0) / 100.0