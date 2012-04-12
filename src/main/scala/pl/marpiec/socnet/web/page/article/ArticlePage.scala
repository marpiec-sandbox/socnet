package pl.marpiec.socnet.web.page.article

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label


import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.model.{User, Article}
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class ArticlePage (parameters: PageParameters) extends WebPage {



  private val articleDatabase = Factory.articleDatabase
  private val userDatabase = Factory.userDatabase


  val articleId = UUID.fromString(parameters.get(ArticlePage.ARTICLE_ID_PARAM).toString)

  articleDatabase.getArticleById(articleId) match {
    case Some(article) => {

      val authorOption = userDatabase.getUserById(article.authorUserId)

      var authorName = "autor nieznany"
      if(authorOption.isDefined) {
        authorName = authorOption.get.name
      }

      add(new Label("articleId", articleId.toString))
      add(new Label("articleContent", article.content))
      add(new Label("articleDate", article.creationTime.toString))
      add(new Label("authorName", authorName))

    }
    case None => {
      throw new AbortWithHttpErrorCodeException(404);
    }
  }

}


object ArticlePage {
  val ARTICLE_ID_PARAM = "k"
}