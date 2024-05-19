package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
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
    fun `given artist bio stored in data base`() {
        val artistName = "artistName"
        val artistBio: ArtistBiography = mockk()
        every { otherInfoLocalStorage.getArticle(artistName) } returns artistBio

        val result = otherInfoRepository.getArticle(artistName)

        assertEquals(artistBio, result)

        verify {artistBio.markItAsLocal()}
    }

    @Test
    fun `given artist bio not empty not stored in data base and not empty`() {
        val artistName = "Test Artist"
        val artistBio: ArtistBiography = mockk()
        every { artistBio.biography } returns "Biography content"
        every { otherInfoLocalStorage.getArticle(artistName) } returns null
        every { otherInfoService.getArticle(artistName) } returns artistBio

        val result = otherInfoRepository.getArtistInfo(artistName)

        assertEquals(artistBio, result)

        verify { otherInfoLocalStorage.insertArtist(newArticle) }
    }


}