package pl.marpiec.cqrs

/**
 * @author Marcin Pieciukiewicz
 */

class SimpleTestEntity extends Aggregate(null, 0) {
  var name: String = _
  var password: String = _
  var email: String = _

  def copy:Aggregate = {
    val entity = new SimpleTestEntity
    entity.id = this.id
    entity.version = this.version
    entity.name = this.name
    entity.password = this.password
    entity.email = this.email
    entity
  }
}
