package pl.marpiec.socnet.web.wicket

/**
 * @author Marcin Pieciukiewicz
 */

trait SecureFormModel {
  var sessionToken:String = _
  var warningMessage:String = _
}
