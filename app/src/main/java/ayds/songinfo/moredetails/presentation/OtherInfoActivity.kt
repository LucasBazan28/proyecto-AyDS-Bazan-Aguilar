package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import com.squareup.picasso.Picasso

class OtherInfoActivity : Activity() {
    private lateinit var lastFmArticleTextView: TextView
    private lateinit var nyTimesArticleTextView: TextView
    private lateinit var wikiArticleTextView: TextView

    private lateinit var source: TextView
    private lateinit var sourceNYT: TextView

    private lateinit var openLastFmUrlButton: Button
    private lateinit var openNYTUrlButton: Button
    private lateinit var openWikiUrlButton: Button

    private lateinit var lastFMImageView: ImageView
    private lateinit var nyTimesImageView: ImageView
    private lateinit var wikipediaImageView: ImageView

    private lateinit var presenter: OtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initPresenter()

        observePresenter()
        getArtistInfoAsync()
    }

    private fun initPresenter() {
        OtherInfoInjector.initGraph(this)
        presenter = OtherInfoInjector.presenter
    }

    private fun observePresenter() {
        // Suscribir cada cardObservable individualmente
        presenter.cardObservable.forEach { observable ->
            observable.subscribe { uiState ->
                updateUi(uiState)
            }
        }
        //Como el observer es una interfaz funcional, podemos definir
        // el único método abstracto que hay en ella con una lamba para
        // implementar el update (la lambda pasa a ser el observador y
        // el update queda reemplazado por updateUI). NOTA: las interfaces funcionales
        // son interfaces con un único método abstracto
    }

    private fun initViewProperties() {
        lastFmArticleTextView = findViewById(R.id.textPane1)
        nyTimesArticleTextView = findViewById(R.id.textPane2)
        wikiArticleTextView = findViewById(R.id.textPane3)

        openLastFmUrlButton = findViewById(R.id.openUrlButton1)
        openNYTUrlButton = findViewById(R.id.openUrlButton2)
        openWikiUrlButton = findViewById(R.id.openUrlButton3)

        lastFMImageView = findViewById(R.id.imageView1)
        nyTimesImageView = findViewById(R.id.imageView2)
        wikipediaImageView = findViewById(R.id.imageView3)

        source = findViewById(R.id.source1)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistName = getArtistName()
        presenter.getArtistInfo(artistName)
    }

    private fun updateUi(uiState: CardUiState) {
        runOnUiThread {
            updateOpenUrlButton(uiState.url)
            //updateCardLogo(uiState.sourceLogoUrl)
            updateArticleText(uiState.source, uiState.contentHtml)
            updateSourceText("Source: "+uiState.source)
        }
    }

    private fun updateOpenUrlButton(url: String) {
        openLastFmUrlButton.setOnClickListener {
            navigateToUrl(url)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateCardLogo(url: String) {
        Picasso.get().load(url).into(lastFMImageView)
        //TODO Tambien para las otras dos
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateArticleText(source: CardSource, infoHtml: String) {
        val textView = when (source) {
            CardSource.LAST_FM -> lastFmArticleTextView
            CardSource.NY_TIMES -> nyTimesArticleTextView
            CardSource.WIKIPEDIA -> wikiArticleTextView
        }
        textView.text = Html.fromHtml(infoHtml)
    }

    private fun updateSourceText(articleSource: String) {
        source.text = articleSource
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}