package pl.marpiec.socnet.web.application

import pl.marpiec.socnet.web.page.newArticle.NewArticlePage
import pl.marpiec.socnet.web.page.{SignOutPage, RegisterPage}
import pl.marpiec.socnet.web.page.article.ArticlePage
import pl.marpiec.socnet.web.page.userProfile.EditUserProfilePage

/**
 * @author Marcin Pieciukiewicz
 */

object SocnetBookmakablePages {

   def apply(application:SocnetApplication) {
     application.mountPage("register", classOf[RegisterPage])
     application.mountPage("signout", classOf[SignOutPage])
     application.mountPage("new-article", classOf[NewArticlePage])
     application.mountPage("article", classOf[ArticlePage])
     application.mountPage("edit-profile", classOf[EditUserProfilePage])
   }
}
