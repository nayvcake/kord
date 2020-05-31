package com.gitlab.kordlib.core.entity

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.common.entity.WebhookType
import com.gitlab.kordlib.core.EntitySupplyStrategy
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.behavior.GuildBehavior
import com.gitlab.kordlib.core.behavior.WebhookBehavior
import com.gitlab.kordlib.core.behavior.channel.GuildMessageChannelBehavior
import com.gitlab.kordlib.core.cache.data.WebhookData
import com.gitlab.kordlib.core.entity.channel.GuildMessageChannel
import com.gitlab.kordlib.core.getChannelOf
import com.gitlab.kordlib.core.getChannelOfOrNull

data class Webhook(val data: WebhookData, override val kord: Kord, override val strategy: EntitySupplyStrategy = kord.resources.defaultStrategy) : WebhookBehavior, Strategizable {

    override val id: Snowflake get() = Snowflake(data.id)

    val type: WebhookType get() = data.type

    val creatorId: Snowflake get() = Snowflake(data.userid)

    val channelId: Snowflake get() = Snowflake(data.channelId)

    val guildId: Snowflake get() = Snowflake(data.guildId)

    val name: String? get() = data.name

    val token: String? get() = data.token

    val channel: GuildMessageChannelBehavior get() = GuildMessageChannelBehavior(guildId, channelId, kord)

    val guild: GuildBehavior get() = GuildBehavior(guildId, kord)

    suspend fun getGuild(): Guild = strategy.supply(kord).getGuild(guildId)
    suspend fun getGuildOrNull(): Guild? = strategy.supply(kord).getGuildOrNull(guildId)

    suspend fun getChannel(): GuildMessageChannel = strategy.supply(kord).getChannelOf(channelId)
    suspend fun getChannelOrNull(): GuildMessageChannel? = strategy.supply(kord).getChannelOfOrNull(channelId)


    /**
     * returns a new [Webhook] with the given [strategy].
     *
     * @param strategy the strategy to use for the new instance. By default [EntitySupplyStrategy.CacheWithRestFallback].
     */
    fun withStrategy(strategy: EntitySupplyStrategy) = Webhook(data,kord,strategy)

}