package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{Aggregate, Event}

class ChangeSummaryEvent(val firstName: String, val lastName: String, val summary: String) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val user = aggregate.asInstanceOf[User]
    user.firstName = firstName
    user.lastName = lastName
    user.summary = summary
  }

  def entityClass = classOf[User]
}
