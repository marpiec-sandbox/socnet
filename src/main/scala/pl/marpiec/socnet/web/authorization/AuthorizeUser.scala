package pl.marpiec.socnet.web.authorization

import pl.marpiec.socnet.web.application.SocnetRoles

/**
 * @author Marcin Pieciukiewicz
 */

object AuthorizeUser extends Authorize(SocnetRoles.USER)
