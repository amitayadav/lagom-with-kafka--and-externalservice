package com.knoldus.impl.services

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import com.knoldus.api.services.{ExternalService, UserService}
import com.knoldus.impl.eventsourcing.{UserEvent, UserReceived}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import com.typesafe.scalalogging.Logger
import com.knoldus.api.datamodels

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

class UserServiceImpl(userService: UserService,actorSystem: ActorSystem,persistentEntityRegistry: PersistentEntityRegistry,externalService: ExternalService)(implicit  val mat:Materializer,executionContext: ExecutionContext) extends UserService {

  private final val log = Logger(classOf[UserServiceImpl])
  val userServiceImpl = userService
/*

  override def user(name: String): ServiceCall[NotUsed, String] = ServiceCall { _ =>
    log.info(s"Customer with name $name, greeted.")
    Future.successful(s"Welcome, $name")
  }
*/


  override def checkUser: ServiceCall[NotUsed, Done] = ServiceCall{ _ =>
    consumeUser.map(result=> result)
  }


  private def consumeUser={
    userServiceImpl.
   userEvents
      .subscribe
      .atLeastOnce(
        Flow.fromFunction(msg =>
        {log.info("consume the user from topic"+msg.user)
          //Future.successful( msg.user)
          Done

        }
        )
      )
  }

  actorSystem.scheduler.schedule(0.microseconds, interval = 300.seconds){
    userEvents
  }

 /* private def getUser = {
    val result: Future[User] = externalService.getUser.invoke()
    result.map(user => {
    val ref = persistentEntityRegistry.refFor[UserEntity](user.id)
    ref.ask(ConsumeUser(user))
   })
  }*/

  override def userEvents: Topic[datamodels.UserReceived] =
    TopicProducer.singleStreamWithOffset {
    fromOffset =>
      persistentEntityRegistry.eventStream(UserEvent.Tag, fromOffset)
        .map(event => (convertEvent(event),event.offset))}

  private def convertEvent(value: EventStreamElement[UserEvent]):datamodels.UserReceived={
          value.event match {
            case UserReceived(user) =>
              datamodels.UserReceived(user)
          }
    }
}
