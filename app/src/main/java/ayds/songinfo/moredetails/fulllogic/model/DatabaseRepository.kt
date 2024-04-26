package ayds.songinfo.moredetails.fulllogic.model

import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.fulllogic.presenter.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView

class DatabaseRepository(val view: OtherInfoView) : UserRepository {
    override var articleDatabase: ArticleDatabase = databaseBuilder(view, ArticleDatabase::class.java,
        UserRepository.ARTICLE_BD_NAME
    ).build()
    override fun getArtistInfoFromRepository(artistName: String): ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }
}
