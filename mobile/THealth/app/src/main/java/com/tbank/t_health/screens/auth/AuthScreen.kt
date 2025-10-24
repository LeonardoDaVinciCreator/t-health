package com.tbank.t_health.screens.auth

import com.tbank.t_health.data.User
import UserPrefs
import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.AuthScreenTypography
import com.tbank.t_health.ui.theme.InterFontFamily
import com.tbank.t_health.ui.theme.THealthTheme

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
                CodeForm(phone = phone, onCodeSuccess = { navController.navigate("nickname/$phone/$it") })
            }
            composable("nickname/{phone}/{code}") { backStack ->
                val phone = backStack.arguments?.getString("phone") ?: ""
                val code = backStack.arguments?.getString("code") ?: ""
                NicknameForm(phone = phone,
                    code = code,
                    userPrefs = userPrefs,
                    onLoginSuccess = {
                        // Перезапускаем MainActivity или переходим на PostsScreen
                        //(context as? Activity)?.recreate()
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
            //.padding(start = 16.dp, top = 16.dp)
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
fun CodeForm(modifier: Modifier = Modifier,
             phone: String,
             onCodeSuccess: (String) -> Unit
) {
    val typography = MaterialTheme.typography
    var code by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(code) {
        // здесь добавить проверку на код из смс
        if (code.length == 4) {
            onCodeSuccess(code)
        }
    }

    Column(
        modifier = modifier
            .size(301.dp, 178.dp)
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        Text(
            text = "Введите код из SMS",
            style = AuthScreenTypography.displayMedium
        )

        BasicTextField(
            value = code,
            onValueChange = {
                if (it.length <= 4 && it.all { ch -> ch.isDigit() }) {
                    code = it
                    if (isError) isError = false
                }
            },
            singleLine = true,
            textStyle = AuthScreenTypography.labelMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .background(Color.Transparent, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .width(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (code.isEmpty()) {
                        Text(
                            "____",
                            style = AuthScreenTypography.labelMedium
                        )
                    }
                    inner()
                }
            }
        )

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
    AuthContainer(title = "Вход в T-Health") {
        var nickname by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NicknameInputField(
                nicknameText = nickname,
                onNicknameChange = {
                    nickname = it
                    if (isError) isError = false
                }
            )

            Spacer(modifier = Modifier.width(3.dp))

            AuthButton {
                val isValid = nickname.length >= 3 && nickname.all { ch -> ch.isLetterOrDigit() || ch == '_' }

                if (isValid) {
                    val user = User(
                        nickname = nickname,
                        fullName = "—",
                        phone = phone,
                        code = code
                    )
                    userPrefs.saveUser(user)
                    onLoginSuccess()
                } else {
                    isError = true
                }
            }
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


@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    THealthTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AuthScreen(modifier = Modifier.padding(innerPadding),
                    onLoginSuccess = {})
        }
    }
}
