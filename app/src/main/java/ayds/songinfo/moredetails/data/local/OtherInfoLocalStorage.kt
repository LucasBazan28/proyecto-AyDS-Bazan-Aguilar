package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.presentation.SourceResolver

interface OtherInfoLocalStorage {
    fun getCards(artistName: String): List<Card>?
    fun insertArtist(card: Card)
}
internal class OtherInfoLocalStorageImpl(
    private val cardDatabase: CardDatabase,
) : OtherInfoLocalStorage {

    override fun getCards(artistName: String): List<Card>? {
        val cardEntities = cardDatabase.CardDao().getCardByArtistName(artistName)
        //getCardByArtistName puede devolver de 1 a 3 elementos, dependiendo si están los de los 3
        val sourceResolver = SourceResolver()
        return cardEntities?.map { cardEntity ->
            Card(
                artistName = artistName,
                text = cardEntity.content,
                url = cardEntity.url,
                source = sourceResolver.mapIntToCardSource(cardEntity.source),  // assuming source is a string that matches enum names
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
