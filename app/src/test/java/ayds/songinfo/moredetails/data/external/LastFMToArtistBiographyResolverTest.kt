package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.ArtistBiography
import org.junit.Assert.assertEquals
import org.junit.Test

class LastFMToArtistBiographyResolverImplTest {

    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver =
        LastFMToArtistBiographyResolverImpl()

    @Test
    fun `given valid json string should return correct ArtistBiography`() {
        val artistName = "existing-artist"
        val json = """
            {
                "artist": {
                    "bio": {
                        "content": "Artist biography content"
                    },
                    "url": "https://artist.url"
                }
            }
        """.trimIndent()
        val expectedBiography = ArtistBiography(artistName, "Artist biography content", "https://artist.url")

        val result = lastFMToArtistBiographyResolver.map(json, artistName)

        assertEquals(expectedBiography, result)
    }

    @Test
    fun `given invalid json string should return ArtistBiography with no results`() {
        val NO_RESULTS = "No Results"
        val artistName = "non-existing-artist"
        val json = "{}"
        val expectedBiography = ArtistBiography(artistName, NO_RESULTS, "")

        val result = lastFMToArtistBiographyResolver.map(json, artistName)

        assertEquals(expectedBiography, result)
    }
}
