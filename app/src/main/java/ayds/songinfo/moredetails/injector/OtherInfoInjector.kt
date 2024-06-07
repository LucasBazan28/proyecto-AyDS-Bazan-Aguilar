package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.external.OtherInfoBrokerImpl
import ayds.songinfo.moredetails.data.external.proxy.LastFMProxy
import ayds.songinfo.moredetails.data.external.proxy.NYTimesProxy
import ayds.songinfo.moredetails.data.external.proxy.WikipediaProxy
import ayds.songinfo.moredetails.data.local.CardDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.presentation.CardDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl


private const val ARTICLE_BD_NAME = "database-article"

object OtherInfoInjector {

    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {

        val cardDatabase =
            Room.databaseBuilder(context, CardDatabase::class.java, ARTICLE_BD_NAME).build()

        LastFMInjector.initService()

        val lastFmService = LastFMInjector.lastFmService
        val nyTimesService = NYTimesInjector.nyTimesService
        val wikipediaTrackService = WikipediaInjector.wikipediaTrackService

        val lastFMProxy = LastFMProxy(lastFmService)
        val nyTimesProxy = NYTimesProxy(nyTimesService)
        val wikipediaProxy = WikipediaProxy(wikipediaTrackService)

        val broker = OtherInfoBrokerImpl(listOf(lastFMProxy, nyTimesProxy, wikipediaProxy))

        val articleLocalStorage = OtherInfoLocalStorageImpl(cardDatabase)

        val repository = OtherInfoRepositoryImpl(articleLocalStorage, broker)

        val artistBiographyDescriptionHelper = CardDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }
}