package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.CardSource

class SourceResolver {
    fun mapIntToCardSource(source: Int): CardSource {
        return when (source) {
            0 -> CardSource.LAST_FM
            1 -> CardSource.WIKIPEDIA
            2 -> CardSource.NY_TIMES
            else -> throw IllegalArgumentException("Unknown source value: $source")
        }
    }
}