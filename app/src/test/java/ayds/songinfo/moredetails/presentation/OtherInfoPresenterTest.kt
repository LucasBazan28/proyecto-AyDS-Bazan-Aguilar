package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val repository: OtherInfoRepository = mockk()
    private val cardDescriptionHelper: CardDescriptionHelper = mockk()

    private val presenter: OtherInfoPresenter = OtherInfoPresenterImpl(repository, cardDescriptionHelper)

    @Test
    fun `on getArtistInfo it should notify the UI state`() {

        val card = Card("ArtistName", "Url", "biography", "source", CardSource.LAST_FM)
        val card2 = Card("ArtistName2", "Url2", "biography2", "source2", CardSource.NY_TIMES)
        val card3 = Card("ArtistName3", "Url3", "biography3", "source3", CardSource.WIKIPEDIA)

        every {repository.getArtistInfo("ArtistName")} returns listOf(card, card2, card3)
        every { cardDescriptionHelper.getDescription(card) } returns "formatted biography"
        every { cardDescriptionHelper.getDescription(card2) } returns "formatted biography2"
        every { cardDescriptionHelper.getDescription(card3) } returns "formatted biography3"

        val cardUiState = CardUiState(card.artistName, cardDescriptionHelper.getDescription(card), card.url, card.logoUrl, card.source)
        val card2UiState = CardUiState(card2.artistName, cardDescriptionHelper.getDescription(card2), card2.url, card2.logoUrl, card2.source)
        val card3UiState = CardUiState(card3.artistName, cardDescriptionHelper.getDescription(card3), card3.url, card3.logoUrl, card3.source)

        val uiStates = CardsUiState(listOf(cardUiState, card2UiState, card3UiState))
        val artistBiographyTester: (CardsUiState) -> Unit = mockk(relaxed = true)
        presenter.cardObservable.subscribe {
            artistBiographyTester(it)
        }

        presenter.getArtistInfo("ArtistName")

        verify { artistBiographyTester(uiStates) }
    }

}