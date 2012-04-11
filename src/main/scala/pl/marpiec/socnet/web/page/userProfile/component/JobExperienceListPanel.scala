package pl.marpiec.socnet.web.page.userProfile.component

import pl.marpiec.socnet.model.{UserProfile, User}
import org.apache.wicket.markup.html.panel.Panel
import collection.mutable.ListBuffer
import pl.marpiec.socnet.model.userprofile.JobExperience
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.list.AbstractItem
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.mapper.parameter.PageParameters
import pl.marpiec.socnet.web.page.article.ArticlePage
import org.apache.wicket.markup.html.link.BookmarkablePageLink

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class JobExperienceListPanel(id: String, val user: User, val jobExperience: ListBuffer[JobExperience])
  extends Panel(id) {

  val repeating:RepeatingView = new RepeatingView("repeating");
  add(repeating);

  for (experience <- jobExperience) {
    val item: AbstractItem = new AbstractItem(repeating.newChildId());
    repeating.add(item);

    item.add(new JobExperiencePanel("content", user, experience))
  }

}
