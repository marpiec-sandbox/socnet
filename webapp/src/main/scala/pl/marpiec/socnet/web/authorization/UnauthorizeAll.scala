package pl.marpiec.socnet.web.authorization

import org.apache.wicket.Component
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy

/**
 * @author Marcin Pieciukiewicz
 */

object UnauthorizeAll {
  def apply(component: Component): Component = {
    MetaDataRoleAuthorizationStrategy.unauthorizeAll(component, Component.RENDER)
    component
  }
}
