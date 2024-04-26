package ayds.songinfo.moredetails.fulllogic.Model

import ayds.songinfo.moredetails.fulllogic.Presenter.ArtistBiography


interface UserRepository {
    var articleDatabase: ArticleDatabase
    public fun getArtistInfoFromRepository(artistName: String): ArtistBiography?
    companion object{
        const val ARTICLE_BD_NAME = "database-article"
    }
}