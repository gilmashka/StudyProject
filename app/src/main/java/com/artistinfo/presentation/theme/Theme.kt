package com.artistinfo.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/*
* ТЕМУ ЗАИМСТВОВАЛ ИЗ АГОНА-ПРОЕКТА,
* ВКЛЮЧАЯ ЭЛЕМЕНТЫ ДИЗАЙНА (КНОПКИ, ТЕКСТФИЛДЫ, ect.)
*/
private val LightColorScheme = lightColorScheme(
    //фон
    background = backgroundLightColor,

    //главный и вторичные цвета (кнопки, навигация?)
    primary = primaryLightColor,
    secondary = secondaryLightColor,

    //цвет содержимого на главном и вторичном цветах
    onPrimary = onPrimaryLightColor,
    onSecondary = onSecondaryLightColor,

    //цвет рамок
    outline = bordersLightColor,

    //поля ввода + текст на них
    primaryContainer = primaryContainerLightColor,
    onPrimaryContainer = onPrimaryContainerLightColor,

    secondaryContainer = secondaryContainerLightColor,
    onSecondaryContainer = onSecondaryContainerLightColor,

    //поверхности (карточки в ленте и в ЛК, ботом шиты?) + элементы на них
    surface = surfaceLightColor,
    onSurface = onSurfaceLightColor,

    //ошибка
    error = errorLightColor
)


@Composable
fun ArtistInfoTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
