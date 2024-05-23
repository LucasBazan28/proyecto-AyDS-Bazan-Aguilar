package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.local.ArticleDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl


private const val ARTICLE_BD_NAME = "database-article"

object OtherInfoInjector {

    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {

        val articleDatabase =
            Room.databaseBuilder(context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()

        LastFMInjector.initService()
        val otherInfoService = LastFMInjector.service
        val articleLocalStorage = OtherInfoLocalStorageImpl(articleDatabase)

        val repository = OtherInfoRepositoryImpl(articleLocalStorage, otherInfoService)

        val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }
}