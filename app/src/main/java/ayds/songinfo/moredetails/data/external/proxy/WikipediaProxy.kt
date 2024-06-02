package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card

class WikipediaProxy(
    private val wikipediaTrackService: WikipediaTrackService
) {
    public fun getCard(artistName: String): Card {
        val wikiArticle = wikipediaTrackService.getInfo(artistName)
        if(wikiArticle != null)
            return Card(artistName, wikiArticle.description, wikiArticle.wikipediaURL,"Wikipedia", wikiArticle.wikipediaLogoURL, false)
        else
            return Card(artistName, "", "","Wikipedia","", false)
    }
}