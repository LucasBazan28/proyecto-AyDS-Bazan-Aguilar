package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
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

        val card: Card = mockk(relaxed = true)
        every {repository.getArtistInfo("ArtistName")} returns card
        every {card.artistName} returns "ArtistName"
        every {card.url} returns "Url"
        every { cardDescriptionHelper.getDescription(card) } returns "formatted biography"
        every {card.source} returns "source"
        every {card.sourceLogoUrl} returns "sourceLogo"
        val uiState = CardUiState(card.artistName, cardDescriptionHelper.getDescription(card), card.url, card.source, card.sourceLogoUrl)


        val artistBiographyTester: (CardUiState) -> Unit = mockk(relaxed = true)
        presenter.cardObservable.subscribe {
            artistBiographyTester(it)
        }

        presenter.getArtistInfo("ArtistName")

        verify { artistBiographyTester(uiState) }
    }

}