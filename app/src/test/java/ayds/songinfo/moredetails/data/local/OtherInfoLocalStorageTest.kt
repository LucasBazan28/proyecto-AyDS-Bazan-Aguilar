package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OtherInfoLocalStorageTest {
    private val articleDao: ArticleDao = mockk(relaxUnitFun = true)
    private val articleDatabase: ArticleDatabase = mockk {
        every { ArticleDao() } returns articleDao
    }

    private val localStorage: OtherInfoLocalStorage = OtherInfoLocalStorageImpl(articleDatabase)

    @Test
    fun `given existing artist in database should return artist biography`() {
        val artistEntity = ArticleEntity("Artist Name", "Biography", "http://url.com")
        every { articleDao.getArticleByArtistName("Artist Name") } returns artistEntity

        val result = localStorage.getArticle("Artist Name")

        assertEquals(ArtistBiography("Artist Name", "Biography", "http://url.com"), result)
    }

    @Test
    fun `given non-existing artist in database should return null`() {
        every { articleDao.getArticleByArtistName("Artist Name") } returns null

        val result = localStorage.getArticle("Artist Name")

        assertNull(result)
    }

    @Test
    fun `should insert artist biography into database`() {
        val artistBiography = ArtistBiography("Artist Name", "Biography", "http://url.com")

        localStorage.insertArtist(artistBiography)

        verify {
            articleDao.insertArticle(
                ArticleEntity("Artist Name", "Biography", "http://url.com")
            )
        }
    }
}