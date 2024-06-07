package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class WikipediaProxy(
    private val wikipediaTrackService: WikipediaTrackService
) :CardProxy {
    override fun getCard(artist: String): Card? {
        val wikiArticle = wikipediaTrackService.getInfo(artist)
        return if (wikiArticle != null) {
            Card(
                artistName = artist,
                text = wikiArticle.description,
                url = wikiArticle.wikipediaURL,
                logoUrl = wikiArticle.wikipediaLogoURL,
                source = CardSource.WIKIPEDIA,
                isLocallyStored = false
            )
        } else {
            null
        }
    }
}