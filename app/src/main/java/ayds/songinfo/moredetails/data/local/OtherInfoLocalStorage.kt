package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

interface OtherInfoLocalStorage {
    fun getCards(artistName: String): List<Card>?
    fun insertArtist(card: Card)
}
internal class OtherInfoLocalStorageImpl(
    private val cardDatabase: CardDatabase,
) : OtherInfoLocalStorage {

    override fun getCards(artistName: String): List<Card>? {
        val artistEntities = cardDatabase.CardDao().getCardByArtistName(artistName)
        //getCardByArtistName puede devolver de 1 a 3 elementos, dependiendo si están los de los 3
        return artistEntities?.map { artistEntity ->
            Card(
                artistName = artistName,
                text = artistEntity.content,
                url = artistEntity.url,
                source = CardSource.valueOf(artistEntity.source.toUpperCase()),  // assuming source is a string that matches enum names
                isLocallyStored = false  // Default value, you can modify as needed
            )
        }
    }
    override fun insertArtist(card: Card) {

        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.artistName, card.text, card.url, card.source.ordinal
            )
        )
    }
}
