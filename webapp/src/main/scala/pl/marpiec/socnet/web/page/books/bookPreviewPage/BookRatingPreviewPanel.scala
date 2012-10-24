package pl.marpiec.socnet.web.page.books.bookPreviewPage

import org.apache.wicket.markup.html.panel.Panel
import pl.marpiec.socnet.web.component.simplecomponent.RatingStarsPanel
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class BookRatingPreviewPanel(id: String, averageRating:Double, votesCount:Int) extends Panel(id) {

  setOutputMarkupId(true)
  
  add(new RatingStarsPanel("rating", averageRating))
  add(new Label("votesCount", votesCount.toString))
  add(new Label("votesLabel", labelBasedOnVotesCount(votesCount)))

  def labelBasedOnVotesCount(votesCount: Int): String = {
    if (votesCount == 1) {
      "głos"
    } else if (votesCount >= 2 && votesCount <= 4) {
      "głosy"
    } else {
      "głosów"
    }
  }

}
