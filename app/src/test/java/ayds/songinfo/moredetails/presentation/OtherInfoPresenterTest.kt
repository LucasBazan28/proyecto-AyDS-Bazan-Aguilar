package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val repository: OtherInfoRepository = mockk()
    private val lastFMDescriptionHelper: LastFMDescriptionHelper = mockk()

    private val presenter: OtherInfoPresenter = OtherInfoPresenterImpl(repository, lastFMDescriptionHelper)

    @Test
    fun `on getArtistInfo it should notify the UI state`() {

        val card: Card = mockk(relaxed = true)
        every {repository.getArtistInfo("ArtistName")} returns card
        every {card.artistName} returns "ArtistName"
        every {card.infoUrl} returns "Url"
        every { lastFMDescriptionHelper.getDescription(card) } returns "formatted biography"
        every {card.source} returns "source"
        every {card.sourceLogoUrl} returns "sourceLogo"
        val uiState = CardUiState(card.artistName, lastFMDescriptionHelper.getDescription(card), card.infoUrl, card.source, card.sourceLogoUrl)


        val artistBiographyTester: (CardUiState) -> Unit = mockk(relaxed = true)
        presenter.cardObservable.subscribe {
            artistBiographyTester(it)
        }

        presenter.getArtistInfo("ArtistName")

        verify { artistBiographyTester(uiState) }
    }

}