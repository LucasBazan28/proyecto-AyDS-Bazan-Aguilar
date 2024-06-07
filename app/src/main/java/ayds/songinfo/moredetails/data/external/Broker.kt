package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.data.external.proxy.CardProxy
import ayds.songinfo.moredetails.domain.Card

interface OtherInfoBroker {

    fun getCards(artistName: String): List<Card>
}
internal class OtherInfoBrokerImpl(
    private val otherInfoProxies: List<CardProxy>
) : OtherInfoBroker {

    override fun getCards(artistName: String): List<Card> {
        return otherInfoProxies.mapNotNull { it.getCard(artistName) }
    }

}