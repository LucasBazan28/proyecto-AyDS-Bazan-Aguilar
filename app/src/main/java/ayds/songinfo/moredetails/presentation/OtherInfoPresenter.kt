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
        val cards = repository.getArtistInfo(artistName)
        val uiStates = cards.map { it.toUiState() }

        // Nos aseguramos de que ambas listas tengan el mismo tamaño antes de continuar
        if (uiStates.size != cardObservable.size) {
            throw IllegalStateException("The number of cards and cardObservable must be equal")
        }

        for (i in cards.indices) {
            val uiState = uiStates[i]
            val observable = cardObservable[i]

            // Notificar el estado de la tarjeta a su correspondiente observable
            observable.notify(uiState)
        }
        //cardObservable.notify(uiState) Esto notificaba a una sola card del nuevo uiState
    }

    private fun Card.toUiState() = CardUiState(
        artistName,
        cardDescriptionHelper.getDescription(this),
        url,
        source,
    )
}