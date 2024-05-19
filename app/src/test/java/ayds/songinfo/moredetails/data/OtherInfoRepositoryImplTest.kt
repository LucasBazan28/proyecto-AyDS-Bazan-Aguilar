package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OtherInfoRepositoryImplTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)
    private val otherInfoService: OtherInfoService = mockk(relaxUnitFun = true) //ver si hay q poner relax solo


    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)


    @Test
    fun `given existing artist in local storage should return local artist biography`() {
        val artistBiography = ArtistBiography("Artist Name", "Biography", "Url", isLocallyStored = false)
        every { otherInfoLocalStorage.getArticle("Artist Name") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(artistBiography, result)
        assertEquals(true, result.isLocallyStored) //verify {artistBiography.markItAsLocal()}
        //Con verify verificamos que realmente se llame al metodo markItAsLocal(), con el otro solo comparamos

    }

    @Test
    fun `given non existing artist in local storage should fetch from service and store locally`() {
        val artistBiography = ArtistBiography("Artist Name", "Biography", "Url", isLocallyStored = false)
        every { otherInfoLocalStorage.getArticle("Artist Name") } returns null
        every { otherInfoService.getArticle("Artist Name") } returns artistBiography

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(artistBiography, result)
        assertEquals(false, result.isLocallyStored)
        verify { otherInfoLocalStorage.insertArtist(artistBiography) }
    }

    @Test
    fun `given non existing artist in local storage and empty biography from service should not store locally`() {
        val emptyBiography = ArtistBiography("Artist Name", "", "Url", isLocallyStored = false)
        every { otherInfoLocalStorage.getArticle("Artist Name") } returns null
        every { otherInfoService.getArticle("Artist Name") } returns emptyBiography

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(emptyBiography, result)
        assertEquals(false, result.isLocallyStored)
        verify(exactly = 0) { otherInfoLocalStorage.insertArtist(emptyBiography) }
    }
    /*
    @Test
    fun `given service exception should return empty artist biography`() {
        every { otherInfoLocalStorage.getArticle("Artist Name") } returns null
        every { otherInfoService.getArticle("Artist Name") } throws mockk<Exception>()

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(ArtistBiography("Artist Name", "", isLocallyStored = false), result)
    }*/
}