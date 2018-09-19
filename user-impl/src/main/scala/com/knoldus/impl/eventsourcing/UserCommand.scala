package com.knoldus.impl.eventsourcing

import java.awt.print.Book

import akka.Done
import com.knoldus.api.datamodels.User
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

sealed trait UserCommand[R] extends ReplyType[R]

/*
case class ConsumeUser() extends UserCommand[User]

object ConsumeUser{
  implicit val format: Format[ConsumeUser] = Json.format[ConsumeUser]
}

*/
