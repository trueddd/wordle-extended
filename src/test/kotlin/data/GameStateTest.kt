package data

import org.junit.jupiter.api.Test

internal class GameStateTest {

    private fun String.asJustTypedLetter(): LetterState {
        return LetterState.Filled.JustTyped(this)
    }

    private fun String.asWordStateOfJustTypedLetters(): WordState {
        return WordState.Active(map { it.toString().asJustTypedLetter() })
    }

    private fun createGameState(word: String, wordForLettersState: String): GameState {
        return GameState(
            attempts = listOf(
                wordForLettersState.uppercase().asWordStateOfJustTypedLetters(),
            ),
            word = word.uppercase(),
        )
    }

    @Test
    fun `checkWord - successful 1`() {
        val checked = createGameState(wordForLettersState = "world", word = "world").checkWord()
        assert(checked.wordGuessed)
    }

    @Test
    fun `checkWord - successful 2`() {
        val checked = createGameState(wordForLettersState = "loner", word = "loner").checkWord()
        assert(checked.wordGuessed)
    }

    @Test
    fun `checkWord - no letter presented`() {
        val checked = createGameState(wordForLettersState = "check", word = "world").checkWord()
        assert(!checked.guessedAnyLetter)
    }

    @Test
    fun `checkWord - any of letters presented 1`() {
        val checked = createGameState(wordForLettersState = "cargo", word = "world").checkWord()
        assert(checked.guessedAnyLetter)
    }

    @Test
    fun `checkWord - any of letters presented 2`() {
        val checked = createGameState(wordForLettersState = "maker", word = "world").checkWord()
        assert(checked.guessedAnyLetter)
    }
}
