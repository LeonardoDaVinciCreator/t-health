package com.tbank.t_health.constants

import com.tbank.t_health.ui.components.FooterItemData

object NavigationDestinations {
    const val HEALTH = "health"
    const val ACHIEVEMENTS = "achievements"
    const val POSTS = "posts"
    const val CHAT = "chat"
    const val PROFILE = "profile"
    const val WORKOUT = "workout"
    const val ADD_WORKOUT = "addWorkout"
    const val AUTH = "auth"
}

object NavigationTabs {
    val Health = FooterItemData(
        id = NavigationDestinations.HEALTH,
        iconName = "ic_home",
        label = "Главная",
        iconDefaultWidth = 21,
        iconDefaultHeight = 19
    )

    val Achievements = FooterItemData(
        id = NavigationDestinations.ACHIEVEMENTS,
        iconName = "ic_trophy",
        label = "Достижения",
        iconDefaultWidth = 25,
        iconDefaultHeight = 23
    )

    val Posts = FooterItemData(
        id = NavigationDestinations.POSTS,
        iconName = "ic_posts",
        label = "Лента",
        iconDefaultWidth = 25,
        iconDefaultHeight = 25
    )

    val Chat = FooterItemData(
        id = NavigationDestinations.CHAT,
        iconName = "ic_chat",
        label = "Чат",
        iconDefaultWidth = 25,
        iconDefaultHeight = 25
    )

    val Profile = FooterItemData(
        id = NavigationDestinations.PROFILE,
        iconName = "ic_profile",
        label = "Профиль",
        iconDefaultWidth = 30,
        iconDefaultHeight = 30
    )

    val AllTabs = listOf(Health, Achievements, Posts, Chat, Profile)
}