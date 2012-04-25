package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.web.page._

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetBookmakablePages {

  def apply(application: SocnetApplication) {
    application.mountPage("register", classOf[RegisterPage])
    application.mountPage("signout", classOf[SignOutPage])
    application.mountPage("new-article", classOf[NewArticlePage])
    application.mountPage("article", classOf[ArticlePage])
    application.mountPage("edit-profile", classOf[EditUserProfilePage])
  }
}
