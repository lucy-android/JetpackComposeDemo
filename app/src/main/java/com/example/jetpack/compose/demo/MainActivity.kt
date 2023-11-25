package com.example.jetpack.compose.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.compose.demo.ui.theme.JetpackComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenApp()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeDemoTheme {
        Greeting("Android")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenApp() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val textState = remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            Column {
                // it is the checkbox
                val checkBoxState = remember { mutableStateOf(false) }


                Surface(shadowElevation = 3.dp) {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        title = {
                            Text(
                                "Log in",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                fontFamily = robotoFamily,
                                letterSpacing = 0.01.sp,
                                color = Color(0xFF212121)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        actions = {/* do something */ },
                        scrollBehavior = scrollBehavior,
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    text = "Enter your phone number:",
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFamily,
                    fontSize = 20.sp,
                    letterSpacing = 0.01.sp,
                    color = Color(0xFF333333)
                )
                TextField(value = textState.value,
                    onValueChange = {
                        textState.value = it
                    },
                    trailingIcon = {
                        if (textState.value.text.isNotBlank()) {
                            Icon(
                                contentDescription = "clear text",
                                painter = painterResource(id = R.drawable.ic_close),
                                modifier = Modifier.clickable {
                                    textState.value = TextFieldValue("")
                                },
                                tint = Color.Unspecified,
                            )
                        } else {
                            null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0x33333340), //hide the indicator
                        unfocusedIndicatorColor = Color(0x33333340),
                        cursorColor = Color(0x33333340)
                    ),
                    singleLine = true,
                    placeholder = {
                        Text(
                            "+375 (XX) XXX-XXX-XX",
                            color = Color(0x80333333),
                            letterSpacing = 0.01.sp,
                            fontSize = 16.sp,
                            fontFamily = robotoFamily
                        )
                    })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(20))
                            .size(20.dp)
                            .background(Color(0xffcccccc))
                            .padding(3.dp)
                            .clip(RoundedCornerShape(20))
                            .background(Color.White)
                            .clickable { checkBoxState.value = !checkBoxState.value },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            tint = if (checkBoxState.value) {
                                Color.Gray
                            } else {
                                Color.Transparent
                            }
                        )
                    }
                    HyperlinkText(
                        fullText = "I agree with the processing of my \npersonal data.",
                        linkText = listOf("personal data"),
                        hyperlinks = listOf("https://loremipsum.io/privacy-policy/#:~:text=We%20may%20collect%20your%20IP,to%20improve%20your%20overall%20experience.")
                    )
                }

                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp) // adding some space to the label
                            .background(
                                color = Color(0x33333340),
                                // rounded corner to match with the OutlinedTextField
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .alpha(if (checkBoxState.value && textState.value.text == "+375445555555") 1f else 0f),
                    shape = RoundedCornerShape(10)
                ) {
                    Text(
                        text = "Continue".uppercase(),
                        fontFamily = robotoFamily,
                        letterSpacing = 2.sp
                    )
                }
            }
        },
    ) {  /* do something */ }
}

val robotoFamily = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)


@Composable
fun HyperlinkText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color = Color.Blue,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperlinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {

        linkText.forEachIndexed { index, link ->
            val startIndex = fullText.indexOf(link)
            val endIndex = startIndex + link.length
            addStyle(
                style = SpanStyle(
                    color = Color(0x80333333),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                ), start = 0, end = fullText.indexOf(link) - 1
            )
            append(fullText)
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize = fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ), start = startIndex, end = endIndex
            )
            addStringAnnotation(
                tag = "URL", annotation = hyperlinks[index], start = startIndex, end = endIndex
            )
        }
        addStyle(
            style = SpanStyle(
                fontSize = fontSize
            ), start = 0, end = fullText.length
        )
    }

    val uriHandler = LocalUriHandler.current

    ClickableText(modifier = modifier, text = annotatedString, style = TextStyle(
        color = Color(0x80333333), fontSize = 16.sp, fontFamily = robotoFamily
    ), onClick = {
        annotatedString.getStringAnnotations("URL", it, it).firstOrNull()?.let { stringAnnotation ->
            uriHandler.openUri(stringAnnotation.item)
        }
    })
}