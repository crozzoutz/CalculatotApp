package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding


private const val PENDING_OPERATION = "Pending Operation"
private const val NUMBER1 = "Number1"
private const val NUMBER1_STORED = "Number1 stored"


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // variables to store numbers

    private var number1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //create a listener for number buttons

        val listener = View.OnClickListener { v ->
            val b = v as Button
            binding.newnumber.append(b.text)
        }

        // setting listeners to each number buttons

        binding.button0.setOnClickListener(listener)
        binding.button1.setOnClickListener(listener)
        binding.button2.setOnClickListener(listener)
        binding.button3.setOnClickListener(listener)
        binding.button4.setOnClickListener(listener)
        binding.button5.setOnClickListener(listener)
        binding.button6.setOnClickListener(listener)
        binding.button7.setOnClickListener(listener)
        binding.button8.setOnClickListener(listener)
        binding.button9.setOnClickListener(listener)
        binding.buttondot.setOnClickListener(listener)

        //  listeners for operands

        val operandListener = View.OnClickListener { v ->
            val operand = (v as Button).text.toString()
            try {
                val value = binding.newnumber.text.toString().toDouble()
                performOperation(value, operand)
            } catch (e: NumberFormatException) {
                binding.newnumber.setText("")

            }
            pendingOperation = operand
            displayOperation.text = pendingOperation
        }

        // setting listeners to operand buttons

        binding.buttonequals.setOnClickListener(operandListener)
        binding.buttonadd.setOnClickListener(operandListener)
        binding.buttonsubract.setOnClickListener(operandListener)
        binding.buttonmultiply.setOnClickListener(operandListener)
        binding.buttondivide.setOnClickListener(operandListener)

        binding.buttonneg.setOnClickListener { v ->
            val value = binding.newnumber.text.toString()
            if (value.isEmpty()) {
                binding.newnumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    binding.newnumber.setText(doubleValue.toString())

                } catch (e: NumberFormatException) {
                    //newNumber was "-" or "." , so cleaning it
                    binding.newnumber.setText("")
                }
            }
        }


    }

    private fun performOperation(value: Double, operation: String) {
        if (number1 == null) {
            number1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> number1 = value
                "/" -> number1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    number1!! / value
                }

                "*" -> number1 = number1!! * value
                "-" -> number1 = number1!! - value
                "+" -> number1 = number1!! + value
            }
        }
        binding.result.setText(number1.toString())
        binding.newnumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (number1 != null) {
            outState.putDouble(NUMBER1, number1!!)
            outState.putBoolean(NUMBER1_STORED, true)
        }
        outState.putString(PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        number1 = if (savedInstanceState.getBoolean(NUMBER1_STORED, false)) {
            savedInstanceState.getDouble(NUMBER1)

        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(PENDING_OPERATION, "")
        displayOperation.text = pendingOperation

    }
}
