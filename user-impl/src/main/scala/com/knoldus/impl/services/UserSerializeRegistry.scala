package com.knoldus.impl.services

import com.knoldus.api.datamodels.User
import com.knoldus.impl.eventsourcing.{UserReceived, UserState}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable

object UserSerializerRegistry extends JsonSerializerRegistry {

  override def serializers: immutable.Seq[JsonSerializer[_]] = immutable.Seq(
    JsonSerializer[User],
    //JsonSerializer[ConsumeUser],
    JsonSerializer[UserReceived],
    JsonSerializer[UserState]
  )
}
