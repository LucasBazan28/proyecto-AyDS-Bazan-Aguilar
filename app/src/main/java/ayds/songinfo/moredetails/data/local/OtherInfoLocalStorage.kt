package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card

interface OtherInfoLocalStorage {
    fun getArticle(artistName: String): Card?
    fun insertArtist(card: Card)
}

internal class OtherInfoLocalStorageImpl(
    private val articleDatabase: ArticleDatabase,
) : OtherInfoLocalStorage {

    override fun getArticle(artistName: String): Card? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            Card(artistName, artistEntity.biography, artistEntity.articleUrl, "LastFM", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png")
        }
    }

    override fun insertArtist(card: Card) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                card.artistName, card.description, card.infoUrl
            )
        )
    }
}
