package com.example.jetpack.compose.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
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
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack.compose.demo.ui.theme.JetpackComposeDemoTheme

enum class DemoRoutes {
    Start,
    Sms
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    // HomeScreenApp()
                    DemoApp()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp(
    navController: NavHostController = rememberNavController()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            Column {
                Surface(shadowElevation = 3.dp) {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        title = {
                            Text(
                                stringResource(R.string.log_in),
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
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DemoRoutes.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = DemoRoutes.Start.name) {
                StartScreen { navController.navigate(DemoRoutes.Sms.name) }
            }

            composable(route = DemoRoutes.Sms.name) {
                SecondScreen(innerPadding = innerPadding)
            }
        }
        /* Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

        }
            Text(
                modifier = Modifier.padding(8.dp),
                text =
                """
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.
                """.trimIndent(),
            )
        }*/
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(onButtonClicked: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        Column {
            val checkBoxState = remember { mutableStateOf(false) }
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.enter_details),
                fontWeight = FontWeight.Bold,
                fontFamily = robotoFamily,
                fontSize = 20.sp,
                letterSpacing = 0.01.sp,
                color = Color(0xFF333333)
            )
            TextField(
                value = textState.value,
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
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0x33333340), //hide the indicator
                    unfocusedIndicatorColor = Color(0x33333340),
                    cursorColor = Color(0x33333340)
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        stringResource(R.string.placeholder_phone_number),
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
                    fullText = stringResource(R.string.personal_data_agreement),
                    linkText = listOf(stringResource(R.string.personal_data)),
                    hyperlinks = listOf(stringResource(R.string.privacy_policy))
                )
            }

            Row(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp) // adding some space to the label
                        .background(
                            color = Color(0x33333340), shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            Button(
                onClick = { onButtonClicked.invoke() },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .alpha(if (checkBoxState.value && textState.value.text == stringResource(R.string.phone_number)) 1f else 0f),
                shape = RoundedCornerShape(10)
            ) {
                Text(
                    text = stringResource(R.string.continue_text).uppercase(),
                    fontFamily = robotoFamily,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun SecondScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

    }
    Text(
        modifier = Modifier.padding(8.dp),
        text =
        """
                    This is the second screen
                """.trimIndent(),
    )
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