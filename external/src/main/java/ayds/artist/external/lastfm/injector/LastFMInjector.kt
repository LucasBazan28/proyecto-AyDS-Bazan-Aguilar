package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastFMToArtistBiographyResolverImpl
import ayds.artist.external.lastfm.data.OtherInfoService
import ayds.artist.external.lastfm.data.OtherInfoServiceImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
object LastFMInjector {

    lateinit var service: OtherInfoService

    fun initService() {

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        val lastFMToArtistBiographyResolver = LastFMToArtistBiographyResolverImpl()
        service = OtherInfoServiceImpl(lastFMAPI, lastFMToArtistBiographyResolver)
    }
}