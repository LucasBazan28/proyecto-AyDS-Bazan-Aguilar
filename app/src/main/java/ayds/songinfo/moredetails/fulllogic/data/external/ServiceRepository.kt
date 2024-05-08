package ayds.songinfo.moredetails.fulllogic.data.external

import androidx.room.Room
//import ayds.songinfo.moredetails.fulllogic.data.LASTFM_BASE_URL
import ayds.songinfo.moredetails.fulllogic.data.UserRepository
import ayds.songinfo.moredetails.fulllogic.data.local.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.presententation.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

class ServiceRepository(val view: OtherInfoView): UserRepository {
    var LastFMToArtistBiographyResolver = LastFMToArtistBiographyResolver()

    override var articleDatabase: ArticleDatabase = Room.databaseBuilder(
        view,
        ArticleDatabase::class.java,
        UserRepository.ARTICLE_BD_NAME
    ).build()
    private val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
    val retrofit = Retrofit.Builder()
        .baseUrl(LASTFM_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private var lastFMAPI = retrofit.create(LastFMAPI::class.java)

    override fun getArtistInfoFromRepository(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = LastFMToArtistBiographyResolver.map(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

}