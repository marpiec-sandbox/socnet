package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate

class User extends Aggregate(null, 0) {
  var firstName: String = _
  var lastName: String = _
  var displayName: String = _
  var passwordHash: String = _
  var passwordSalt: String = _
  var email: String = _

  def fullName = firstName + " " + lastName
  
  def copy:Aggregate = {
    val user = new User
    user.id = this.id
    user.version = this.version
    user.firstName = this.firstName
    user.lastName = this.lastName
    user.displayName = this.displayName
    user.passwordHash = this.passwordHash
    user.passwordSalt = this.passwordSalt
    user.email = this.email
    user
  }
}
