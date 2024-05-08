package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.data.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.presententation.ArtistBiography


interface UserRepository {
    var articleDatabase: ArticleDatabase
    public fun getArtistInfoFromRepository(artistName: String): ArtistBiography?
    companion object{
        const val ARTICLE_BD_NAME = "database-article"
    }
}