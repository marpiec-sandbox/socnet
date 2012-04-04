package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{CqrsEntity, CqrsEvent}

class RegisterUserEvent(override val entityId:Int, override val expectedVersion:Int, val name: String, val email: String, val password: String)
  extends CqrsEvent(entityId, expectedVersion, classOf[User]) {

  def applyEvent(entity: CqrsEntity) {
    val user = entity.asInstanceOf[User]
    user.name = name
    user.email = email
    user.password = password
  }

}
