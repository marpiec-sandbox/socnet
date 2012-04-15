package pl.marpiec.cqrs

import pl.marpiec.util.UID
import java.sql.{DriverManager, Connection}

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

trait UidGenerator {


  def nextUid:UID

}