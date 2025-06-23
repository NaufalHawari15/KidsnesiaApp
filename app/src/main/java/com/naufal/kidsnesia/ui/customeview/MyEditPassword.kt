package com.naufal.kidsnesia.ui.customeview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.naufal.kidsnesia.R

class MyEditPassword  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                when {
                    text.isEmpty() -> setError(context.getString(R.string.empy_pass), null)
                    text.contains(" ") -> setError(context.getString(R.string.no_spacy_pass), null)
                    text.length < 8 -> setError(context.getString(R.string.pass_less_than_8), null)
                    else -> error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}