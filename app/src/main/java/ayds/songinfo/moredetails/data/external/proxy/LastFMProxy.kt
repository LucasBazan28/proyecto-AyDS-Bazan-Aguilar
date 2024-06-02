package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.lastfm.data.LastFmService
import ayds.songinfo.moredetails.domain.Card

class LastFMProxy(
    private val otherInfoService: LastFmService
) {
    public fun getCard(artist: String): Card {
        val lastFMArticle = otherInfoService.getArticle(artist)
        return Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, "LastFM", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png")
    }
}