
package com.knoldus.impl.eventsourcing

import com.knoldus.api.datamodels.User
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

sealed trait UserEvent extends AggregateEvent[UserEvent]{
   def aggregateTag = UserEvent.Tag
}

object UserEvent {
  val Tag = AggregateEventTag[UserEvent]
}

case class UserReceived (user: User) extends UserEvent

object UserReceived {

  implicit val format: Format[UserReceived] = Json.format[UserReceived]
}
