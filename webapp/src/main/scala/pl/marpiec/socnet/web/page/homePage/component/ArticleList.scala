package pl.marpiec.socnet.web.page.homePage.component

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.basic.Label
import pl.marpiec.socnet.model.Article
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.page.articlePage.ArticlePage

/**
 * @author Marcin Pieciukiewicz
 */

class ArticleList(id: String, private val articleList: List[Article]) extends Panel(id) {

  add(new Label("title", "Lista artykulow (mamy obecnie " + articleList.length + " artykulow)"));

  val repeating: RepeatingView = new RepeatingView("repeating");
  add(repeating);

  for (article <- articleList) {

    val item: AbstractItem = new AbstractItem(repeating.newChildId());
    repeating.add(item);

    item.add(new Label("content", article.content))
    item.add(new Label("time", article.creationTime.toString))
    item.add(new Label("author", article.authorUserId.toString))
    val parameters: PageParameters = new PageParameters()
    parameters.set(ArticlePage.ARTICLE_ID_PARAM, article.id)

    item.add(new BookmarkablePageLink("articleLink", classOf[ArticlePage], parameters))
  }

}
