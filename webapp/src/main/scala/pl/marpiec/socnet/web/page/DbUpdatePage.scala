package pl.marpiec.socnet.web.page

import template.SimpleTemplatePage
import org.apache.wicket.spring.injection.annot.SpringBean
import pl.marpiec.socnet.readdatabase.{ProgrammerProfileDatabase, UserDatabase, ArticleDatabase}
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.socnet.service.programmerprofile.ProgrammerProfileCommand
import pl.marpiec.cqrs.UidGenerator

/**
 * @author Marcin Pieciukiewicz
 */

class DbUpdatePage extends SimpleTemplatePage {
  
  @SpringBean private var userDatabase: UserDatabase = _
  @SpringBean private var programmerProfileDatabase: ProgrammerProfileDatabase = _
  @SpringBean private var programmerProfileCommand: ProgrammerProfileCommand = _

  @SpringBean private var uidGenerator: UidGenerator = _

  userDatabase.getAllUsers.foreach(user => {

    val profileOption:Option[ProgrammerProfile] = programmerProfileDatabase.getProgrammerProfileByUserId(user.id)
    if(profileOption.isEmpty) {
      programmerProfileCommand.createProgrammerProfile(user.id, user.id, uidGenerator.nextUid)
    }
    
  })
  
}
