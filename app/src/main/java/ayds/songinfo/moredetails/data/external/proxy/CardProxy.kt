package ayds.songinfo.moredetails.data.external.proxy

import ayds.songinfo.moredetails.domain.Card

interface CardProxy {
    fun getCard(artist: String): Card?
}