package com.tbank.t_health.screens.auth

import UserPrefs
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tbank.t_health.R
import com.tbank.t_health.data.model.UserData
import com.tbank.t_health.ui.theme.AuthScreenTypography
import com.tbank.t_health.ui.theme.THealthTheme

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { UserPrefs(context) }

    val colors = MaterialTheme.colorScheme
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        AuthLogo(modifier = Modifier.align(Alignment.TopStart))

        NavHost(
            navController = navController,
            startDestination = "phone",
            modifier = Modifier.align(Alignment.Center)
        ) {
            composable("phone") {
                AuthForm(
                    onSuccess = { phone ->
                        navController.navigate("code/$phone")
                    }
                )
            }
            composable("code/{phone}") { backStack ->
                val phone = backStack.arguments?.getString("phone") ?: ""
                CodeForm(
                    phone = phone,
                    onCodeSuccess = { code ->
                        navController.navigate("nickname/$phone/$code")
                    },
                    onResendCode = {
                        // Здесь: вызов API для повторной отправки SMS
                        // Например: authRepository.resendCode(phone)
                        // Пока — просто лог
                        println("Повторная отправка кода на $phone")
                    }
                )
            }
            composable("nickname/{phone}/{code}") { backStack ->
                val phone = backStack.arguments?.getString("phone") ?: ""
                val code = backStack.arguments?.getString("code") ?: ""
                NicknameForm(phone = phone,
                    code = code,
                    userPrefs = userPrefs,
                    onLoginSuccess = {
                        // перезапуск MainActivity или переходим на PostsScreen
                        onLoginSuccess()
                    })
            }
        }
    }
}

@Composable
private fun AuthLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = null,
        modifier = modifier
            .size(width = 83.dp, height = 41.dp)
    )
}

@Composable
private fun AuthForm(
    modifier: Modifier = Modifier,
    onSuccess: (String) -> Unit
) {
    AuthContainer(title = "Вход в T-Health") {
        var phoneText by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhoneInputField(
                phoneText = phoneText,
                onPhoneChange = {
                    phoneText = it
                    if (isError) isError = false
                }
            )

            Spacer(modifier = Modifier.width(3.dp))

            AuthButton {
                val digits = phoneText.filter { it.isDigit() }
                val isValid = digits.matches(Regex("^(?:7|8)?\\d{10}$"))
                if (isValid) onSuccess(digits) else isError = true
            }
        }
    }
}

@Composable
private fun PhoneInputField(
    phoneText: String,
    onPhoneChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(formatPhoneNumber(phoneText)))
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val newDigits = newValue.text.filter { it.isDigit() }.take(11)
            onPhoneChange(newDigits)

            val formatted = formatPhoneNumber(newDigits)

            val digitBeforeCursor = newValue.text.take(newValue.selection.start).count { it.isDigit() }
            var cursorPosition = 0
            var digitsPassed = 0

            while (digitsPassed < digitBeforeCursor && cursorPosition < formatted.length) {
                if (formatted[cursorPosition].isDigit()) digitsPassed++
                cursorPosition++
            }

            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(cursorPosition)
            )
        },
        singleLine = true,
        textStyle = AuthScreenTypography.bodyLarge.copy(
            textAlign = TextAlign.Start,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(colors.background, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 10.dp)
                    .width(182.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (phoneText.isEmpty()) {
                    Text(
                        text = "Телефон",
                        style = AuthScreenTypography.bodySmall

                    )
                }
                innerTextField()
            }
        }
    )
}



private fun formatPhoneNumber(input: String): String {
    val digits = input.filter { it.isDigit() }
    if (digits.isEmpty()) return ""

    val clean = when {
        digits.startsWith("8") -> digits.drop(1)
        digits.startsWith("7") -> digits.drop(1)
        else -> digits
    }

    val formatted = StringBuilder("+7 ")
    for (i in clean.indices) {
        when (i) {
            0 -> formatted.append("(${clean[i]}")
            2 -> formatted.append("${clean[i]}) ")
            5 -> formatted.append("${clean[i]}-")
            7 -> formatted.append("${clean[i]}-")
            else -> formatted.append(clean[i])
        }
    }
    return formatted.toString()
}

@Composable
private fun AuthButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFDDB2D),
            contentColor = Color(0xFF000000)
        ),
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(35.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = null,
            modifier = Modifier.size(13.dp)
        )
    }
}

