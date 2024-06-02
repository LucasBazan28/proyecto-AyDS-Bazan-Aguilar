package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.data.LastFmService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OtherInfoRepositoryImplTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)
    private val otherInfoService: LastFmService = mockk()


    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)


    @Test
    fun `given existing artist in local storage should return local artist biography`() {
        val card = Card("Artist Name", "Biography", "Url", "Source", "SourceLogo", isLocallyStored = false)
        every { otherInfoLocalStorage.getCards("Artist Name") } returns card

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(card, result)
        assertEquals(true, result.isLocallyStored) //verify {artistBiography.markItAsLocal()}
        //Con verify verificamos que realmente se llame al metodo markItAsLocal(), con el otro solo comparamos

    }

    @Test
    fun `given non existing artist in local storage should fetch from service and store locally`() {
        val lastFMArticle = LastFMArticle("Artist Name", "Biography", "Url")
        every { otherInfoLocalStorage.getCards("Artist Name") } returns null
        every { otherInfoService.getArticle("Artist Name") } returns lastFMArticle
        val card = Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, "LastFM", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png", false)

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(card, result)
        assertEquals(false, result.isLocallyStored)
        verify { otherInfoLocalStorage.insertArtist(card) }
    }

    @Test
    fun `given non existing artist in local storage and empty biography from service should not store locally`() {
        val lastFMArticle = LastFMArticle("Artist Name", "", "Url")
        every { otherInfoLocalStorage.getCards("Artist Name") } returns null
        every { otherInfoService.getArticle("Artist Name") } returns lastFMArticle
        val card = Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, "LastFM", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png", false)

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(card, result)
        assertEquals(false, result.isLocallyStored)
        verify(exactly = 0) { otherInfoLocalStorage.insertArtist(card) }
    }

}