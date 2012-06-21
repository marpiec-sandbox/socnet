package pl.marpiec.cqrs

abstract class Event {

  def applyEvent(aggregate: Aggregate)

  def entityClass: Class[_ <: Aggregate]

}
