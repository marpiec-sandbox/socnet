package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{Aggregate, Event}

class ChangeEmailEvent(val email: String) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val user = aggregate.asInstanceOf[User]
    user.email = email
  }

  def entityClass = classOf[User]
}
