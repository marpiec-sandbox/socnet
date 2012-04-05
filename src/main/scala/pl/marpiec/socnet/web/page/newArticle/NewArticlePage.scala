package pl.marpiec.socnet.web.page.newArticle

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{TextArea, Button, Form}
import pl.marpiec.socnet.web.page.HomePage
import pl.marpiec.socnet.web.application.SocnetSession
import pl.marpiec.socnet.model.User
import pl.marpiec.socnet.di.Factory

/**
 * @author Marcin Pieciukiewicz
 */

class NewArticlePage extends WebPage {
  add(new Form[NewArticlePageModel]("newArticleForm") {

    private val articleCommand = Factory.articleCommand

    private val warningMessage: Model[String] = new Model[String]("");

    setModel(new CompoundPropertyModel[NewArticlePageModel](new NewArticlePageModel))

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
      val model: NewArticlePageModel = getDefaultModelObject.asInstanceOf[NewArticlePageModel]
      val user:User = getSession.asInstanceOf[SocnetSession].user
      
      articleCommand.createArticle(model.content, user.id)
      setResponsePage(classOf[HomePage])
    }
  })
}
