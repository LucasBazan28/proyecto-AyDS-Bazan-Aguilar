package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import io.mockk.every

class OtherInfoPresenterTest {

    private val repository: OtherInfoRepository = mockk(relaxed = true)
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk(relaxed = true)

    private val presenter: OtherInfoPresenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)

    @Test
    fun `on getArtistInfo it should notify the UI state`() {
        val artistBiography: ArtistBiography = mockk()
        val uiState : ArtistBiographyUiState = mockk()

        every { repository.getArtistInfo("artistName") } returns artistBiography
        every { artistBiographyDescriptionHelper.getDescription(artistBiography) } returns "formatted biography"

        val artistBiographyTester: (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)
        presenter.artistBiographyObservable.subscribe {
            artistBiographyTester(it)
        }

        presenter.getArtistInfo("artistName")

        verify { artistBiographyTester(uiState) }
    }

}