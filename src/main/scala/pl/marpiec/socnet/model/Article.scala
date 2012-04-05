package pl.marpiec.socnet.model

import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.CqrsEntity
import collection.mutable.ListBuffer

/**
 * @author Marcin Pieciukiewicz
 */

class Article extends CqrsEntity(0, 0) {
  var content:String = _
  var comments = new ListBuffer[ArticleComment]
  var creationTime:LocalDateTime = _
  var authorUserId:Int = _

  def createCopy:Article = {
    val article = new Article
    article.id = this.id
    article.version = this.version
    article.content = this.content
    article.creationTime = this.creationTime
    article.authorUserId = this.authorUserId
    article.comments = this.comments.clone()
    //TODO zrobic lepsze kopiowanie komentarzy
    article
  }
}
