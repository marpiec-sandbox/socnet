package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.BeanUtil

class User extends Aggregate(null, 0) {
  var firstName: String = _
  var lastName: String = _
  var displayName: String = _
  var passwordHash: String = _
  var passwordSalt: String = _
  var email: String = _

  def fullName = firstName + " " + lastName

  def copy: Aggregate = {
    BeanUtil.copyProperties(new User, this)
  }
}
