package ayds.songinfo.moredetails.data


import ayds.songinfo.moredetails.data.external.OtherInfoBroker
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OtherInfoRepositoryImplTest {

    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)
    private val otherInfoService: OtherInfoBroker = mockk()

    private val otherInfoRepository: OtherInfoRepository =
        OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)


    @Test
    fun `given existing artist in local storage should return local artist biography`() {
        val card = Card("Artist Name", "Biography", "Url", "Source", CardSource.LAST_FM, isLocallyStored = false)
        every { otherInfoLocalStorage.getCard("Artist Name") } returns listOf(card)

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(listOf(card) , result)
        assertEquals(true, result[0].isLocallyStored) //verify {artistBiography.markItAsLocal()}
        //Con verify verificamos que realmente se llame al metodo markItAsLocal(), con el otro solo comparamos

    }

    @Test
    fun `given non existing artist in local storage should fetch from service and store locally`() {
        val card = Card("Artist Name", "Biography", "Url", "Source", CardSource.LAST_FM, isLocallyStored = false)
        every { otherInfoLocalStorage.getCard("Artist Name") } returns emptyList()
        every { otherInfoService.getCards("Artist Name") } returns listOf(card)

        val result = otherInfoRepository.getArtistInfo("Artist Name")

        assertEquals(listOf(card), result)
        assertEquals(false, result[0].isLocallyStored)
        verify { otherInfoLocalStorage.insertCard(card) }
    }



}