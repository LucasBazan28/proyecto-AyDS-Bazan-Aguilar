package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.Broker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val lastFmService: ayds.artist.external.lastfm.data.LastFmService,
    private val broker: Broker,
) : OtherInfoRepository {

    override fun getArtistInfo(artistName: String): Card {
        val dbArticle = otherInfoLocalStorage.getCards(artistName)
        //Acá pedir las 3 cards de la BD

        //Luego acá ciclar por la lista de a lo sumo 3 elementos

        //Puedo hacer que el if quede dentro de un for y manejarlo con un when
        //Puedo hacer un when directamente
        //Puedo manejarlo con varios if
        val card: Card

        if (dbArticle != null) {
            card = dbArticle.apply { markItAsLocal() }
        } else {
            val cards = broker.getCardsList(artistName)

            val lastFMArticle = lastFmService.getArticle(artistName)
            card = Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, "LastFM", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png")
            if (card.text.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(card)
            }
        }
        return card
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }
}