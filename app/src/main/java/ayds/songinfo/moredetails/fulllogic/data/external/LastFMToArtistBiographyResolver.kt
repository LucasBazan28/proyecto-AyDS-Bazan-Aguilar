package ayds.songinfo.moredetails.fulllogic.data.external

import ayds.songinfo.moredetails.fulllogic.presententation.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject

class LastFMToArtistBiographyResolver {
    fun map(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }
}