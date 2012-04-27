package pl.marpiec.socnet.web.page.template

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import pl.marpiec.socnet.web.page.{HomePage, SignOutPage}
import pl.marpiec.socnet.web.application.SocnetSession

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class SimpleTemplatePage extends WebPage {

  val titleLabelModel = new Model[String]("Socnet")

  add(AuthorizeUser(new BookmarkablePageLink("signoutLink", classOf[SignOutPage])))
  add(new BookmarkablePageLink("homeLink", classOf[HomePage]))

  add(new Label("pageTitle", titleLabelModel))

  private val session: SocnetSession = getSession.asInstanceOf[SocnetSession]
  if (session.isSignedIn) {
    add(new Label("userName", session.user.fullName))
  } else {
    add(new Label("userName", ""))
  }


  protected def setSubTitle(title: String) {
    titleLabelModel.setObject("Socnet " + title)
  }
}
