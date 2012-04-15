package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

class ChangeEmailEvent(val email: String) extends CqrsEvent {

  def applyEvent(entity: CqrsEntity) {
    val user = entity.asInstanceOf[User]
    user.email = email
  }

  def entityClass = classOf[User]
}
