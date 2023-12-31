package com.example.kotlingenerics

/**
 * 1.Make a reusable class with generics
 */

// クイズを各クラスで表現
class FillInTheBlankQuestion(
    val questionText: String,
    val answer: String,
    val difficulty: String
)
class TrueOrFalseQuestion(
    val questionText: String,
    val answer: Boolean,
    val difficulty: String
)
class NumericQuestion(
    val questionText: String,
    val answer: Int,
    val difficulty: String
)

// 親クラスで共通部を定義してanswerのみサブクラスで定義
// →answerのデータ型が違う問題を追加するたびにサブクラスを定義する手間がある
// →そもそもanswerプロパティを持たない親クラスQuestionがあるのも不自然
open class ParentQuestion(
    val questionText: String,
    val difficulty: String
)
class SubQuestion1(
    val answer: Boolean,
    questionText: String,
    difficulty: String
): ParentQuestion(questionText, difficulty)
class SubQuestion2(
    val answer: Int,
    questionText: String,
    difficulty: String
): ParentQuestion(questionText, difficulty)

// genericsで表現
// →違うのはanswerのデータ型のみなので、この方法が一番シンプルでいい
class Question<T>(
    val questionText: String,
    val answer: T,
    val difficulty: String
)

// genericsで表現したクラスをインスタンス化
fun main() {
    val que1 = Question("aaa", "bbb", "easy")
    val que2 = Question("aaa", 1, "normal")
    val que3 = Question("aaa", true, "hard")
}

/**
 * 2.Use an enum class
 */
// enum
enum class Difficulty {
    EASY,
    NORMAL,
    HARD
}
class Question2<T>(
    val questionText: String,
    val answer: T,
    val difficulty: Difficulty
)

// genericsで表現したクラスをインスタンス化
fun main2() {
    val que1 = Question2("aaa", "bbb", Difficulty.EASY)
    val que2 = Question2("aaa", 1, Difficulty.NORMAL)
    val que3 = Question2("aaa", true, Difficulty.HARD)
}

/**
 * 3.Use a data class
 */
// メソッドを持たないプロパティのみのクラスであればdata classを使用
// data classでしか利用できないメソッドがいくつかある　→　例えばtoString()がデフォルトで実装されている
data class Question3<T>(
    val questionText: String,
    val answer: T,
    val difficulty: Difficulty
)

/*
data class では以下のメソッドが実装されている
equals()
hashCode(): このメソッドは、特定のコレクション型を操作する際に使用されます。
toString()
componentN(): component1()、component2()（以下同様）
copy()
 */

// toString()
fun main3() {
    val que1 = Question3("aaa", "bbb", Difficulty.EASY)
    println("$que1") // Question3(questionText=aaa, answer=bbb, difficulty=EASY)
    // data classではなく普通のclassの場合、インスタンスをtoString()してもインスタンスIDが出力されるだけ
}
// equals()
fun main4() {
    val que1 = Question3("aaa", "bbb", Difficulty.EASY)
    val que2 = Question3("aaa", "bbb", Difficulty.EASY)
    println("${que1.equals(que2)}")
    // data class　→　プロパティの値が同じかどうかの判定(構造同一)
    // class　　　　→　インスタンスが同じかどうかの判定(参照同一)
}

/**
 * 4.Use a singleton object
 */
// singleton objectはobject宣言かcompanion object宣言で作成できる
class Quiz {
    companion object StudentProgress {
        var total = 10
        var answered = 3
    }

    val que1 = Question3("aaa", "bbb", Difficulty.EASY)
}

// companion object内で宣言されたプロパティはクラス外から、クラス名.プロパティ名でアクセス可能
fun main5() {
    println("${Quiz.total}")
    println("${Quiz.answered}")
}

/**
 * 5.Extend classes with new properties and methods
 */

/**
 * 6.Rewrite extension functions using interfaces
 */
interface ProgressPrintable {
    val progressText: String
    fun printProgressBar()
}

class Quiz2 : ProgressPrintable {
    companion object StudentProgress {
        var total = 10
        var answered = 3
    }

    override val progressText: String
        get() = "$answered of $total answered"

    override fun printProgressBar() {
        repeat(answered) { print("▓") }
        repeat(total - answered) { print("▒") }
        println()
        println("$progressText")
    }
}

fun main6() {
    Quiz2().printProgressBar()
}

/**
 * 7.Use scope functions to access class properties and methods
 */
data class Question4<T>(
    val questionText: String,
    val answer: T,
    val difficulty: Difficulty
)

fun main7() {
    val que1 = Question4("aaa", "111", Difficulty.EASY)
    val que2 = Question4("bbb", "222", Difficulty.NORMAL)

    // scope関数を使わないパターン
    println("${que1.questionText},${que1.answer},${que1.difficulty}")
    println("${que2.questionText},${que2.answer},${que2.difficulty}")

    // let
    que1.let {
        println(
            "${it.questionText},${it.answer},${it.difficulty}"
        )
    }

    // apply
    // 参照する変数も省略できる
    // applyはレシーバのインスタンスを返すので変数に入れることも可能
    // val que = que1.apply {
    que1.apply {
        println("$questionText,$answer,$difficulty")
    }
}