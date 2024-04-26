package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.songinfo.moredetails.fulllogic.model.DatabaseRepository
import ayds.songinfo.moredetails.fulllogic.model.UserRepository
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView
import android.content.Intent
import ayds.songinfo.moredetails.fulllogic.model.ServiceRepository

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)
private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")
class Presenter(private val intent: Intent){
    private lateinit var repository: UserRepository
    private var view = OtherInfoView()

    public fun updateArtistInfo() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        repository = DatabaseRepository(view)
        val artistName = getArtistName()
        var artistBiography = repository.getArtistInfoFromRepository(artistName)
        if (artistBiography != null) {
            artistBiography = artistBiography.markItAsLocal()
        }
        else{
            repository = ServiceRepository(view)
            artistBiography = repository.getArtistInfoFromRepository(artistName)
            if (artistBiography!!.biography.isNotEmpty()) {
                (repository as ServiceRepository).insertArtistIntoDB(artistBiography)
            }
        }
        view.updateUi(artistBiography)
    }
    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}