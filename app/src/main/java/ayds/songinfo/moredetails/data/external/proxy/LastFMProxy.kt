package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.lastfm.data.LastFmService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class LastFMProxy(
    private val otherInfoService: LastFmService
) : CardProxy{
    override fun getCard(artist: String): Card? {
        val lastFMArticle = otherInfoService.getArticle(artist)
        if (lastFMArticle.biography.isNotEmpty()){
            return Card(
                artistName = lastFMArticle.artistName,
                text = lastFMArticle.biography,
                url = lastFMArticle.articleUrl,
                source = CardSource.LAST_FM,
                logoUrl = lastFMArticle.sourceLogoUrl,
                isLocallyStored = false
            )
        }
        return null
    }
}