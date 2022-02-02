package data

sealed class WordState(
    open val letters: List<LetterState>,
) {

    data class Active(override val letters: List<LetterState>) : WordState(letters)

    data class Locked(override val letters: List<LetterState>) : WordState(letters)

    fun addLetter(letter: String): WordState {
        if (this is Locked) return this
        val indexOfNewChar = letters.indexOfFirst { it is LetterState.Empty }
        return if (indexOfNewChar >= 0) {
            Active(
                letters.mapIndexed { index, letterState ->
                    if (index == indexOfNewChar)
                        LetterState.Filled.JustTyped(letter)
                    else
                        letterState
                }
            )
        } else {
            this
        }
    }

    fun removeLast(): WordState {
        if (this is Locked) return this
        val indexOfLastChar = letters.indexOfLast { it is LetterState.Filled }
        return if (indexOfLastChar >= 0) {
            Active(
                letters.mapIndexed { index, letterState ->
                    if (index == indexOfLastChar)
                        LetterState.Empty
                    else
                        letterState
                }
            )
        } else {
            this
        }
    }

    companion object {

        fun empty(size: Int): Active {
            return Active(List(size) { LetterState.Empty })
        }
    }
}
