package chat.onmap.chatservice.rest.dto


import java.util.UUID


data class UserInfoDto(
    val uuid: UUID,
    val name: String,
    val picture: String? = null
)