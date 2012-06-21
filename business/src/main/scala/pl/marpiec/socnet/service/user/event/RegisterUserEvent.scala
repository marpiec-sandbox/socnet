package pl.marpiec.socnet.service.user.event

import pl.marpiec.socnet.model.User
import pl.marpiec.cqrs.{Aggregate, Event}

class RegisterUserEvent(val firstName: String, val lastName: String, val email: String,
                        val passwordHash: String, val passwordSalt: String) extends Event {

  def applyEvent(aggregate: Aggregate) {
    val user = aggregate.asInstanceOf[User]
    user.firstName = firstName
    user.lastName = lastName
    user.displayName = user.fullName
    user.email = email
    user.passwordHash = passwordHash
    user.passwordSalt = passwordSalt
  }

  def entityClass = classOf[User]

}
