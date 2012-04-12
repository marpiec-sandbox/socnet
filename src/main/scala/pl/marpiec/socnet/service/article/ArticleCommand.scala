package pl.marpiec.socnet.service.article

import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

trait ArticleCommand {

  def createArticle(content: String, authorUserId: UUID):UUID

  def addComment(articleId: UUID, articleVersion: Int, commentContent: String, commentAuthorUserId: UUID)
}
