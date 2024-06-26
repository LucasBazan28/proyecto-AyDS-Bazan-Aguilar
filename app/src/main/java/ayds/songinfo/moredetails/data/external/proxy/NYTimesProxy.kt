package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class NYTimesProxy (
    private val nyTimesService: NYTimesService
) : CardProxy{
    override fun getCard(artist: String): Card? {
        val nytimesArticle = nyTimesService.getArtistInfo(artist)
        return when(nytimesArticle) {
            is NYTimesArticle.NYTimesArticleWithData -> {
                Card(
                    artistName = nytimesArticle.name ?: "",
                    text = nytimesArticle.info ?: "",
                    url = nytimesArticle.url,
                    logoUrl = NYT_LOGO_URL,
                    source = CardSource.NY_TIMES
                )
            }
            else -> null
                // En caso de NYTimesArticle.EmptyArtistDataExternal u otros posibles casos

        }
    }
}