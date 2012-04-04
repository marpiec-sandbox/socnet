package pl.marpiec.socnet.model

import pl.marpiec.cqrs.CqrsEntity

class User extends CqrsEntity(0, 0) {
  var name: String = _
  var password: String = _
  var email: String = _
  
  def createCopy:User = {
    val user = new User
    user.id = this.id
    user.name = this.name
    user.password = this.password
    user.email = this.email
    user
  }
}
