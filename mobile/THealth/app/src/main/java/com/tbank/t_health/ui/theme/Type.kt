package com.tbank.t_health.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.tbank.t_health.R

//val InterFontFamily = FontFamily(
//    Font(R.font.inter_variable, FontWeight.Thin),
//    Font(R.font.inter_variable, FontWeight.ExtraLight),
//    Font(R.font.inter_variable, FontWeight.Light),
//    Font(R.font.inter_variable, FontWeight.Normal),
//    Font(R.font.inter_variable, FontWeight.Medium),
//    Font(R.font.inter_variable, FontWeight.SemiBold),
//    Font(R.font.inter_variable, FontWeight.Bold),
//    Font(R.font.inter_variable, FontWeight.ExtraBold),
//    Font(R.font.inter_variable, FontWeight.Black)
//)

val InterFontFamily = FontFamily( Font(R.font.inter_variable))



val THealthTypography = Typography(

    // Заголовки
    displayLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    displayMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 10.sp
    ),
    displaySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),

    // Основной текст
    headlineLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    // Body текст
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    ),

    // Labels
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 1.sp
    )
)

val AuthScreenTypography = Typography(
    // Заголовок экрана
    displayMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        color = Color.Black,
        textAlign = TextAlign.Center
    ),

    // для плейсхолдеров
    bodySmall = TextStyle(//BasicTextField
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        color = Color(0xFF7D7D7D)
    ),

    // Текст внутри полей ввода
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = Color.Black),

    // ввод кода
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        letterSpacing = 8.sp,
        color = Color.Black,
    ),
)

val PostBlockTypography = Typography(
    headlineLarge = TextStyle( // Заголовок поста
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = Color.Black
    ),
    bodyLarge = TextStyle( // Развёрнутый основной текст
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = Color.Black
    ),
    bodyMedium = TextStyle( // Основной текст в сжатом виде
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        color = Color.Black
    ),
    labelMedium = TextStyle( // Лайки и комментарии
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        lineHeight = 1.em,
        color = Color(0xFF838383)
    ),
    labelSmall = TextStyle( // Текст "еще"
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        color = Color(0xFF2188EC)
    ),
    headlineMedium = TextStyle(//никнейм
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        color = Color.Black
    ),
    bodySmall = TextStyle(//дата
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        lineHeight = 10.sp,
        color = Color(0xFF383838)
    )
)


val FooterTypography = Typography(
    headlineLarge = TextStyle( //footer
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 9.sp,
        color = Color.Black
    )
)

val HeaderTypography = Typography(
    headlineLarge = TextStyle( //footer
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        color = Color.Black
    )
)

val StatsTypography = Typography(
    //имя пользователя
    headlineLarge= TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        color = Color.Black
    ),
    //статы
    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        color = Color.Black
    ),
    //заголовок для шагов
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        color = Color.Black
    ),
    //цифры на диаграмме
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 9.sp,
        lineHeight = 9.sp,
        color = Color.Black
    ),
    //дата для даграммы
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        lineHeight = 10.sp,
        color = Color.Black
    ),
    //текст на меню
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        color = Color.Black
    ),

)