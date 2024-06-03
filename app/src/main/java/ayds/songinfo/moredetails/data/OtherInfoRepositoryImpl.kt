package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.Broker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val broker: Broker,
) : OtherInfoRepository {

    override fun getArtistInfo(artistName: String): List<Card> {
        val dbArticles = otherInfoLocalStorage.getCards(artistName)
        //Acá pedir las 3 cards de la BD

        dbArticles?.forEach { card ->
            card.markItAsLocal()
        }
        //Me fijo cuales tengo (Según sources), pido todas por broker y luego combino.
        val existingCardSources = dbArticles?.map { it.source }?.toSet() ?: emptySet()
        val brokerCards = broker.getCardsList(artistName)
        val actualBrokerCards = brokerCards.filter { it.source !in existingCardSources }


        val combinedCards = (dbArticles ?: emptyList()) + actualBrokerCards

        return combinedCards
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }
}