@Composable
fun CodeForm(
    modifier: Modifier = Modifier,
    phone: String,
    onCodeSuccess: (String) -> Unit,
    onResendCode: () -> Unit
) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme

    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Таймер: 30 секунд
    var timeLeft by remember { mutableStateOf(30) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    fun resendCode() {
        timeLeft = 30
        code = ""
        error = null
        onResendCode()
    }

    LaunchedEffect(code) {
        if (code.length == 4 && !isLoading) {
            isLoading = true
            error = null
            delay(800) // имитация запроса

            if (code == "1234") {
                onCodeSuccess(code)
            } else {
                error = "Неверный код"
            }
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .size(301.dp, 220.dp)
            .background(colors.background, RoundedCornerShape(16.dp))
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Введите код из SMS",
            style = AuthScreenTypography.displayMedium,
            textAlign = TextAlign.Center
        )

        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                    code = newValue
                }
            },
            enabled = !isLoading,
            singleLine = true,
            textStyle = AuthScreenTypography.labelMedium.copy(
                color = if (error != null) colors.error else colors.onBackground
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .background(Color.Transparent, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .width(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (code.isEmpty()) {
                        Text(
                            "____",
                            style = AuthScreenTypography.labelMedium,
                            color = colors.outline
                        )
                    }
                    inner()
                }
            }
        )

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                style = AuthScreenTypography.labelSmall,
                textAlign = TextAlign.Center
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = colors.primary
            )
        } else {
            if (timeLeft > 0) {
                Text(
                    text = "Отправить повторно через $timeLeft сек",
                    style = AuthScreenTypography.labelSmall,
                    color = colors.outline
                )
            } else {
                TextButton(
                    onClick = {
                        resendCode()
                    }
                ) {
                    Text(
                        text = "Отправить код повторно",
                        style = AuthScreenTypography.labelMedium,
                        color = colors.primary
                    )
                }
            }
        }
    }
}

@Composable
fun NicknameInputField(
    nicknameText: String,
    onNicknameChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(nicknameText))
    }
    var nickname by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            // только латинские буквы, цифры и "_"
            val filtered = newValue.text.filter { it.isLetterOrDigit() || it == '_' }
            val onlyEnglish = filtered.filter { it.code < 128 } // ограничение на английский алфавит
            onNicknameChange(onlyEnglish)

            textFieldValue = TextFieldValue(
                text = onlyEnglish,
                selection = TextRange(onlyEnglish.length)
            )
        },
        singleLine = true,
        textStyle = typography.bodyLarge.copy(
            color = colors.onTertiary,
            textAlign = TextAlign.Start,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(colors.background, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 10.dp)
                    .width(182.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (nicknameText.isEmpty()) {
                    Text(
                        text = "Никнейм",
                        style = AuthScreenTypography.bodySmall,
                        color = Color(0xFF7D7D7D)
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun NicknameForm(
    phone: String,
    code: String,
    userPrefs: UserPrefs,
    onLoginSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val authRepo = remember { com.tbank.t_health.data.repository.AuthRepository() }

    var nickname by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    AuthContainer(title = "Вход в T-Health") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            NicknameInputField(
                nicknameText = nickname,
                onNicknameChange = {
                    nickname = it
                    if (isError) isError = false
                }
            )

            Spacer(modifier = Modifier.width(3.dp))

            AuthButton {
                val isValid = nickname.length >= 3 && nickname.all { it.isLetterOrDigit() || it == '_' }

                Log.i("NicknameForm", "Нажата кнопка, nickname=$nickname, isValid=$isValid, isLoading=$isLoading")



                if (isValid && !isLoading) {
                    Log.i("NicknameForm", "Запуск scope.launch")
                    isLoading = true
                    scope.launch {
                        try {
                            val createdUser = authRepo.registerUser(
                                nickname = nickname,
                                phone = "+7$phone"
                            )
                            Log.i("AuthRepository", "Отправка запроса: nickname=$nickname, phone=$phone")


                            userPrefs.saveUser(
                                UserData(
                                    id = createdUser.id,
                                    username = createdUser.username,
                                    phone = createdUser.phone
                                )
                            )


                            onLoginSuccess()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("NicknameForm", "Ошибка регистрации: ${e.message}", e)
                            isError = true
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    isError = true
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(24.dp)
            )
        }
    }
}



@Composable
private fun NicknameCheckButton(
    enabled: Boolean,
    onCheck: () -> Unit
) {
    Button(
        onClick = onCheck,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFDDB2D),
            contentColor = Color(0xFF000000),
            disabledContainerColor = Color(0xFFFDDB2D).copy(alpha = 0.4f),
            disabledContentColor = Color.Black.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(35.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "Check nickname",
            modifier = Modifier.size(13.dp)
        )
    }
}


@Composable
private fun AuthContainer(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .size(301.dp, 178.dp)
            .background(colors.background, RoundedCornerShape(16.dp))
            .padding(horizontal = 32.dp, vertical = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Text(
                text = title,
                style = AuthScreenTypography.displayMedium
            )
            content()
        }
    }
}

