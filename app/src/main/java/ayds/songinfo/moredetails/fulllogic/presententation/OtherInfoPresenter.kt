package ayds.songinfo.moredetails.fulllogic.presententation

import ayds.songinfo.moredetails.fulllogic.data.local.DatabaseRepository
import ayds.songinfo.moredetails.fulllogic.data.UserRepository
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView
import android.content.Intent
import android.database.Observable
import ayds.songinfo.moredetails.fulllogic.data.external.ServiceRepository
import javax.security.auth.Subject
import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiography

private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")

interface OtherInfoPresenter{
    val artistBiographyObservable: Subject<ArtistBiography>() // Cambiar Subject abajo.
}
class Presenter(private val intent: Intent){
    private lateinit var repository: UserRepository //Esto se debe inyectar (Venir como parámetro)
    private var view = OtherInfoView()

    val artistBiographySubject = Subject<ArtistBiography>()
    val artistBiographyObservable : Observable<ArtistBiography> = artistBiographySubject

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
                (repository as DatabaseRepository).insertArtistIntoDB(artistBiography)
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