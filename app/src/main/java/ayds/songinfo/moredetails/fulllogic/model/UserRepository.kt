package ayds.songinfo.moredetails.fulllogic.model

import ayds.songinfo.moredetails.fulllogic.presenter.ArtistBiography


interface UserRepository {
    var articleDatabase: ArticleDatabase
    public fun getArtistInfoFromRepository(artistName: String): ArtistBiography?
    companion object{
        const val ARTICLE_BD_NAME = "database-article"
    }
}