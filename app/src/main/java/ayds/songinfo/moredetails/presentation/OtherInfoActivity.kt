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
import ayds.songinfo.moredetails.injector.OtherInfoInjector
import com.squareup.picasso.Picasso

class OtherInfoActivity : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var source: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

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
        presenter.cardObservable.subscribe { card ->
            updateUi(card)   //como el observer es una interfaz funcional, podemos definir el único método abstracto que hay en ella con una lamba para
                                         //implementar el update (la lambda pasa a ser el observador y el update queda reemplazado por updateUI). NOTA: las interfaces funcionales
                                        //son interfaces con un único método abstracto
        }
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton1)
        lastFMImageView = findViewById(R.id.imageView1)
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
            updateOpenUrlButton(uiState.infoUrl)
            updateCardLogo(uiState.sourceLogoUrl)
            updateArticleText(uiState.infoHtml)
            updateSourceText("Source: "+uiState.source)
        }
    }

    private fun updateOpenUrlButton(url: String) {
        openUrlButton.setOnClickListener {
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
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateArticleText(infoHtml: String) {
        articleTextView.text = Html.fromHtml(infoHtml)
    }

    private fun updateSourceText(articleSource: String) {
        source.text = articleSource
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}