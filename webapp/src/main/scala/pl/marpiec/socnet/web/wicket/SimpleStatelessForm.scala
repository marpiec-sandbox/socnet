package pl.marpiec.socnet.web.wicket

import org.apache.wicket.markup.html.form.StatelessForm
import org.apache.wicket.model.CompoundPropertyModel

/**
 * @author Marcin Pieciukiewicz
 */

class SimpleStatelessForm(id: String) extends StatelessForm[StatelessForm[_]](id) {

  setModel(new CompoundPropertyModel[StatelessForm[_]](this.asInstanceOf[StatelessForm[_]]))

}
