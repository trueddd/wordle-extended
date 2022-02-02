package data

sealed class LetterState {

    object Empty : LetterState()

    sealed class Filled(open val value: Char) : LetterState() {

        data class JustTyped(override val value: Char) : Filled(value) {

            constructor(string: String) : this(string.first())
        }

        data class NotPresented(override val value: Char) : Filled(value)

        data class HasInWord(override val value: Char) : Filled(value)

        data class OnRightPlace(override val value: Char) : Filled(value)
    }
}
