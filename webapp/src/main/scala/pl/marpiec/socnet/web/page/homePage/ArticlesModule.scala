package pl.marpiec.socnet.web.page.homePage

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import pl.marpiec.socnet.web.page.NewArticlePage
import pl.marpiec.socnet.web.authorization.AuthorizeUser
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.ArticleDatabase

/**
 * @author Marcin Pieciukiewicz
 */

class ArticlesModule(id:String) extends Panel(id) {

  @SpringBean private var articleDatabase: ArticleDatabase = _

  add(AuthorizeUser(new BookmarkablePageLink("newArticleLink", classOf[NewArticlePage])))
  add(AuthorizeUser(new ArticleList("articleList", articleDatabase.getAllArticles)))
}
