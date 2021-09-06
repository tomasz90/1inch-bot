package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
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

    fun saveTransaction(from: TokenQuote, to: TokenQuote, gasPrice: BigInteger, txHash: String, maxSlippage: Double) {
        val tx = RealTxEntity(
            chain.id,
            txHash,
            gasPrice,
            maxSlippage,
            from.address,
            from.origin.toString(),
            from.calcReadable(chain),
            to.address,
            to.origin.toString(),
            to.calcReadable(chain)
        )
        iRealTxRepository.save(tx)
    }
}