package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.usertechnologies.UserTechnologiesPage
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.ProgrammerProfileDatabase
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import pl.marpiec.socnet.web.component.simplecomponent.RatingStarsPanel
import org.apache.wicket.markup.html.basic.Label

/**
 * @author Marcin Pieciukiewicz
 */

class TechnologiesModule(id: String) extends Panel(id) {

  @SpringBean private var programmerProfileDatabase: ProgrammerProfileDatabase = _

  val mostPopularTechnologies = programmerProfileDatabase.getMostPopularTechnologies(5)
  val mostLikedTechnologies = programmerProfileDatabase.getMostLikedTechnologies(5)

  add(AuthorizeUser(new BookmarkablePageLink("technologiesLink", classOf[UserTechnologiesPage])))

  add(new RepeatingView("mostPopularTechnologies") {
    mostPopularTechnologies.foreach(simpleRating => {
      add(new AbstractItem(newChildId()) {
        add(new Label("technologyName", simpleRating.technologyName))
        add(new RatingStarsPanel("rating", simpleRating.averageRating))
      })
    })
  })

  add(new RepeatingView("mostLikedTechnologies") {
    mostLikedTechnologies.foreach(simpleRating => {
      add(new AbstractItem(newChildId()) {
        add(new Label("technologyName", simpleRating.technologyName))
        add(new RatingStarsPanel("rating", simpleRating.averageRating))
      })
    })
  })

}
