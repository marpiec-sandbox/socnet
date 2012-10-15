package pl.marpiec.socnet.model.booksuggestion

import org.joda.time.LocalDateTime
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

object BookSuggestionResponse {
  val ACCEPTED = "accepted"
  val DECLINED = "declined"
  val ALREADY_EXISTED = "existed"
}

class BookSuggestionResponse {

  var bookIdOption: Option[UID] = None
  var time:LocalDateTime = _
  var decision: String = _
  var commentOption: Option[String] = None
  var userHasSeenResponse = false


  def alreadyExisted:Boolean = decision == BookSuggestionResponse.ALREADY_EXISTED
  def declined:Boolean = decision == BookSuggestionResponse.DECLINED
  def accepted:Boolean = decision == BookSuggestionResponse.ACCEPTED
}
