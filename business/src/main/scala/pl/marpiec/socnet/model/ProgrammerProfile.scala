package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}
import programmerprofile.KnownTechnology

/**
 * @author Marcin Pieciukiewicz
 */

class ProgrammerProfile extends Aggregate(null, 0) {

  var userId: UID = _

  var technologyKnowledge: Map[String, KnownTechnology] = Map[String, KnownTechnology]() //

  def copy: Aggregate = {
    BeanUtil.copyProperties(new ProgrammerProfile, this)
  }
}
