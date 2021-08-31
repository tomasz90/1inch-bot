package common

import quote_request.api.data.Token

class Chain(val id: Int, val tokens: List<Token>, val rpc: String)