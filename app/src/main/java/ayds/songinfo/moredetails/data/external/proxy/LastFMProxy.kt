package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.lastfm.data.LastFmService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

class LastFMProxy(
    private val otherInfoService: LastFmService
) {
    public fun getCard(artist: String): Card {
        val lastFMArticle = otherInfoService.getArticle(artist)
        return Card(
            artistName = lastFMArticle.artistName,
            text = lastFMArticle.biography,
            url = lastFMArticle.articleUrl,
            source = CardSource.LAST_FM,
            isLocallyStored = false
        )}
}