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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.HeaderTypography
import com.tbank.t_health.ui.theme.InterFontFamily

@Composable
fun HeaderPost() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = "Фильтр",
            modifier = Modifier.size(34.dp)
        )

        BasicTextField(
            value = searchText,
            modifier = Modifier
                .size(228.dp, 28.dp)
                .background(Color(0xFFCDCDCD), RoundedCornerShape(12.dp)),
            onValueChange = { searchText = it },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 16.sp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 0.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Поиск",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (searchText.text.isEmpty()) {
                            Text(
                                text = "Поиск",
                                style = HeaderTypography.headlineLarge
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        // Уведомления
        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "Уведомления",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPostPreview() {
    HeaderPost()
}
