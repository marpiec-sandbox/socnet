package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}
import pl.marpiec.util.UID

class ChangeEmailEvent(entityId:UID, expectedVersion:Int, val email: String)
      extends CqrsEvent(entityId, expectedVersion) {

  def applyEvent(entity: CqrsEntity) {
    val user = entity.asInstanceOf[User]
    user.email = email
  }

  def entityClass = classOf[User]
}
