package pl.marpiec.socnet.web.wicket

import org.apache.wicket.request.mapper.MountedMapper
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder
import org.apache.wicket.request.mapper.info.PageComponentInfo
import org.apache.wicket.request.{IRequestHandler, Url}
import org.apache.wicket.request.handler.ListenerInterfaceRequestHandler

/**
 * Based on discussion on http://apache-wicket.1842946.n4.nabble.com/I-don-t-want-url-page-count-parameter-localhost-8080-context-0-td4481510.html
 * @author Marcin Pieciukiewicz
 */

class NoVersionMountedMapper(path: String, pageClass: Class[_ <: IRequestablePage])
  extends MountedMapper(path, pageClass, new PageParametersEncoder) {

  override def encodePageComponentInfo(url: Url, info: PageComponentInfo) {
    // do nothing so that component info does not get rendered in url
  }

  override def mapHandler(requestHandler: IRequestHandler): Url = {
    if (requestHandler.isInstanceOf[ListenerInterfaceRequestHandler]) {
      null
    } else {
      super.mapHandler(requestHandler)
    }
  }
}
