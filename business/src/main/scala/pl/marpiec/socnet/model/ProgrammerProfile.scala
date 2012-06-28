package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class ProgrammerProfile extends Aggregate(null, 0) {

  var userId: UID = _

  // mapa id technologii => poziom znajomosci
  // 0 - brak znajomo?ci, 1 - s?ysza?em, 2 - podstawowa znajomo??, 3 - pracowa?em z technologi?
  // 4 - d?u?sza znajomo??, 5 - ekspert
  var technologyKnowledge: Map[String, Int] = Map[String, Int]() //
  var technologyLearning: Set[String] = Set[String]()
  var technologyWorking: Set[String] = Set[String]()

}
