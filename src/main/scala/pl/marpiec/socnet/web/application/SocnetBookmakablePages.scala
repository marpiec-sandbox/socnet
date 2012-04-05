package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.web.page.newArticle.NewArticlePage
import pl.marpiec.socnet.web.page.{SignOutPage, RegisterPage}

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetBookmakablePages {

   def apply(application:SocnetApplication) {
     application.mountPage("register", classOf[RegisterPage])
     application.mountPage("signout", classOf[SignOutPage])
     application.mountPage("newArticle", classOf[NewArticlePage])
   }
}
