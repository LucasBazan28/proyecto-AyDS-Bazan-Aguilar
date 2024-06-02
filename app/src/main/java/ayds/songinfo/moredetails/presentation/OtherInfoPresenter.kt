package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface OtherInfoPresenter {
    val cardObservable: List<Observable<CardUiState>>
    fun getArtistInfo(artistName: String)

}

internal class OtherInfoPresenterImpl(
    private val repository: OtherInfoRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : OtherInfoPresenter {

    override val cardObservable: MutableList<Subject<CardUiState>> = mutableListOf()
    override fun getArtistInfo(artistName: String) {

        val artistBiography = repository.getArtistInfo(artistName)
        //artistBiography es una lista, entonces en vez de for hago uno por uno
        //                                          (Leo el uiState y lo notifico)

        val uiState = artistBiography.toUiState()

        for (card in cardObservable){
            card.notify(uiState)
        }
        //cardObservable.notify(uiState) Esto notificaba a una sola card del nuevo uiState
    }

    private fun Card.toUiState() = CardUiState(
        artistName,
        cardDescriptionHelper.getDescription(this),
        url,
        source,
        sourceLogoUrl
    )
}