package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


private const val PENDING_OPERATION= "Pending Operation"
private const val NUMBER1= "Number1"
private const val NUMBER1_STORED="Number1 stored"



class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // variables to store numbers

    private var number1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newnumber)

        //Input for buttons

        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttondot)
        val buttonNeg :Button = findViewById(R.id.buttonneg)

        //Operand buttons

        val buttonEqual = findViewById<Button>(R.id.buttonequals)
        val buttonAdd = findViewById<Button>(R.id.buttonadd)
        val buttonSubract = findViewById<Button>(R.id.buttonsubract)
        val buttonMultiply = findViewById<Button>(R.id.buttonmultiply)
        val buttonDivide = findViewById<Button>(R.id.buttondivide)

        //create a listener for number buttons

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        // setting listeners to each number buttons

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        //  listeners for operands

        val operandListener = View.OnClickListener { v ->
            val operand = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, operand)
            } catch (e: NumberFormatException) {
                newNumber.setText("")

            }
            pendingOperation = operand
            displayOperation.text = pendingOperation
        }

        // setting listeners to operand buttons

        buttonEqual.setOnClickListener(operandListener)
        buttonAdd.setOnClickListener(operandListener)
        buttonSubract.setOnClickListener(operandListener)
        buttonMultiply.setOnClickListener(operandListener)
        buttonDivide.setOnClickListener(operandListener)

        buttonNeg.setOnClickListener { v->
            val value = newNumber.text.toString()
            if (value.isEmpty()){
                newNumber.setText("-")
            }else{
                try{
                    var doubleValue = value.toDouble()
                    doubleValue*= - 1
                    newNumber.setText(doubleValue.toString())

                }catch (e:NumberFormatException){
                    //newNumber was "-" or "." , so cleaning it
                    newNumber.setText("")
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
        result.setText(number1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (number1!= null){
            outState.putDouble(NUMBER1,number1!!)
            outState.putBoolean(NUMBER1_STORED,true)
        }
        outState.putString(PENDING_OPERATION,pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        number1 = if (savedInstanceState.getBoolean(NUMBER1_STORED,false)){
            savedInstanceState.getDouble(NUMBER1)

        }else{
            null
        }
        pendingOperation= savedInstanceState.getString(PENDING_OPERATION,"")
        displayOperation.text = pendingOperation

    }
}
