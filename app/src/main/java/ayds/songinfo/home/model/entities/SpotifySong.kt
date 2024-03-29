package ayds.songinfo.home.model.entities

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val spotifyUrl: String,
        val imageUrl: String,
        val releaseDatePrecision: String,
        var isLocallyStored: Boolean = false
    ) : Song() {
        fun getPreciseDate(): String{
            var retornar = releaseDate
            val fechaSplit = releaseDate.split("-")
            when(releaseDatePrecision){
                "day" -> retornar = fechaSplit.last() + "/" + fechaSplit[1] + "/" + fechaSplit.first()
                "month" -> retornar = obtenerNombreMes(fechaSplit[1].toInt()) + ", " + fechaSplit.first()
            }
            return retornar
        }
       /* val year: String = releaseDate.split("-").first() */

        private fun obtenerNombreMes(numeroMes: Int): String {
            return when (numeroMes) {
                1 -> "Enero"
                2 -> "Febrero"
                3 -> "Marzo"
                4 -> "Abril"
                5 -> "Mayo"
                6 -> "Junio"
                7 -> "Julio"
                8 -> "Agosto"
                9 -> "Septiembre"
                10 -> "Octubre"
                11 -> "Noviembre"
                12 -> "Diciembre"
                else -> "Mes no válido"
            }
        }
    }

    object EmptySong : Song()
}

