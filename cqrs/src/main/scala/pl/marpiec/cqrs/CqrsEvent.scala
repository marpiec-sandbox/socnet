package pl.marpiec.cqrs

abstract class CqrsEvent {

  def applyEvent(entity:CqrsEntity)

  def entityClass:Class[_ <: CqrsEntity]

}
