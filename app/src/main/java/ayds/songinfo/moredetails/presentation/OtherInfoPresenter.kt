package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface OtherInfoPresenter {
    val cardObservable: Observable<CardsUiState>
    fun getArtistInfo(artistName: String)

}

internal class OtherInfoPresenterImpl(
    private val repository: OtherInfoRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : OtherInfoPresenter {

    override val cardObservable = Subject<CardsUiState>()
    override fun getArtistInfo(artistName: String) {
        val cards = repository.getArtistInfo(artistName)
        val uiState = cards.toUiState()

        cardObservable.notify(uiState)
    }

    private fun List<Card>.toUiState() = CardsUiState(
        cards = map {
            CardUiState(
                artistName = it.artistName,
                contentHtml = cardDescriptionHelper.getDescription(it),
                url = it.url,
                logoUrl = it.logoUrl,
                source = it.source
            )
        }
    )
}