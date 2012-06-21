package pl.marpiec.socnet.web.page

import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import pl.marpiec.socnet.service.article.ArticleCommand
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.markup.html.form.{TextArea, Button}
import pl.marpiec.socnet.web.wicket.SimpleStatelessForm

/**
 * @author Marcin Pieciukiewicz
 */

class NewArticlePage extends SecureWebPage(SocnetRoles.ARTICLE_AUTHOR) {

  @SpringBean
  private var articleCommand: ArticleCommand = _

  add(new SimpleStatelessForm("newArticleForm") {
    var content: String = _
    private val warningMessage: Model[String] = new Model[String]("");


    add(new Label("warningMessage", warningMessage))
    add(new TextArea[String]("content").setRequired(true))
    add(new Button("saveButton"))

    add(new Button("cancelButton") {

      setDefaultFormProcessing(false)

      override def onSubmit() {
        setResponsePage(classOf[HomePage])
      }
    })


    override def onSubmit() {
      val user: User = session.user

      articleCommand.createArticle(user.id, content, user.id)
      setResponsePage(classOf[HomePage])
    }
  })
}
