package pl.marpiec.socnet.service.article

import event.{CreateCommentEvent, CreateArticleEvent}
import pl.marpiec.cqrs.{DataStore, EventStore}
import pl.marpiec.socnet.database.UserDatabase

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleCommandImpl(val eventStore:EventStore, val dataStore:DataStore, val userDatabase:UserDatabase) extends ArticleCommand {

  override def createArticle(content: String, authorUserId: Int):Int = {
    val createArticle = new CreateArticleEvent(content, authorUserId)
    eventStore.addEventForNewAggregate(createArticle)
  }

  def addComment(articleId: Int, articleVersion: Int, commentContent: String, commentAuthorUserId: Int) {
    val createComment = new CreateCommentEvent(articleId, articleVersion, commentContent, commentAuthorUserId)
    eventStore.addEvent(createComment)
  }

}
