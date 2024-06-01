package com.guzelgimadieva.tasky.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.guzelgimadieva.tasky.R

val provider = GoogleFont.Provider(
   providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

val fontName = GoogleFont("Inter")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
    )
)

// Set of Material typography styles to start with
val Typography = Typography()