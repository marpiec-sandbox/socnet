package pl.marpiec.socnet.service.programmerprofile

import org.testng.annotations.Test
import org.testng.Assert._
import pl.marpiec.cqrs._
import pl.marpiec.socnet.model.ProgrammerProfile
import pl.marpiec.socnet.constant.TechnologyKnowledgeLevel

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class ProgrammerProfileCommandTest {
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
  
  var newTechnologies = Map[String, Int]()
  newTechnologies += "Java" -> TechnologyKnowledgeLevel.EXPERT
  newTechnologies += "Scala" -> TechnologyKnowledgeLevel.WORKED_WITH
  newTechnologies += "Wicket" -> TechnologyKnowledgeLevel.WORKED_WITH
  newTechnologies += "Spring" -> TechnologyKnowledgeLevel.WORKED_FOR_LONG

  programmerProfileCommand.changeTechnologies(userId, programmerProfile.id, programmerProfile.version, newTechnologies, Nil)

  programmerProfile = programmerProfileDatabase.getById(programmerProfileId).get

  assertEquals(programmerProfile.technologyKnowledge.size, TechnologyKnowledgeLevel.WORKED_FOR_LONG.value)
  assertEquals(programmerProfile.technologyKnowledge("Java"), TechnologyKnowledgeLevel.EXPERT.value)
  assertEquals(programmerProfile.technologyKnowledge("Scala"), TechnologyKnowledgeLevel.WORKED_WITH.value)
  assertEquals(programmerProfile.technologyKnowledge("Wicket"), TechnologyKnowledgeLevel.WORKED_WITH.value)
  assertEquals(programmerProfile.technologyKnowledge("Spring"), TechnologyKnowledgeLevel.WORKED_FOR_LONG.value)

  var removeTechnologies = List[String]()
  removeTechnologies += "Spring"

  var changeTechnologies = Map[String, Int]()
  changeTechnologies += "Java" -> TechnologyKnowledgeLevel.WORKED_FOR_LONG
  changeTechnologies += "Guice" -> TechnologyKnowledgeLevel.BASIC

  programmerProfileCommand.changeTechnologies(userId, programmerProfile.id, programmerProfile.version, newTechnologies, Nil)

  programmerProfile = programmerProfileDatabase.getById(programmerProfileId).get

  assertEquals(programmerProfile.technologyKnowledge.size, TechnologyKnowledgeLevel.WORKED_FOR_LONG.value)
  assertEquals(programmerProfile.technologyKnowledge("Java"), TechnologyKnowledgeLevel.WORKED_FOR_LONG.value)
  assertEquals(programmerProfile.technologyKnowledge("Scala"), TechnologyKnowledgeLevel.WORKED_WITH.value)
  assertEquals(programmerProfile.technologyKnowledge("Wicket"), TechnologyKnowledgeLevel.WORKED_WITH.value)
  assertEquals(programmerProfile.technologyKnowledge("Guice"), TechnologyKnowledgeLevel.BASIC.value)
  
}
