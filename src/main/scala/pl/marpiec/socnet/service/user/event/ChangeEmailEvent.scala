package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

class ChangeEmailEvent(override val entityId:Int, override val expectedVersion:Int, val email: String)
      extends CqrsEvent(entityId, expectedVersion, classOf[User]) {

  def applyEvent(entity: CqrsEntity) {
    val user = entity.asInstanceOf[User]
    user.email = email
  }
}
