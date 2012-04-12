package pl.marpiec.socnet.model.article

import org.joda.time.LocalDateTime
import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleComment(var content: String, var creationTime: LocalDateTime, var authorUserId: UUID)