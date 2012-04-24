package pl.marpiec.cqrs

abstract class Event {

  def applyEvent(entity:Aggregate)

  def entityClass:Class[_ <: Aggregate]

}
