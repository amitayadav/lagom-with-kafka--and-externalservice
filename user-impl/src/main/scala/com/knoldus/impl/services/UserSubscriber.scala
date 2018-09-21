
package com.knoldus.impl.services

import akka.Done
import akka.stream.scaladsl.Flow
import com.knoldus.api.datamodels.UserReceived
import com.knoldus.api.services.UserService
import com.typesafe.scalalogging.Logger

class UserSubscriber(userService: UserService) {

  private final val log = Logger(classOf[UserSubscriber])

  userService
    .userEvents
    .subscribe
    .atLeastOnce(
      Flow[UserReceived].map(msg => {
        log.info("consume the user from topic" + msg.user)
        Done
      }
      )
    )
}
