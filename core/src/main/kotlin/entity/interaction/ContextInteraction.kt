package dev.kord.core.entity.interaction

import dev.kord.core.Kord
import dev.kord.core.behavior.UserBehavior
import dev.kord.core.behavior.interaction.ApplicationCommandInteractionBehavior
import dev.kord.core.cache.data.InteractionData
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.core.supplier.EntitySupplier
import dev.kord.core.supplier.EntitySupplyStrategy

/**
 * Represents an interaction of type [ApplicationCommand][dev.kord.common.entity.InteractionType.ApplicationCommand]
 */
sealed interface ApplicationCommandInteraction : Interaction, ApplicationCommandInteractionBehavior


/**
 * An [ApplicationCommandInteraction] that's invoked through chat input.
 */
sealed interface  ChatInputCommandInteraction : ApplicationCommandInteraction {
    val command: InteractionCommand get() =  InteractionCommand(data.data, kord)


}

/**
 * A [ApplicationCommandInteraction] that's invoked through chat input specific to a guild.
 */
class GuildChatInputCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : ChatInputCommandInteraction, GuildApplicationCommandInteraction



/**
 * An [ApplicationCommandInteraction] that's invoked through chat input.
 */
class GlobalChatInputCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : ChatInputCommandInteraction, GlobalApplicationCommandInteraction


/**
 * An [ApplicationCommandInteraction] that's invoked through user commands.
 */
sealed interface  UserCommandInteraction : ApplicationCommandInteraction {

    val name: String get() = data.data.name.value!!

    private val resolvedUsersData  get() = data.data.resolvedObjectsData.value?.users?.value
    val users get() = resolvedUsersData.orEmpty().mapValues { User(it.value, kord) }
}

/**
 * An [ApplicationCommandInteraction] that's invoked through user commands specific to a guild.
 */
class GuildUserCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : UserCommandInteraction, GuildApplicationCommandInteraction {
    private val resolvedMembersData  get() = data.data?.resolvedObjectsData.value?.members?.value
    val members get() = resolvedMembersData.orEmpty().mapValues { memberData ->
        Member(memberData.value, users[memberData.key]!!.data, kord) }
}

/**
 * An [ApplicationCommandInteraction] that's invoked through user commands.
 */
class GlobalUserCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : UserCommandInteraction, GlobalApplicationCommandInteraction


/**
 * An [ApplicationCommandInteraction] that's invoked through messages.
 */
sealed interface  MessageCommandInteraction : ApplicationCommandInteraction {

    val name: String get() = data.data.name.value!!

    private val resolvedMessagesData get() = data.data.resolvedObjectsData.value?.messages?.value
    val messages get() = resolvedMessagesData.orEmpty().mapValues { Message(it.value, kord) }

}

/**
 * An [ApplicationCommandInteraction] that's invoked through messages specific to a guild.
 */
class GuildMessageCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : MessageCommandInteraction, GuildApplicationCommandInteraction

/**
 * An [ApplicationCommandInteraction] that's invoked through messages.
 */
class GlobalMessageCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : MessageCommandInteraction, GlobalApplicationCommandInteraction


class UnknownApplicationCommandInteraction(
    override val data: InteractionData,
    override val kord: Kord,
    override val supplier: EntitySupplier
) : MessageCommandInteraction, ApplicationCommandInteraction {
    override val user: UserBehavior
        get() = UserBehavior(data.user.value!!.id, kord)

    override fun withStrategy(strategy: EntitySupplyStrategy<*>): Interaction {
        return UnknownComponentInteraction(data, kord, strategy.supply(kord))
    }
}
