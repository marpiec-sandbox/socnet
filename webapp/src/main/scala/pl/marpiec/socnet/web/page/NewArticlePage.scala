package pl.marpiec.socnet.web.page

import newArticlePage.NewArticleFormModel
import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{TextArea, Button, Form}
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.di.Factory
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.{SocnetRoles, SocnetSession}

/**
 * @author Marcin Pieciukiewicz
 */

class NewArticlePage extends SecureWebPage(SocnetRoles.ARTICLE_AUTHOR) {

  setStatelessHint(false)

  add(new Form[NewArticleFormModel]("newArticleForm") {

    private val articleCommand = Factory.articleCommand

    private val warningMessage: Model[String] = new Model[String]("");

    setModel(new CompoundPropertyModel[NewArticleFormModel](new NewArticleFormModel))

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
      val model: NewArticleFormModel = getDefaultModelObject.asInstanceOf[NewArticleFormModel]
      val user: User = getSession.asInstanceOf[SocnetSession].user

      articleCommand.createArticle(user.id, model.content, user.id)
      setResponsePage(classOf[HomePage])
    }
  })
}