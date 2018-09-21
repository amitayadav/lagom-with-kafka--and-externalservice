
package com.knoldus.impl.eventsourcing

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class UserEntity extends PersistentEntity {
  override type Command = UserCommand[_]
  override type Event = UserEvent
  override type State = UserState

  override def initialState: UserState = UserState(None, LocalDateTime.now.toString)

  override def behavior: (UserState) => Actions = {
    case UserState(_, _) => Actions()
      .onCommand[AddUser, Done] {
      case (AddUser(user), ctx, _) =>
        ctx.thenPersist(UserAdded(user))(_ =>
          ctx.reply(Done)
        )
    }
      .onEvent {
        case (UserAdded(user), _) =>
          UserState(Some(user), LocalDateTime.now().toString)

      }
  }
}
