package com.tbank.t_health.constants

import com.tbank.t_health.ui.components.FooterItemData

object NavigationDestinations {
    const val HEALTH = "health"
    const val POSTS = "posts"
    const val ADD_POST = "addPosts"
    //const val CHAT = "chat"
    //const val PROFILE = "profile"
    const val WORKOUT = "workout"
    const val ADD_WORKOUT = "addWorkout"
    const val AUTH = "auth"

    const val NOTIFICATIONS = "notifications"
}

object NavigationTabs {
    val Health = FooterItemData(
        id = NavigationDestinations.HEALTH,
        iconName = "ic_home",
        label = "Главная",
        iconDefaultWidth = 21,
        iconDefaultHeight = 19
    )

    val Posts = FooterItemData(
        id = NavigationDestinations.POSTS,
        iconName = "ic_posts",
        label = "Лента",
        iconDefaultWidth = 25,
        iconDefaultHeight = 25
    )

    val ADD_POST = FooterItemData(
        id = NavigationDestinations.ADD_POST,
        iconName = "ic_plus",
        label = "Новый пост",
        iconDefaultWidth = 25,
        iconDefaultHeight = 25
    )

//    val Profile = FooterItemData(
//        id = NavigationDestinations.PROFILE,
//        iconName = "ic_profile",
//        label = "Профиль",
//        iconDefaultWidth = 30,
//        iconDefaultHeight = 30
//    )

    val AllTabs = listOf(Health, Posts, ADD_POST)
}