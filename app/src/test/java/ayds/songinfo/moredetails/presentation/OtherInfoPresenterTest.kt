package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import io.mockk.every

class OtherInfoPresenterTest {

    private val repository: OtherInfoRepository = mockk()
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk()

    private val presenter: OtherInfoPresenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)

    @Test
    fun `on getArtistInfo it should notify the UI state`() {

        val artistBiography: ArtistBiography = mockk(relaxed = true)
        every {repository.getArtistInfo("ArtistName")} returns artistBiography
        every {artistBiography.artistName} returns "ArtistName"
        every {artistBiography.articleUrl} returns "Url"
        every { artistBiographyDescriptionHelper.getDescription(artistBiography) } returns "formatted biography"
        val uiState = ArtistBiographyUiState(artistBiography.artistName, artistBiographyDescriptionHelper.getDescription(artistBiography), artistBiography.articleUrl)


        val artistBiographyTester: (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)
        presenter.artistBiographyObservable.subscribe {
            artistBiographyTester(it)
        }

        presenter.getArtistInfo("ArtistName")

        verify { artistBiographyTester(uiState) }
    }

}