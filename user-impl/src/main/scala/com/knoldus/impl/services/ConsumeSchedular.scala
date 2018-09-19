/*
package com.knoldus.impl.services

import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import com.knoldus.api.datamodels.User
import com.knoldus.api.services.ExternalService
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import javax.inject.Inject
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

class ConsumeSchedular @Inject() (externalService: ExternalService,actorSystem: ActorSystem,persistentEntityRegistry: PersistentEntityRegistry)(implicit  val mat:Materializer,executionContext: ExecutionContext) {


  actorSystem.scheduler.schedule(0.microseconds, interval = 300.seconds){
    getUser
  }

  private def getUser = {
    val result: Future[User] = externalService.getUser.invoke()
    result.map(response => response)
  }
}*/
