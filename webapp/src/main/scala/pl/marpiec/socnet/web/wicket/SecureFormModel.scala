package pl.marpiec.socnet.web.wicket

import pl.marpiec.util.BeanUtil

/**
 * @author Marcin Pieciukiewicz
 */

class SecureFormModel {
  var sessionToken: String = _
  var warningMessage: String = _

  def clear() {
    val sessTok = sessionToken
    BeanUtil.clearProperties(this)
    sessionToken = sessTok
  }
}
