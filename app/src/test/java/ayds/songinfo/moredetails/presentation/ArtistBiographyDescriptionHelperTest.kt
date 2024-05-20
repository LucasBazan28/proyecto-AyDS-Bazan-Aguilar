package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiography
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistBiographyDescriptionHelperImplTest {

    private val descriptionHelper = ArtistBiographyDescriptionHelperImpl()

    @Test
    fun `getDescription should return formatted HTML description with prefix for locally stored artist`() {
        val artistBiography = ArtistBiography("Artist Name", "This is the artist's biography.", "url", true)

        val expectedDescription = """
            <html><div width=400><font face="arial">[*]This is the artist s biography.</font></div></html>
        """.trimIndent()

        val result = descriptionHelper.getDescription(artistBiography)

        assertEquals(expectedDescription, result)
    }

    @Test
    fun `getDescription should return formatted HTML description without prefix for non-locally stored artist`() {
        val artistBiography = ArtistBiography("Artist Name", "This is the artist's biography.", "url", false)

        val expectedDescription = """
            <html><div width=400><font face="arial">This is the artist s biography.</font></div></html>
        """.trimIndent()

        val result = descriptionHelper.getDescription(artistBiography)

        assertEquals(expectedDescription, result)
    }

}