
package com.knoldus.impl.eventsourcing

import com.knoldus.api.datamodels.User
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

sealed trait UserEvent extends AggregateEvent[UserEvent] {
  def aggregateTag = UserEvent.Tag
}

object UserEvent {
  val Tag = AggregateEventTag[UserEvent]
}

case class UserAdded(user: User) extends UserEvent

object UserAdded {

  implicit val format: Format[UserAdded] = Json.format[UserAdded]
}
