package com.katerinah.smackchat.Utils

// PROFILE
const val DEFAULT_AVATAR = "profiledefault"
const val DEFAULT_COLOR = "[0.5, 0.5, 0.5, 1]"

// URLS
const val BASE_URL = "https://mac-chat-api-app.herokuapp.com/v1"
const val SOCKET_URL = "https://mac-chat-api-app.herokuapp.com/"

const val URL_ACCOUNT_REGISTER = "${BASE_URL}/account/register"
const val URL_ACCOUNT_LOGIN = "${BASE_URL}/account/login"
const val URL_ADD_USER = "${BASE_URL}/user/add"
const val URL_GET_USER_BY_EMAIL = "${BASE_URL}/user/byEmail/"
const val URL_GET_CHANNELS = "${BASE_URL}/channel"
const val URL_GET_MESSAGES = "${BASE_URL}/message/byChannel/"

// Outgoing Socket Events
const val NEW_CHANNEL = "newChannel"
//const val START_TYPE = "startType"
//const val STOP_TYPE = "stopType"
const val NEW_MESSAGE = "newMessage"

// Incoming Socket Events
const val CHANNEL_CREATED = "channelCreated"
//const val USER_TYPING_UPDATE = "userTypingUpdate"
const val MESSAGE_CREATED = "messageCreated"

// BROADCAST
const val BROADCAST_USER_DATA_CHANGED = "BROADCAST_USER_DATA_CHANGED"
