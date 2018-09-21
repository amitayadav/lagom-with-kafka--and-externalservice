
package com.knoldus.impl.services

import akka.Done
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.knoldus.api.datamodels
import com.knoldus.api.datamodels.User
import com.knoldus.api.services.{ExternalService, UserService}
import com.knoldus.impl.eventsourcing._
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(actorSystem: ActorSystem, persistentEntityRegistry: PersistentEntityRegistry,
                      externalService: ExternalService)(implicit val mat: Materializer,
                                                        executionContext: ExecutionContext) extends UserService {

  private final val log = Logger(classOf[UserServiceImpl])

 /* override def checkUser = ServiceCall { _ =>
    userSubscriber
      .userSubscribeResult
      .map(response => response)

  }*/

  override def getUserFromExternalService = ServiceCall { _ =>
    val result: Future[User] = externalService.getUser.invoke()
    result.map(user => {
      val ref = persistentEntityRegistry.refFor[UserEntity](user.id)
      val replyType= ref.ask(AddUser(user))
        .map(_ => {
          log.info("new user added")
          Done
        })
      actorSystem.scheduler.schedule(0.microseconds, interval = 300.seconds){
        replyType.map(e => e match {
          case Done => userEvents
          case _ => log.info("no user")
        })
      }

      Done.getInstance()
    })

  }

  override def userEvents: Topic[datamodels.UserReceived] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        log.info("inside userProducer")
        persistentEntityRegistry.eventStream(UserEvent.Tag, fromOffset)
          .map(event =>
            (convertEvent(event), event.offset))
    }

  private def convertEvent(value: EventStreamElement[UserEvent]): datamodels.UserReceived = {
    value.event match {
      case UserAdded(user) => log.info("inside userProducer convert method")
        datamodels.UserReceived(user)
    }
  }
}

