package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: ayds.artist.external.lastfm.data.OtherInfoService,
) : OtherInfoRepository {

    override fun getArtistInfo(artistName: String): Card {
        val dbArticle = otherInfoLocalStorage.getArticle(artistName)

        val card: Card

        if (dbArticle != null) {
            card = dbArticle.apply { markItAsLocal() }
        } else {
            val lastFMArticle = otherInfoService.getArticle(artistName)
            card = Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, "LastFM", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png")
            if (card.description.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(card)
            }
        }
        return card
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }
}