package pl.marpiec.socnet.web.authorization

import org.apache.wicket.Component
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy

/**
 * @author Marcin Pieciukiewicz
 */

abstract class Authorize(val role: String) {
  def apply(component: Component): Component = {
    MetaDataRoleAuthorizationStrategy.authorize(component, Component.RENDER, role)
    component
  }
}
