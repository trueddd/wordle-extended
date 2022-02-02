package data

data class GameState(
    val attempts: List<WordState>,
    val word: String,
) {

    val canCheckWord: Boolean
        get() = attempts.firstOrNull { it is WordState.Active }?.letters?.all { it is LetterState.Filled } ?: false

    val wordGuessed: Boolean
        get() {
            return attempts.lastOrNull { it is WordState.Locked }?.letters
                ?.all { it is LetterState.Filled.OnRightPlace } ?: false
        }

    val guessedAnyLetter: Boolean
        get() = attempts.lastOrNull { it is WordState.Locked }?.letters
            ?.any { it is LetterState.Filled.HasInWord || it is LetterState.Filled.OnRightPlace } ?: false

    val finished: Boolean
        get() = attempts.all { it is WordState.Locked }

    companion object {

        fun create(word: String): GameState {
            println("Guessed word: $word")
            return GameState(
                attempts = List(6) { WordState.empty(5) },
                word = word,
            )
        }
    }

    fun onCharacterTyped(character: String): GameState {
        val indexOfActiveAttempt = attempts.indexOfFirst { it is WordState.Active }
        return if (indexOfActiveAttempt >= 0) {
            copy(attempts = attempts.mapIndexed { index, wordState -> if (index == indexOfActiveAttempt) wordState.addLetter(character) else wordState })
        } else {
            this
        }
    }

    fun onBackspaceClicked(): GameState {
        val indexOfActiveAttempt = attempts.indexOfFirst { it is WordState.Active }
        return if (indexOfActiveAttempt >= 0) {
            copy(attempts = attempts.mapIndexed { index, wordState -> if (index == indexOfActiveAttempt) wordState.removeLast() else wordState })
        } else {
            this
        }
    }

    fun checkWord(): GameState {
        val activeAttemptIndex = attempts.indexOfFirst { it is WordState.Active }
        val wordState = if (activeAttemptIndex >= 0) attempts[activeAttemptIndex] else return this
        val letters = wordState.letters
            .map { if (it is LetterState.Filled) it else return this }
            .mapIndexed { index, letter ->
                if (word[index].lowercase() == letter.value.lowercase())
                    LetterState.Filled.OnRightPlace(letter.value)
                else
                    letter
            }
            .map { letter ->
                if (letter is LetterState.Filled.JustTyped && word.indexOf(letter.value, ignoreCase = true) in 0 .. word.length)
                    LetterState.Filled.HasInWord(letter.value)
                else
                    letter
            }
            .map { letter ->
                if (letter is LetterState.Filled.JustTyped)
                    LetterState.Filled.NotPresented(letter.value)
                else
                    letter
            }
        return copy(attempts = attempts.mapIndexed { index, state -> if (index == activeAttemptIndex) WordState.Locked(letters) else state })
    }
}
