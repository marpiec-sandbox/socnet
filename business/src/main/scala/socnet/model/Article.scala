package pl.marpiec.socnet.model

import article.ArticleComment
import org.joda.time.LocalDateTime
import pl.marpiec.cqrs.Aggregate
import collection.mutable.ListBuffer
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class Article extends Aggregate(null, 0) {

  var authorUserId:UID = _
  var content:String = _
  var comments = new ListBuffer[ArticleComment]
  var creationTime:LocalDateTime = _

  def copy:Aggregate = {
    BeanUtil.copyProperties(new Article, this)
  }
}
