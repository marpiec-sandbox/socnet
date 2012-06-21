package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.BeanUtil

class User extends Aggregate(null, 0) {
  var firstName: String = ""
  var lastName: String = ""
  var passwordHash: String = ""
  var passwordSalt: String = ""
  var email: String = ""
  var summary: String = ""
  
  def fullName = firstName + " " + lastName

  def copy: Aggregate = {
    BeanUtil.copyProperties(new User, this)
  }
}
