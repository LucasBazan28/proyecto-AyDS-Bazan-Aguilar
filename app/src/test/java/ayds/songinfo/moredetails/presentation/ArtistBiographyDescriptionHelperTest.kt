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

    @Test
    fun `textToHtml should return HTML formatted text with artist name in bold`() {
        val text = "This is the artist's biography about Artist Name."
        val term = "Artist Name"

        val expectedHtml = """
            <html><div width=400><font face="arial">This is the artist s biography about <b>ARTIST NAME</b>.</font></div></html>
        """.trimIndent()

        val result = descriptionHelper.textToHtml(text, term)

        assertEquals(expectedHtml, result)
    }

    @Test
    fun `textToHtml should replace newlines with HTML line breaks`() {
        val text = "This is the artist's\nbiography."
        val term = "artist"

        val expectedHtml = """
            <html><div width=400><font face="arial">This is the <b>ARTIST</b> s<br>biography.</font></div></html>
        """.trimIndent()

        val result = descriptionHelper.textToHtml(text, term)

        assertEquals(expectedHtml, result)
    }
}