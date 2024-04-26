package ayds.songinfo.moredetails.fulllogic.Model

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
import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.Model.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.Model.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.Presenter.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.View.OtherInfoView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

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
