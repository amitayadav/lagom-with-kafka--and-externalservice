package com.knoldus.impl.eventsourcing

import java.time.LocalDateTime

import com.knoldus.api.datamodels.User
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class UserEntity extends PersistentEntity{
  override type Command = UserCommand[_]
  override type Event = UserEvent
  override type State = UserState

  override def initialState: UserState = UserState(None, LocalDateTime.now.toString)

  override def behavior: (UserState)=> Actions = {
    case UserState(_ , _) => Actions()

      /*.onReadOnlyCommand[ConsumeUser,User]{
      case (ConsumeUser(),ctx,state)=>
         ctx.reply(state.user.getOrElse(User("","","")))
    }
*/

  }
}
