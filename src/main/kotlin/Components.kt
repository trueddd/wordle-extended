import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.LetterState

fun Modifier.modifyIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        block()
    } else {
        this
    }
}

@Preview
@Composable
fun Letter(
    letterState: LetterState = LetterState.Filled.JustTyped('A'),
) {
    val value = when (letterState) {
        is LetterState.Empty -> " "
        is LetterState.Filled -> letterState.value.toString()
    }
    val backgroundColor = when (letterState) {
        is LetterState.Empty, is LetterState.Filled.JustTyped -> AppColors.Grey
        is LetterState.Filled.OnRightPlace -> AppColors.Green
        is LetterState.Filled.HasInWord -> AppColors.Yellow
        is LetterState.Filled.NotPresented -> AppColors.Black
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .defaultMinSize(minWidth = 88.dp)
            .background(backgroundColor)
            .modifyIf(letterState is LetterState.Filled.JustTyped) {
                border(
                    color = AppColors.Black,
                    width = 4.dp,
                )
            }
    ) {
        Text(
            text = value,
            fontSize = 72.sp,
            color = AppColors.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 4.dp),
        )
    }
}