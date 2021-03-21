package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {

    private val map = mutableMapOf<String, User>()

    fun registerUser(
            fullName: String,
            email: String,
            password: String
    ): User = User.makeUser(fullName, email = email, password = password)
            .also { user ->
                if (map.containsKey(user.login)) throw IllegalArgumentException("A user with this email already exists")
                map[user.login] = user
            }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        return User.makeUser(fullName, phone = rawPhone)
                .also { user ->
                    if (map.containsKey(user.login)) throw IllegalArgumentException("A user with this phone already exists")
                    map[user.login] = user
                }
    }

    fun loginUser(login: String, password: String): String? {
        return map[checkLogin(login)]?.let {
            if (it.checkPassword(password)) it.userInfo
            else null
        }
    }

    private fun checkLogin(login: String): String {
        var trimLogin = login
        if (!login.contains('@')) {
            trimLogin = login.replace("[^+\\d]".toRegex(), "")
        }
        return trimLogin.trim()
    }

    fun requestAccessCode(login: String) {
        val user = map[checkLogin(login)]
        user?.let {
            it.changePassword(it.accessCode!!, it.generateAccessCode())
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}
