package com.knoldus.api.services

import akka.{Done, NotUsed}
import com.knoldus.api.datamodels.{User, UserReceived}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait UserService extends Service {

  def checkUser: ServiceCall[NotUsed, Done]

  def userEvents: Topic[UserReceived]


  override final def descriptor = {
    import Service._

    named("user")
      .withCalls(
        //pathCall("/api/user/:name", user _),
        pathCall("/api/consume/user", checkUser _)
      )
      .withTopics(
        topic("getting-UserEvent", userEvents)
          .addProperty(
            KafkaProperties.partitionKeyStrategy,
            PartitionKeyStrategy[UserReceived](_.user.id)
          )
      )
      .withAutoAcl(true)

  }


}
