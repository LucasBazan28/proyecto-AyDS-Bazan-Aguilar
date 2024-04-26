package ayds.songinfo.moredetails.fulllogic.Presenter

import ayds.songinfo.moredetails.fulllogic.Model.DatabaseRepository
import ayds.songinfo.moredetails.fulllogic.Model.UserRepository
import ayds.songinfo.moredetails.fulllogic.View.OtherInfoView
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.Model.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.Model.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.Model.ServiceRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)
private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")
class Presenter(private val intent: Intent){
    private lateinit var repository: UserRepository
    private var view = OtherInfoView()

    public fun updateArtistInfo() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        repository = DatabaseRepository(view)
        val artistName = getArtistName()
        var artistBiography = repository.getArtistInfoFromRepository(artistName)
        if (artistBiography != null) {
            artistBiography = artistBiography.markItAsLocal()
        }
        else{
            repository = ServiceRepository(view)
            artistBiography = repository.getArtistInfoFromRepository(artistName)
            if (artistBiography!!.biography.isNotEmpty()) {
                (repository as ServiceRepository).insertArtistIntoDB(artistBiography)
            }
        }
        view.updateUi(artistBiography)
    }
    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}