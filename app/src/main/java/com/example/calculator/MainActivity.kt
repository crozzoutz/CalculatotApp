package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

   // private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]
        val viewModel = ViewModelProvider(this)[BigDecimalViewModel::class.java]

        viewModel.stringResult.observe(this, Observer<String>{ stringResult->binding.result.setText(stringResult)})
        viewModel.stringNewNumber.observe(this,Observer<String>{stringNumber->binding.newnumber.setText(stringNumber)})
        viewModel.stringDisplayOperation.observe(this,Observer<String>{stringOperation-> binding.operation.text = stringOperation })


        //create a listener for number buttons

        val listener = View.OnClickListener { v ->
            viewModel.numberPressed((v as Button).text.toString())

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
            viewModel.operandPressed((v as Button).text.toString())

        }

        // setting listeners to operand buttons

        binding.buttonequals.setOnClickListener(operandListener)
        binding.buttonadd.setOnClickListener(operandListener)
        binding.buttonsubract.setOnClickListener(operandListener)
        binding.buttonmultiply.setOnClickListener(operandListener)
        binding.buttondivide.setOnClickListener(operandListener)

        binding.buttonneg.setOnClickListener { v ->
            viewModel.negPressed()

        }
    }
}
