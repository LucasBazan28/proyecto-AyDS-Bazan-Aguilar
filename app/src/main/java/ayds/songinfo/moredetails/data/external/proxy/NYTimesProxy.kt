package ayds.songinfo.moredetails.data.external.proxy

import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.Card
import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
class NYTimesProxy (
    private val nyTimesService: NYTimesService
){
    public fun getCard(artist: String): Card {
        val nytimesArticle = nyTimesService.getArtistInfo(artist)
        return when(nytimesArticle){
            is NYTimesArticle.NYTimesArticleWithData->{
                Card(
                    artistName = nytimesArticle.name ?: "",
                    text = nytimesArticle.info ?: "",
                   nytimesArticle.url,
                    "NYTimes",
                    NYT_LOGO_URL,
                    false)
            }

            else ->{
                //is(NYTimesArticle.EmptyArtistDataExternal)
                Card("", "", "", "NYTimes", NYT_LOGO_URL, false)
            }
        }
    }
}