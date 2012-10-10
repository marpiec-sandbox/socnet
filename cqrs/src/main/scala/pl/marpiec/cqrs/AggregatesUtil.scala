package pl.marpiec.cqrs

/**
 * @author Marcin Pieciukiewicz
 */

object AggregatesUtil {

  def incrementVersion(aggregate: Aggregate) {
    aggregate.version = aggregate.version + 1
  }

}
