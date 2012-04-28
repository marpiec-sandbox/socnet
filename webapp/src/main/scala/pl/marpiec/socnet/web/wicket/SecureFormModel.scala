package pl.marpiec.socnet.web.wicket

import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

trait SecureFormModel {
  var sessionToken:String = _
  var warningMessage:String = _

  def clear() {
    val sessTok = sessionToken
    BeanUtil.clearProperties(this)
    sessionToken = sessTok
  }
}
