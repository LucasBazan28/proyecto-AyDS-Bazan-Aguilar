package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

class OtherInfoWindow : Activity() {
    private var description: TextView? = null
    private lateinit var dataBase: ArticleDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        description = findViewById(R.id.textPane1)
        dataBase = databaseBuilder(this,
            ArticleDatabase::class.java,
            "database-articulos"
        ).build()
        getArtistInfo(intent.getStringExtra("artistName"))
    }

    private fun getArtistInfo(artistName: String?) {


        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        Thread {
            val article = dataBase.ArticleDao().getArticleByArtistName(artistName!!)
            var textoContenido = ""
            if (existsInDataBase(article)) {
                textoContenido = "[*]" + article!!.biography
                setearListener(article.articleUrl)
            } else { //get from service
                val callResponse: Response<String>
                try {
                    callResponse = lastFMAPI.getArtistInfo(artistName).execute()
                    val gsonInfo = Gson().fromJson(callResponse.body(), JsonObject::class.java)
                    val artist = gsonInfo["artist"].getAsJsonObject()
                    val biografia = artist["bio"].getAsJsonObject()
                    val content = biografia["content"]
                    val artistUrl = artist["url"].asString
                    if (content == null) {
                        textoContenido = "No Results"
                    } else {
                        textoContenido = content.asString.replace("\\n", "\n")
                        textoContenido = textToHtml(textoContenido, artistName)

                        Thread {
                            dataBase!!.ArticleDao().insertArticle(
                                ArticleEntity(
                                    artistName, textoContenido, artistUrl
                                )
                            )
                        }.start()
                    }
                    setearListener(artistUrl)
                } catch (exception: IOException) {
                    Log.e("IOException", "Error $exception")
                    exception.printStackTrace()
                }
            }
            setearImagenYContenido(textoContenido)
        }.start()
    }

    private fun setearImagenYContenido(textoContenido: String) {
        val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
            description!!.text = Html.fromHtml(textoContenido)
        }
    }

    private fun setearListener(url: String) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun existsInDataBase(article: ArticleEntity?) = article != null

    private fun textToHtml(textoContenido: String, artistName: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = textoContenido
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$artistName".toRegex(),
                    "<b>" + artistName!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }


}
