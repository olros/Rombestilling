package ntnu.idatt2105.sercurity.extractor

interface TokenExtractor {
    fun extract(payload: String): String
}

