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
    private lateinit var cardContent1TextView: TextView
    private lateinit var cardContent2TextView: TextView
    private lateinit var cardContent3TextView: TextView

    private lateinit var openUrl1Button: Button
    private lateinit var openUrl2Button: Button
    private lateinit var openUrl3Button: Button

    private lateinit var source1ImageView: ImageView
    private lateinit var sourceImage2View: ImageView
    private lateinit var sourceImage3View: ImageView

    private lateinit var source1TextView: TextView
    private lateinit var source2TextView: TextView
    private lateinit var source3TextView: TextView

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
            presenter.cardObservable.subscribe { description ->
                updateUi(description) }

    }
        //Como el observer es una interfaz funcional, podemos definir
        // el único método abstracto que hay en ella con una lamba para
        // implementar el update (la lambda pasa a ser el observador y
        // el update queda reemplazado por updateUI). NOTA: las interfaces funcionales
        // son interfaces con un único método abstracto


    private fun initViewProperties() {
        cardContent1TextView = findViewById(R.id.textPane1)
        cardContent2TextView = findViewById(R.id.textPane2)
        cardContent3TextView = findViewById(R.id.textPane3)

        openUrl1Button = findViewById(R.id.openUrlButton1)
        openUrl2Button = findViewById(R.id.openUrlButton2)
        openUrl3Button = findViewById(R.id.openUrlButton3)

        source1ImageView = findViewById(R.id.imageView1)
        sourceImage2View = findViewById(R.id.imageView2)
        sourceImage3View = findViewById(R.id.imageView3)

        source1TextView = findViewById(R.id.source1)
        source2TextView = findViewById(R.id.source2)
        source3TextView = findViewById(R.id.source3)
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

    private fun updateUi(uiState: CardsUiState) {
        runOnUiThread {
            uiState.cards.getOrNull(0)?.let { updateCard1(it) }
            uiState.cards.getOrNull(1)?.let { updateCard2(it) }
            uiState.cards.getOrNull(2)?.let { updateCard3(it) }
        }
    }

    private fun updateCard1(card: CardUiState) {
        updateOpenUrlButton(openUrl1Button, card.url)
        updateCardLogo(source1ImageView, card.logoUrl)
        updateCardText(cardContent1TextView, card.contentHtml)
        updateSourceText(source1TextView, card.source)
    }

    private fun updateCard2(card: CardUiState) {
        updateOpenUrlButton(openUrl2Button, card.url)
        updateCardLogo(sourceImage2View, card.logoUrl)
        updateCardText(cardContent2TextView, card.contentHtml)
        updateSourceText(source2TextView, card.source)
    }

    private fun updateCard3(card: CardUiState) {
        updateOpenUrlButton(openUrl3Button, card.url)
        updateCardLogo(sourceImage3View, card.logoUrl)
        updateCardText(cardContent3TextView, card.contentHtml)
        updateSourceText(source3TextView, card.source)
    }

    private fun updateOpenUrlButton(openUrlButton: Button, url: String) {
        openUrlButton.setOnClickListener {
            navigateToUrl(url)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateCardLogo(sourceImageView: ImageView, url: String) {
        Picasso.get().load(url).into(sourceImageView)
    }

    private fun updateCardText(cardContentTextView: TextView, infoHtml: String) {
        cardContentTextView.text = Html.fromHtml(infoHtml)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateSourceText(sourceTextView: TextView, source: CardSource) {

        sourceTextView.text = when (source) {
            CardSource.LAST_FM -> "Source: LastFM"
            CardSource.NY_TIMES -> "Source: NewYorkTimes"
            CardSource.WIKIPEDIA -> "Source: Wikipedia"
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}