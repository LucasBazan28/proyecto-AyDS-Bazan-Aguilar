package ayds.songinfo.moredetails.fulllogic.Model

import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.View.OtherInfoView
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.Model.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.Model.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.Model.UserRepository.Companion.ARTICLE_BD_NAME
import ayds.songinfo.moredetails.fulllogic.Presenter.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
class ServiceRepository(val view: OtherInfoView): UserRepository {
    override var articleDatabase: ArticleDatabase = databaseBuilder(view, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    val retrofit = Retrofit.Builder()
        .baseUrl(LASTFM_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private var lastFMAPI = retrofit.create(LastFMAPI::class.java)

    override fun getArtistInfoFromRepository(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()
    public fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }
}
