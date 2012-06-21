package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{Aggregate, Event}

class ChangeSummaryEvent(val summary: String) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val user = aggregate.asInstanceOf[User]
    user.summary = summary
  }

  def entityClass = classOf[User]
}
