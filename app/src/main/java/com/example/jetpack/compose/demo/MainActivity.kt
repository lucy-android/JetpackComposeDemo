package com.example.jetpack.compose.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack.compose.demo.ui.theme.JetpackComposeDemoTheme
import kotlinx.coroutines.delay

enum class DemoRoutes {
    Start, Sms, LoggedIn
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
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
                                style = MaterialTheme.typography.headlineLarge,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigate(DemoRoutes.Start.name) }) {
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
                StartScreen(modifier = Modifier.padding(16.dp)) { arg -> navController.navigate("${DemoRoutes.Sms.name}/$arg") }
            }

            composable(route = "${DemoRoutes.Sms.name}/{phoneNumber}") { navBackStackEntry ->
                val phoneNumber = navBackStackEntry.arguments?.getString("phoneNumber")
                phoneNumber?.let { number ->
                    SecondScreen(modifier = Modifier.padding(16.dp), phoneNumber = number) {
                        navController.navigate(
                            DemoRoutes.LoggedIn.name
                        )
                    }
                }
            }

            composable(route = DemoRoutes.LoggedIn.name){
                LoggedInScreen(modifier = Modifier.padding(16.dp)) {navController.navigate(DemoRoutes.Start.name)}
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier, onButtonClicked: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
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
        TextField(value = textState.value, onValueChange = {
            if (TextUtils.isDigitsOnly(it.text) || it.text.contains("+")) {
                textState.value = it
            }
        }, trailingIcon = {
            if (textState.value.text.isNotBlank()) {
                Icon(
                    contentDescription = "clear text",
                    painter = painterResource(id = R.drawable.ic_close),
                    modifier = Modifier.clickable {
                        textState.value = TextFieldValue("")
                    },
                    tint = Color.Unspecified,
                )
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),

            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0x55614DDF),
                unfocusedIndicatorColor = Color(0x33333340),
                cursorColor = Color(0x33333340)
            ), singleLine = true, placeholder = {
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
                    .padding(end = 16.dp)
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
                    .padding(top = 8.dp)
                    .background(
                        color = Color(0x33333340), shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        Button(
            onClick = { onButtonClicked.invoke(textState.value.text) },
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .alpha(if (checkBoxState.value && textState.value.text.matches(Regex("^[+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}\$"))) 1f else 0f),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    modifier: Modifier = Modifier, phoneNumber: String, onButtonClicked: () -> Unit
) {

    var setView by remember { mutableStateOf(60) }
    LaunchedEffect(key1 = setView) {
        if (setView > 0) {
            delay(1_000)
            setView -= 1
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.enter_code),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontSize = 20.sp,
            letterSpacing = 0.01.sp,
            color = Color(0xFF333333)
        )


        val globalText = stringResource(id = R.string.sent_code, phoneNumber)

        val start = globalText.indexOf(phoneNumber)
        val spanStyles = listOf(
            AnnotatedString.Range(
                SpanStyle(
                    fontWeight = FontWeight.Normal, color = Color(0xFFBDBDBD), fontSize = 16.sp
                ), start = 0, end = start - 1
            ), AnnotatedString.Range(
                SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                start = start,
                end = start + phoneNumber.length
            )
        )

        val placeholder2 = setView.toString()

        val globalText2 = stringResource(id = R.string.send_code, placeholder2)

        val start2 = globalText2.indexOf(placeholder2)
        val spanStyles2 = listOf(
            AnnotatedString.Range(
                SpanStyle(
                    fontWeight = FontWeight.Normal, color = Color(0xFFBDBDBD), fontSize = 16.sp
                ), start = 0, end = start2 - 1
            ), AnnotatedString.Range(
                SpanStyle(fontWeight = FontWeight.Bold),
                start = start2,
                end = start2 + placeholder2.length
            )
        )

        val spanStyles3 = SpanStyle(
            color = Color(0xFF614DDF),
        )

        Column(
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = textState.value, onValueChange = {
                if (TextUtils.isDigitsOnly(it.text)) {
                    textState.value = it
                }
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
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0x55614DDF),
                    unfocusedIndicatorColor = Color(0x33333340),
                    cursorColor = Color(0x33333340)
                ), singleLine = true, placeholder = {
                    Text(
                        stringResource(R.string.placeholder_sms_code),
                        color = Color(0x80333333),
                        letterSpacing = 0.01.sp,
                        fontSize = 16.sp,
                        fontFamily = robotoFamily
                    )
                })
            Text(
                text = AnnotatedString(text = globalText, spanStyles = spanStyles),
                fontFamily = robotoFamily,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            var isClicked by remember { mutableStateOf(false) }

            var progressTimer by remember { mutableStateOf(3) }
            LaunchedEffect(key1 = progressTimer) {
                if (progressTimer in 0..3) {
                    delay(1_000)
                    progressTimer -= 1
                } else if (progressTimer == -1) {
                    progressTimer = 3
                    isClicked = false
                }
            }


            if (isClicked && progressTimer < 3) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp), color = MaterialTheme.colorScheme.secondary
                )
            } else {
                ClickableText(
                    onClick = {
                        if (!isClicked && setView <= 0) {
                            isClicked = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 20.dp),
                    text = if (setView > 0) AnnotatedString(
                        text = globalText2, spanStyles = spanStyles2
                    ) else AnnotatedString(
                        text = stringResource(id = R.string.send_another_code),
                        spanStyle = spanStyles3
                    )
                )
            }
        }

        Row(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
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
                .alpha(if (textState.value.text.matches(Regex("^[0-9]{6}\$"))) 1f else 0f),
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


@Composable
fun LoggedInScreen(
    modifier: Modifier = Modifier, navigateToStartAction: () -> Unit
) {
    Column {
        Row(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(
                        color = Color(0x33333340), shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        Button(
            onClick = { navigateToStartAction.invoke() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .alpha(1f),
            shape = RoundedCornerShape(10)
        ) {
            Text(
                text = stringResource(R.string.exit_text).uppercase(),
                fontFamily = robotoFamily,
                letterSpacing = 2.sp
            )
        }
    }
}