package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.CardSource

data class CardsUiState(
    val cards: List<CardUiState>
)
data class CardUiState(
    val artistName: String,
    val contentHtml: String,
    val url: String,
    val logoUrl: String,
    val source: CardSource
)