package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.data.external.proxy.LastFMProxy
import ayds.songinfo.moredetails.data.external.proxy.NYTimesProxy
import ayds.songinfo.moredetails.data.external.proxy.WikipediaProxy
import ayds.songinfo.moredetails.domain.Card

class Broker(
    private val lastFMProxy: LastFMProxy,
    private val nyTimesProxy: NYTimesProxy,
    private val wikipediaProxy: WikipediaProxy,
) {
    public fun getCardsList(artist: String): List<Card> {
        return listOf(
            wikipediaProxy.getCard(artist),
            nyTimesProxy.getCard(artist),
            lastFMProxy.getCard(artist)
        )
    }
}