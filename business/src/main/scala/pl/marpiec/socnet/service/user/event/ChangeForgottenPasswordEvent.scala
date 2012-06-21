package pl.marpiec.socnet.service.user.event

import pl.marpiec.cqrs.{Aggregate, Event}
import pl.marpiec.socnet.model.User


/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class ChangeForgottenPasswordEvent extends Event {

  var passwordHash: String = _
  var passwordSalt: String = _

  def applyEvent(aggregate: Aggregate) {
    val user = aggregate.asInstanceOf[User]
    user.passwordHash = this.passwordHash
    user.passwordSalt = this.passwordSalt
  }

  def entityClass = classOf[User]
}
