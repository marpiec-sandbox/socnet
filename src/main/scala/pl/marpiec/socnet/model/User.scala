package pl.marpiec.socnet.model

import pl.marpiec.cqrs.CqrsEntity

class User extends CqrsEntity(0, 0) {
  var name: String = null
  var password: String = null
  var email: String = null
  
  def createCopy:User = {
    val user = new User
    user.id = this.id
    user.name = this.name
    user.password = this.password
    user.email = this.email
    user
  }
}
