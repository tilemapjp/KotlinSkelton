package cz.sazel.hellokotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cz.sazel.hellokotlin.console.AndroidConsole
import cz.sazel.hellokotlin.console.IConsole
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

/**
 * In Android this activity is a start point.
 */
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    lateinit var sharedClass: SharedClass

    lateinit private var console: IConsole

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btStart.setOnClickListener { startClick(it) }
        console = AndroidConsole(tvConsole)
        sharedClass = SharedClass(console, Math())
        sharedClass.platform = "Android"

        printHello()
        printNumber()
    }

    private fun printNumber() {
        async {
            val primes: List<Long> = calcPrimes(10)
            val output = primes.map { prime -> prime.toString() }.reduce { s1, s2 -> s1 + "\n" + s2 }
            console.println(output)
        }
    }

    private fun printHello() {
        launch {
            delay(1000)
            console.println("Kotlin Coroutines World!")
        }
        console.println("Hello")
    }

    suspend fun calcPrimes(n: Long) = sharedClass.calcPrimes(n)

    protected fun startClick(v: View) {
        sharedClass.printMe()
        //we can't have direct sharedClass.printPrimes(1000) there, we need to do it asynchronously to not block the UI
        async {
            val primes: List<Long> = calcPrimes(1000)
            val output = primes.map { prime -> prime.toString() }.reduce { s1, s2 -> s1 + "\n" + s2 }
            console.println(output)
        }
    }

}
