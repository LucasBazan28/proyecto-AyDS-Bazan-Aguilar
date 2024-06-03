package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

class WikipediaProxy(
    private val wikipediaTrackService: WikipediaTrackService
) {
    public fun getCard(artistName: String): Card {
        val wikiArticle = wikipediaTrackService.getInfo(artistName)
        return if (wikiArticle != null) {
            Card(
                artistName = artistName,
                text = wikiArticle.description,
                url = wikiArticle.wikipediaURL,
                source = CardSource.WIKIPEDIA,
                isLocallyStored = false
            )
        } else {
            Card(
                artistName = artistName,
                text = "",
                url = "",
                source = CardSource.WIKIPEDIA,
                isLocallyStored = false
            )
        }
    }
}