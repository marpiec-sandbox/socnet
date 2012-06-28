package pl.marpiec.socnet.web.page

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label


import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import pl.marpiec.util.UID
import pl.marpiec.socnet.web.page.template.SimpleTemplatePage
import pl.marpiec.socnet.readdatabase.{ArticleDatabase, UserDatabase}
import org.apache.wicket.spring.injection.annot.SpringBean

/**
 * @author Marcin Pieciukiewicz
 */

class ArticlePage(parameters: PageParameters) extends SimpleTemplatePage {

  @SpringBean private var articleDatabase: ArticleDatabase = _
  @SpringBean private var userDatabase: UserDatabase = _


  val articleId = UID.parseOrZero(parameters.get(ArticlePage.ARTICLE_ID_PARAM).toString)

  articleDatabase.getArticleById(articleId) match {
    case Some(article) => {

      val authorOption = userDatabase.getUserById(article.authorUserId)

      var authorName = "autor nieznany"
      if (authorOption.isDefined) {
        authorName = authorOption.get.fullName
      }

      add(new Label("articleId", articleId.toString))
      add(new Label("articleContent", article.content))
      add(new Label("articleDate", article.creationTime.toString))
      add(new Label("authorName", authorName))

      setSubTitle(article.authorUserId.toString)

    }
    case None => {
      throw new AbortWithHttpErrorCodeException(404);
    }
  }

}


object ArticlePage {
  val ARTICLE_ID_PARAM = "articleId"
}