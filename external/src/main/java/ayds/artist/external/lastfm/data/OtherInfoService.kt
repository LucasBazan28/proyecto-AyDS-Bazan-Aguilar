package ayds.artist.external.lastfm.data

import java.io.IOException

interface OtherInfoService {
    fun getArticle(artistName: String): LastFMArticle
}
internal class OtherInfoServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToArtistBiographyResolver: LastFMToArtistBiographyResolver
) : OtherInfoService {

    override fun getArticle(artistName: String): LastFMArticle {

        var lastFMArticle = LastFMArticle(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            lastFMArticle = lastFMToArtistBiographyResolver.map(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return lastFMArticle
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

}