package pl.marpiec.socnet.service.programmerprofile

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.socnet.constant.TechnologyKnowledgeLevel
import pl.marpiec.socnet.readdatabase.mock.ProgrammerProfileDatabaseMockImpl
import pl.marpiec.socnet.model.programmerprofile.KnownTechnology

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ProgrammerProfileCommandTest {

  def testDefineKnownTechnologies() {

    val eventStore: EventStore = new EventStoreMockImpl
    val aggregateCache: AggregateCache = new AggregateCacheSimpleImpl

    val dataStore: DataStore = new DataStoreImpl(eventStore, aggregateCache)
    val programmerProfileDatabase = new ProgrammerProfileDatabaseMockImpl(dataStore)
    val programmerProfileCommand = new ProgrammerProfileCommandImpl(eventStore)

    val uidGenerator: UidGenerator = new UidGeneratorMockImpl

    val userId = uidGenerator.nextUid
    val programmerProfileId = uidGenerator.nextUid

    programmerProfileCommand.createProgrammerProfile(userId, userId, programmerProfileId)

    var programmerProfile:ProgrammerProfile = programmerProfileDatabase.getById(programmerProfileId).get

    var newTechnologies = List[KnownTechnology]()
    newTechnologies ::= KnownTechnology("Java", TechnologyKnowledgeLevel.EXPERT)
    newTechnologies ::= KnownTechnology("Scala", TechnologyKnowledgeLevel.WORKED_WITH)
    newTechnologies ::= KnownTechnology("Wicket", TechnologyKnowledgeLevel.WORKED_WITH)
    newTechnologies ::= KnownTechnology("Spring", TechnologyKnowledgeLevel.WORKED_FOR_LONG)

    programmerProfileCommand.changeTechnologies(userId, programmerProfile.id, programmerProfile.version, newTechnologies, Nil)

    programmerProfile = programmerProfileDatabase.getById(programmerProfileId).get

    assertEquals(programmerProfile.technologyKnowledge.size, 4)
    assertEquals(programmerProfile.technologyKnowledge("Java").knowledgeLevel.value, TechnologyKnowledgeLevel.EXPERT.value)
    assertEquals(programmerProfile.technologyKnowledge("Scala").knowledgeLevel.value, TechnologyKnowledgeLevel.WORKED_WITH.value)
    assertEquals(programmerProfile.technologyKnowledge("Wicket").knowledgeLevel.value, TechnologyKnowledgeLevel.WORKED_WITH.value)
    assertEquals(programmerProfile.technologyKnowledge("Spring").knowledgeLevel.value, TechnologyKnowledgeLevel.WORKED_FOR_LONG.value)

    var removeTechnologies = List[String]()
    removeTechnologies ::= "Spring"

    var changeTechnologies = List[KnownTechnology]()
    changeTechnologies ::= KnownTechnology("Java", TechnologyKnowledgeLevel.WORKED_FOR_LONG)
    changeTechnologies ::= KnownTechnology("Guice", TechnologyKnowledgeLevel.BASIC)

    programmerProfileCommand.changeTechnologies(userId, programmerProfile.id, programmerProfile.version, changeTechnologies, removeTechnologies)

    programmerProfile = programmerProfileDatabase.getById(programmerProfileId).get

    assertEquals(programmerProfile.technologyKnowledge.size, 4)
    assertEquals(programmerProfile.technologyKnowledge("Java").knowledgeLevel.value, TechnologyKnowledgeLevel.WORKED_FOR_LONG.value)
    assertEquals(programmerProfile.technologyKnowledge("Scala").knowledgeLevel.value, TechnologyKnowledgeLevel.WORKED_WITH.value)
    assertEquals(programmerProfile.technologyKnowledge("Wicket").knowledgeLevel.value, TechnologyKnowledgeLevel.WORKED_WITH.value)
    assertEquals(programmerProfile.technologyKnowledge("Guice").knowledgeLevel.value, TechnologyKnowledgeLevel.BASIC.value)

  }
}
