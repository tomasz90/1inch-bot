package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
import com.oneinch.getLogger
import com.oneinch.repository.dao.RealTxEntity
import com.oneinch.repository.dao.TokenEntity
import org.springframework.stereotype.Component
import java.math.BigInteger

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

    fun saveTransaction(from: TokenQuote, to: TokenQuote, gasPrice: BigInteger, txHash: String?, maxSlippage: Double) {
        if(txHash == null) {
            getLogger().error("TxHash is null, did not receive proper response")
            return
        }
        val tx = RealTxEntity(
            chainId = chain.id,
            hash = txHash,
            fromReadable = from.calcReadable(chain).round(),
            toReadable = to.calcReadable(chain).round(),
            fromAddress = from.address,
            toAddress = to.address,
            fromAmount = from.origin.toString(),
            toAmount = to.origin.toString(),
            gasPrice = gasPrice,
            maxSlippage = maxSlippage
        )
        iRealTxRepository.save(tx)
    }
}

fun Double.round() = Math.round(this * 100.0) / 100.0