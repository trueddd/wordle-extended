@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.GameState
import data.WordState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

fun main() = application {
    val keyEventsFlow = MutableSharedFlow<KeyEvent>(extraBufferCapacity = 1)
    val wordsFile = File("E:\\Projects\\wordle-filter\\src\\main\\resources\\words-filtered2.txt")
    val words = wordsFile.readLines()
    Window(
        title = "Wordle Extended",
        icon = painterResource("w_letter.png"),
        onCloseRequest = ::exitApplication,
        onKeyEvent = { keyEventsFlow.tryEmit(it) },
    ) {
        MaterialTheme {
            GamePanel(keyEventsFlow, words)
        }
    }
}

@Composable
fun GamePanel(
    keyEventsFlow: MutableSharedFlow<KeyEvent>,
    words: List<String>,
) {
    val word = remember { words.random() }
    println("Guessed word: $word")
    val alphabetRange = Key.A.keyCode .. Key.Z.keyCode
    var gameState by remember { mutableStateOf(GameState.create(word)) }
    LaunchedEffect(word) {
        keyEventsFlow
            .filter { (it.nativeKeyEvent as? java.awt.event.KeyEvent)?.id == java.awt.event.KeyEvent.KEY_RELEASED }
            .onEach { keyEvent ->
                when (keyEvent.key.keyCode) {
                    Key.Backspace.keyCode -> gameState = gameState.onBackspaceClicked()
                    Key.Enter.keyCode -> if (gameState.canCheckWord) gameState = gameState.checkWord()
                    in alphabetRange -> gameState = gameState.onCharacterTyped(keyEvent.asTypedText())
                }
            }
            .launchIn(this)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            gameState.attempts.forEach { wordState ->
                Word(
                    wordState = wordState,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Button(
                    onClick = {
                        if (gameState.canCheckWord) {
                            gameState = gameState.checkWord()
                        }
                    },
                ) {
                    Text(
                        text = "Check",
                    )
                }
                Button(
                    onClick = {
                        gameState = GameState.create(words.random())
                    },
                ) {
                    Text(
                        text = "Restart",
                    )
                }
            }
        }
    }
}

fun KeyEvent.asTypedText(): String {
    return java.awt.event.KeyEvent.getKeyText(key.nativeKeyCode)
}

@Composable
fun ColumnScope.Word(
    wordState: WordState,
) {
    Row(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    ) {
        wordState.letters.forEach { letterState ->
            Letter(letterState)
        }
    }
}
