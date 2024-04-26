package ayds.songinfo.moredetails.fulllogic.Presenter

import ayds.songinfo.moredetails.fulllogic.Model.DatabaseRepository
import ayds.songinfo.moredetails.fulllogic.Model.userRepository
import ayds.songinfo.moredetails.fulllogic.OtherInfoWindow
import ayds.songinfo.moredetails.fulllogic.View.OtherInfoView
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)
class Presenter{
    private lateinit var repository: userRepository
    private var view = OtherInfoView()

    public fun updateArtistInfo() {
        Thread {
            getArtistInfo()
        }.start()
    }

    /*private fun getArtistInfo() {
        repository = DatabaseRepository()
        val artistName = getArtistName()
        val artistBiography = getArtistInfoFromRepository()
        view.updateUi(artistBiography)
    }*/
    private fun getArtistName() =
        intent.getStringExtra(OtherInfoWindow.ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

}