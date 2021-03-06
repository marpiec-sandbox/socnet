package pl.marpiec.socnet.model.article

import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleComment(var content: String, var creationTime: LocalDateTime, var authorUserId: UID)