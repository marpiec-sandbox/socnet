package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */

class UserRoles extends Aggregate(null, 0) {

  var userId: UID = _
  var roles: Set[String] = Set()

  def copy: Aggregate = {
    BeanUtil.copyProperties(new UserRoles, this)
  }
}
