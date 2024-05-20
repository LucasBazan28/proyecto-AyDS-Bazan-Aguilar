package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class OtherInfoServiceImplTest {

    private val lastFMAPI: LastFMAPI = mockk(relaxUnitFun = true)
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolverTest = mockk(relaxUnitFun = true)

    private val otherInfoService: OtherInfoService =
        OtherInfoServiceImpl(lastFMAPI, lastFMToArtistBiographyResolver)

    @Test
    fun `given non existing artist should return empty biography`() {
        val artistName = "non-existing-artist"
        val emptyBiography = ArtistBiography(artistName, "", "")
        every { lastFMAPI.getArtistInfo(artistName).execute() } returns Response.success(emptyBiography)

        val result = otherInfoService.getArticle(artistName)

        assertEquals(emptyBiography, result)
    }

    @Test
    fun `given existing artist should return biography`() {
        val artistName = "existing-artist"
        val biography: ArtistBiography = mockk()
        every { lastFMAPI.getArtistInfo(artistName).execute() } returns Response.success(biography)
        every { lastFMToArtistBiographyResolver.map(biography, artistName) } returns biography

        val result = otherInfoService.getArticle(artistName)

        assertEquals(biography, result)
    }

    @Test
    fun `given service exception should return empty biography`() {
        val artistName = "exception-artist"
        val emptyBiography = ArtistBiography(artistName, "", "")
        every { lastFMAPI.getArtistInfo(artistName).execute() } throws mockk<IOException>()

        val result = otherInfoService.getArticle(artistName)

        assertEquals(emptyBiography, result)
    }
}
